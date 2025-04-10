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
    private JLabel discountLabel;
    private Runnable onPurchaseCallback;
    private Runnable onUserUpdateCallback;

    public CartFrame(User user, Runnable onPurchaseCallback, Runnable onUserUpdateCallback) {
        super("Mon Panier");
        this.currentUser = user;
        this.onPurchaseCallback = onPurchaseCallback;
        this.onUserUpdateCallback = onUserUpdateCallback;

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

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(new Color(242, 224, 200));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        discountLabel = new JLabel("Réduction appliquée : -0.00 €");
        discountLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        discountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        totalLabel = new JLabel("Total : 0.00 €");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

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

    private void recalcTotal() {
        double totalBefore = 0.0;
        double totalSession = 0.0;
        int sessionQty = 0;
        boolean hasBrassiere = false, hasEnsemble = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            String name = model.getValueAt(i, 2).toString();
            double price = (double) model.getValueAt(i, 3);
            int qty = (int) model.getValueAt(i, 4);
            double subtotal = price * qty;

            totalBefore += subtotal;

            if (name.startsWith("Séance #")) {
                sessionQty += qty;
                totalSession += subtotal;
            }

            if (name.equalsIgnoreCase("Brassières de sport")) hasBrassiere = true;
            if (name.equalsIgnoreCase("Ensemble de sport")) hasEnsemble = true;
        }

        double discount = 0.0;

        if (sessionQty >= 4) {
            discount += totalSession * 0.2;
        }

        if (hasBrassiere && hasEnsemble) {
            double sumBE = 0.0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = model.getValueAt(i, 2).toString();
                if (name.equalsIgnoreCase("Brassières de sport") || name.equalsIgnoreCase("Ensemble de sport")) {
                    double price = (double) model.getValueAt(i, 3);
                    int qty = (int) model.getValueAt(i, 4);
                    sumBE += price * qty;
                }
            }
            discount += sumBE * 0.2;
        }

        double totalAfter = totalBefore - discount;

        discountLabel.setText(String.format("Réduction appliquée : -%.2f €", discount));
        totalLabel.setText(String.format("Total : %.2f €", totalAfter));
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
        List<Ticket> items = TicketDAO.getCartItems(currentUser.getId());
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide.");
            return;
        }

        new CheckoutFrame(currentUser, items, () -> {
            loadCartItems();
            if (onPurchaseCallback != null) onPurchaseCallback.run();
        }, onUserUpdateCallback).setVisible(true);
    }
}
