package view;

import model.Ticket;
import model.User;
import dao.TicketDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class OrderSummaryPanel extends JPanel {
    private User currentUser;
    private List<Ticket> cartItems;
    private Runnable onCheckoutComplete;

    public OrderSummaryPanel(User user, List<Ticket> cartItems, Runnable onCheckoutComplete) {
        this.currentUser = user;
        this.cartItems = cartItems;
        this.onCheckoutComplete = onCheckoutComplete;

        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240));

        JLabel title = new JLabel("Résumé de votre commande", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(255, 250, 240));

        double total = 0.0;
        double totalSessionPrice = 0.0;
        int totalSessionQty = 0;
        boolean hasBrassiere = false, hasEnsemble = false;
        double brassiereEtEnsembleTotal = 0.0;

        for (Ticket t : cartItems) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(100, 100));

            String name = getItemName(t.getSessionId());
            int qty = t.getQuantity();
            double lineTotal = t.getPrice() * qty;

            if (name.startsWith("Séance #") && t.getRace() != null) {
                ImageIcon icon = getDogIcon(t.getRace());
                if (icon != null) imageLabel.setIcon(icon);
            } else {
                ImageIcon icon = new ImageIcon(getImagePath(t.getSessionId()));
                Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            }

            itemPanel.add(imageLabel, BorderLayout.WEST);

            JLabel nameLabel = new JLabel(name + " - Qté : " + qty);
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            itemPanel.add(nameLabel, BorderLayout.CENTER);

            JLabel priceLabel = new JLabel(String.format("%.2f €", lineTotal));
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            itemPanel.add(priceLabel, BorderLayout.EAST);

            contentPanel.add(itemPanel);

            if (name.startsWith("Séance #")) {
                totalSessionQty += qty;
                totalSessionPrice += lineTotal;
            } else {
                total += lineTotal;
            }

            if (name.equalsIgnoreCase("Brassières de sport")) {
                hasBrassiere = true;
                brassiereEtEnsembleTotal += lineTotal;
            } else if (name.equalsIgnoreCase("Ensemble de sport")) {
                hasEnsemble = true;
                brassiereEtEnsembleTotal += lineTotal;
            }
        }

        double discount = 0.0;
        if (totalSessionQty >= 4) {
            double discountSession = totalSessionPrice * 0.2;
            discount += discountSession;
            total += totalSessionPrice - discountSession;
        } else {
            total += totalSessionPrice;
        }

        if (hasBrassiere && hasEnsemble) {
            double discountBE = brassiereEtEnsembleTotal * 0.2;
            discount += discountBE;
            total -= discountBE;
        }

        JLabel discountLabel = new JLabel("Réduction appliquée : -" + String.format("%.2f €", discount), SwingConstants.CENTER);
        discountLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        discountLabel.setForeground(new Color(0, 128, 0));
        discountLabel.setBorder(new EmptyBorder(10, 0, 5, 0));

        JLabel totalLabel = new JLabel("Total après réduction : " + String.format("%.2f €", total), SwingConstants.CENTER);
        totalLabel.setFont(new Font("Serif", Font.BOLD, 20));
        totalLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton confirmButton = new JButton("Valider l'achat");
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(e -> {
            boolean ok = TicketDAO.checkoutCart(currentUser.getId());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Achat validé avec succès !");
                if (onCheckoutComplete != null) onCheckoutComplete.run();
                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la validation de l'achat.");
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 250, 240));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(discountLabel);
        bottomPanel.add(totalLabel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(confirmButton);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private String getItemName(int sessionId) {
        if (sessionId <= 100) return "Séance #" + sessionId;
        return switch (sessionId) {
            case 2001 -> "Brassières de sport";
            case 2002 -> "Ensemble de sport";
            case 2003 -> "Poids";
            case 2004 -> "Tapis de yoga";
            case 1001 -> "Chaussettes de yoga";
            case 1002 -> "Barres de céréales";
            case 1003 -> "Bouteilles énergisantes";
            case 1004 -> "Serviettes";
            default -> "Article #" + sessionId;
        };
    }

    private String getImagePath(int sessionId) {
        return switch (sessionId) {
            case 2001 -> "photos/brassieres.png";
            case 2002 -> "photos/ensemble.png";
            case 2003 -> "photos/poids.png";
            case 2004 -> "photos/tapis.png";
            case 1001 -> "photos/chaussettes.png";
            case 1002 -> "photos/barres.png";
            case 1003 -> "photos/bouteilles.png";
            case 1004 -> "photos/serviettes.png";
            default -> "photos/default.png";
        };
    }

    private ImageIcon getDogIcon(String breed) {
        String path = switch (breed.toLowerCase()) {
            case "golden retriever" -> "photos/golden.png";
            case "labrador" -> "photos/labrador.png";
            case "beagle" -> "photos/beagle.png";
            case "bulldog" -> "photos/bulldog.png";
            case "cocker spaniel" -> "photos/cocker.png";
            case "poodle" -> "photos/poodle.png";
            case "schnauzer" -> "photos/schnauzer.png";
            default -> "photos/races/default.png";
        };
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}