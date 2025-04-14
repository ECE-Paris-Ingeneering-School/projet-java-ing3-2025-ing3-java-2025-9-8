package view;

import dao.DiscountDAO;
import dao.TicketDAO;
import model.Discount;
import model.Ticket;
import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartFrame extends JFrame {
    private User currentUser;
    private JTable table;
    private DefaultTableModel model;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JLabel promoDetailsLabel;

    public CartFrame(User user) {
        super("Mon Panier");
        this.currentUser = user;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout());
        headerPanel.setBackground(new Color(242, 224, 200));
        JLabel headerLabel = new JLabel("Mon Panier", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 26));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table de panier
        String[] colNames = {"ID", "sessionId", "Nom", "Prix unitaire", "Qté", "Sous-total", "Date ajout"};
        model = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        JScrollPane tableScroll = new JScrollPane(table);
        add(tableScroll, BorderLayout.CENTER);

        // Footer : affichage de la réduction, du détail des promos et du total
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(new Color(242, 224, 200));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        discountLabel = new JLabel("Réduction appliquée : -0,00 €", SwingConstants.CENTER);
        discountLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        discountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        promoDetailsLabel = new JLabel("Promotions appliquées : ", SwingConstants.CENTER);
        promoDetailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        promoDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalLabel = new JLabel("Total : 0,00 €", SwingConstants.CENTER);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(242, 224, 200));
        JButton removeButton = new JButton("Retirer l'article");
        removeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        removeButton.addActionListener(e -> removeSelectedItem());
        buttonPanel.add(removeButton);

        JButton checkoutButton = new JButton("Valider l'achat");
        checkoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkoutButton.addActionListener(e -> validatePurchase());
        buttonPanel.add(checkoutButton);

        footerPanel.add(discountLabel);
        footerPanel.add(Box.createVerticalStrut(5));
        footerPanel.add(promoDetailsLabel);
        footerPanel.add(Box.createVerticalStrut(5));
        footerPanel.add(totalLabel);
        footerPanel.add(Box.createVerticalStrut(10));
        footerPanel.add(buttonPanel);
        add(footerPanel, BorderLayout.SOUTH);

        loadCartItems();
    }

    private void loadCartItems() {
        model.setRowCount(0);
        List<Ticket> items = TicketDAO.getCartItems(currentUser.getId());
        for (Ticket t : items) {
            int sessionId = t.getSessionId();
            double price = t.getPrice();
            int qty = t.getQuantity();
            double subTotal = price * qty;
            model.addRow(new Object[]{
                    t.getId(),
                    sessionId,
                    getItemName(sessionId),
                    price,
                    qty,
                    subTotal,
                    t.getAddedDate()
            });
        }
        recalcTotal();
    }

    private void recalcTotal() {
        double totalBefore = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            totalBefore += (double) model.getValueAt(i, 5);
        }
        double promoDiscount = 0.0;
        List<String> appliedPromos = new ArrayList<>();
        List<Discount> activePromos = DiscountDAO.getActiveDiscounts();
        for (Discount d : activePromos) {
            String promoType = d.getDiscountType().trim();
            for (Ticket t : TicketDAO.getCartItems(currentUser.getId())) {
                int ticketSessionId = t.getSessionId();
                int qty = t.getQuantity();
                double price = t.getPrice();
                // Pour vos produits, si l'ID >= 101, le ticket est de type "Produit"
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
        discountLabel.setText(String.format("Réduction appliquée : -%.2f €", promoDiscount));
        totalLabel.setText(String.format("Total : %.2f €", totalAfter));
        String promoDetailString = appliedPromos.isEmpty() ? "Aucune promotion appliquée"
                : appliedPromos.stream().collect(Collectors.joining(", "));
        promoDetailsLabel.setText("Promotions appliquées : " + promoDetailString);
    }

    private void removeSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int ticketId = (int) model.getValueAt(selectedRow, 0);
            if (TicketDAO.removeItemFromCart(ticketId)) {
                JOptionPane.showMessageDialog(this, "Article retiré du panier.");
                loadCartItems();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'article.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Aucun article sélectionné.");
        }
    }

    // Nouveau flux de validation : Lorsqu'on clique sur "Valider l'achat"
    // On ferme le panier et on ouvre le CheckoutFrame.
    private void validatePurchase() {
        List<Ticket> items = TicketDAO.getCartItems(currentUser.getId());
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide.");
            return;
        }
        // Fermer le CartFrame et lancer CheckoutFrame
        this.dispose();
        new CheckoutFrame(currentUser, () -> {
            // Une fois le CheckoutFrame terminé (infos personnelles et bancaires), on ouvre le récapitulatif
            openOrderSummary();
        }).setVisible(true);
    }

    // Ouvre une nouvelle fenêtre contenant le récapitulatif de commande (OrderSummaryPanel)
    private void openOrderSummary() {
        List<Ticket> items = TicketDAO.getCartItems(currentUser.getId());
        JFrame summaryFrame = new JFrame("Récapitulatif de commande");
        summaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        summaryFrame.setSize(800, 600);
        summaryFrame.setLocationRelativeTo(null);

        OrderSummaryPanel summaryPanel = new OrderSummaryPanel(currentUser, items, () -> {
            // Callback après confirmation du récapitulatif : finalisation de la commande
            boolean ok = TicketDAO.checkoutCart(currentUser.getId());
            if (ok) {
                JOptionPane.showMessageDialog(null, "Achat validé !");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la validation de l'achat.");
            }
        });
        summaryFrame.add(summaryPanel);
        summaryFrame.setVisible(true);
    }

    private String getItemName(int sessionId) {
        if (sessionId < 101)
            return "Séance #" + sessionId;
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
