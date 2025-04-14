package view;

import dao.DiscountDAO;
import dao.TicketDAO;
import model.Discount;
import model.Ticket;
import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSummaryPanel extends JPanel {
    private User currentUser;
    private List<Ticket> cartItems;
    private Runnable onCheckoutComplete;

    /**
     * Construit un récapitulatif de commande affichant la liste des articles, le total et la réduction appliquée.
     * @param user L'utilisateur courant.
     * @param cartItems La liste des tickets du panier.
     * @param onCheckoutComplete Callback appelé lorsque l'utilisateur confirme la commande.
     */
    public OrderSummaryPanel(User user, List<Ticket> cartItems, Runnable onCheckoutComplete) {
        this.currentUser = user;
        this.cartItems = cartItems;
        this.onCheckoutComplete = onCheckoutComplete;

        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240));

        JLabel title = new JLabel("Récapitulatif de votre commande", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Panel pour afficher la liste des articles du panier
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(255, 250, 240));

        double totalBefore = 0.0;
        for (Ticket t : cartItems) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(100, 100));
            ImageIcon icon = new ImageIcon(getImagePath(t.getSessionId()));
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));

            String name = getItemName(t.getSessionId());
            int qty = t.getQuantity();
            double price = t.getPrice();
            double lineTotal = price * qty;

            itemPanel.add(imageLabel, BorderLayout.WEST);
            JLabel nameLabel = new JLabel(name + " - Qté : " + qty);
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            itemPanel.add(nameLabel, BorderLayout.CENTER);
            JLabel priceLabel = new JLabel(String.format("%.2f €", lineTotal));
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            itemPanel.add(priceLabel, BorderLayout.EAST);

            contentPanel.add(itemPanel);
            totalBefore += lineTotal;
        }

        // Calcul des promotions actives
        double promoDiscount = 0.0;
        List<String> appliedPromos = new ArrayList<>();
        List<Discount> activePromos = DiscountDAO.getActiveDiscounts();
        for (Discount d : activePromos) {
            String promoType = d.getDiscountType().trim();
            for (Ticket t : cartItems) {
                int ticketSessionId = t.getSessionId();
                int qty = t.getQuantity();
                double price = t.getPrice();
                String ticketCategory = (ticketSessionId >= 101) ? "Produit" : "Session";
                if (d.getTargetCategory().trim().equalsIgnoreCase(ticketCategory)
                        && ticketSessionId == d.getTargetId()
                        && qty >= d.getMinQuantity()) {
                    if ("pourcentage".equalsIgnoreCase(promoType)) {
                        double lineDiscount = price * qty * (d.getDiscountAmount() / 100.0);
                        promoDiscount += lineDiscount;
                        if (!appliedPromos.contains(d.getName()))
                            appliedPromos.add(d.getName() + " (-" + d.getDiscountAmount() + "%)");
                    } else if ("gratuit".equalsIgnoreCase(promoType)) {
                        int groupSize = d.getMinQuantity() + (int) d.getDiscountAmount();
                        int freeItems = (int) (qty / groupSize) * (int) d.getDiscountAmount();
                        double lineDiscount = freeItems * price;
                        promoDiscount += lineDiscount;
                        if (!appliedPromos.contains(d.getName()))
                            appliedPromos.add(d.getName() + " (" + freeItems + " gratuit(s))");
                    }
                }
            }
        }

        double totalAfter = totalBefore - promoDiscount;

        JLabel discountLabel = new JLabel(String.format("Réduction appliquée : -%.2f €", promoDiscount), SwingConstants.CENTER);
        discountLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        discountLabel.setForeground(new Color(0, 128, 0));
        discountLabel.setBorder(new EmptyBorder(10, 0, 5, 0));

        JLabel totalLabel = new JLabel(String.format("Total après réduction : %.2f €", totalAfter), SwingConstants.CENTER);
        totalLabel.setFont(new Font("Serif", Font.BOLD, 20));
        totalLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        String promoDetailString = appliedPromos.isEmpty() ? "Aucune promotion appliquée"
                : appliedPromos.stream().collect(Collectors.joining(", "));
        JLabel promoDetailsLabel = new JLabel("Promotions appliquées : " + promoDetailString, SwingConstants.CENTER);
        promoDetailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        promoDetailsLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JButton confirmButton = new JButton("Confirmer et finaliser l'achat");
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(e -> {
            if (onCheckoutComplete != null) {
                onCheckoutComplete.run();
            }
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 250, 240));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(discountLabel);
        bottomPanel.add(promoDetailsLabel);
        bottomPanel.add(totalLabel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(confirmButton);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private String getItemName(int sessionId) {
        if (sessionId < 101) return "Séance #" + sessionId;
        return switch (sessionId) {
            case 101 -> "Chaussettes de yoga";
            case 102 -> "Poids";
            case 103 -> "Tapis de yoga";
            case 104 -> "Barres de céréales";
            case 105 -> "Bouteilles énergisantes";
            case 106 -> "Brassières de sport";
            case 107 -> "Ensemble de sport";
            case 108 -> "Serviettes";
            default -> "Article #" + sessionId;
        };
    }

    private String getImagePath(int sessionId) {
        return switch (sessionId) {
            case 101 -> "photos/chaussettes.png";
            case 102 -> "photos/poids.png";
            case 103 -> "photos/tapis.png";
            case 104 -> "photos/barres.png";
            case 105 -> "photos/bouteilles.png";
            case 106 -> "photos/brassieres.png";
            case 107 -> "photos/ensemble.png";
            case 108 -> "photos/serviettes.png";
            default -> "photos/default.png";
        };
    }
}
