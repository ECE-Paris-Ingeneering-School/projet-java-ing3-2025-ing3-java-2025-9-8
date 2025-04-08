package view;

import dao.TicketDAO;
import model.Ticket;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartFrame extends JFrame {
    private User currentUser;
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;

    public CartFrame(User user) {
        super("Mon Panier");
        this.currentUser = user;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(242, 224, 200));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("Mon Panier", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 26));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        String[] colNames = {"ID", "sessionId", "Nom", "Prix unitaire", "Qté", "Sous-Total", "Date ajout"};
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

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        footerPanel.setBackground(new Color(242, 224, 200));

        JButton removeButton = new JButton("Retirer l'article");
        removeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        removeButton.addActionListener(e -> removeSelectedItem());
        footerPanel.add(removeButton);

        JButton checkoutButton = new JButton("Valider l'achat");
        checkoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkoutButton.addActionListener(e -> validatePurchase());
        footerPanel.add(checkoutButton);

        totalLabel = new JLabel("Total : 0.00 €");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        footerPanel.add(totalLabel);

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

            // Récupération du nom en fonction de l'ID du produit
            String name = getItemName(sessionId);

            double subTotal = price * qty;

            model.addRow(new Object[]{
                    t.getId(),
                    sessionId,
                    name,
                    price,
                    qty,
                    subTotal,
                    t.getAddedDate()
            });
        }
        recalcTotal();
    }

    private String getItemName(int sessionId) {
        // Si l'ID est <= 100, c'est une séance : "Séance #X"
        if (sessionId <= 100) {
            return "Séance #" + sessionId;
        } else if (sessionId == 2001) {
            return "Brassières de sport";
        } else if (sessionId == 2002) {
            return "Ensemble de sport";
        } else if (sessionId == 2003) {
            return "Poids";
        } else if (sessionId == 2004) {
            return "Tapis de yoga";
        }
        return "Article #" + sessionId; // Valeur par défaut pour d'autres produits
    }

    // Méthode modifiée pour appliquer les remises globalement sur l'ensemble des billets de session
    private void recalcTotal() {
        double total = 0.0;
        int totalSessionQty = 0;
        double totalSessionPrice = 0.0;
        boolean hasBrassiere = false, hasEnsemble = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            String name = (String) model.getValueAt(i, 2);
            double price = (double) model.getValueAt(i, 3);
            int qty = (int) model.getValueAt(i, 4);
            double lineTotal = price * qty;

            // Si c’est un billet de session (nom commençant par "Séance #")
            if (name.startsWith("Séance #")) {
                totalSessionQty += qty;
                totalSessionPrice += lineTotal;
            } else {
                total += lineTotal;
            }

            // Détection des articles pour la remise brassière/ensemble
            if (name.equalsIgnoreCase("Brassières de sport")) {
                hasBrassiere = true;
            } else if (name.equalsIgnoreCase("Ensemble de sport")) {
                hasEnsemble = true;
            }
        }

        // Remise de 20 % sur l'ensemble des billets de session si le total cumulé atteint 4 billets ou plus
        if (totalSessionQty >= 4) {
            totalSessionPrice *= 0.8;
        }
        total += totalSessionPrice;

        // Remise additionnelle de 20 % si le panier contient à la fois une brassière et un ensemble
        if (hasBrassiere && hasEnsemble) {
            double sumBE = 0.0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = (String) model.getValueAt(i, 2);
                if (name.equalsIgnoreCase("Brassières de sport") || name.equalsIgnoreCase("Ensemble de sport")) {
                    double price = (double) model.getValueAt(i, 3);
                    int qty = (int) model.getValueAt(i, 4);
                    sumBE += price * qty;
                }
            }
            double discount = sumBE * 0.2;
            total -= discount;
        }

        totalLabel.setText(String.format("Total : %.2f €", total));
    }

    private void removeSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int ticketId = (int) model.getValueAt(selectedRow, 0);
            boolean ok = TicketDAO.removeItemFromCart(ticketId);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Article retiré du panier.");
                loadCartItems();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Aucun article sélectionné.");
        }
    }

    private void validatePurchase() {
        boolean ok = TicketDAO.checkoutCart(currentUser.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Achat validé ! Panier vidé.");
            loadCartItems();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur ou panier vide.");
        }
    }
}
