package view;

import dao.ProductDAO;
import model.Product;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminBoutiquePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public AdminBoutiquePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240)); // Fond pastel clair

        // ---- Titre ----
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 236, 227));
        JLabel titleLabel = new JLabel("Gestion des Produits Boutique");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // ---- Création du tableau (sans colonne Image) ----
        String[] columns = {"ID", "Nom du Produit", "Prix (€)", "Stock"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setForeground(new Color(50, 50, 50));
        table.setBackground(new Color(255, 250, 240));
        table.setSelectionBackground(new Color(255, 220, 200));

        // En-tête
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(255, 236, 227));
        table.getTableHeader().setForeground(new Color(60, 40, 40));
        table.getTableHeader().setReorderingAllowed(false);

        // Centrage du texte
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Désactiver l'auto-resize pour fixer la largeur
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // JScrollPane sans bordure grise
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(255, 250, 240));
        scrollPane.setPreferredSize(new Dimension(900, 350));

        // Panel pour centrer horizontalement le JScrollPane
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerPanel.setBackground(new Color(255, 250, 240));
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // ---- Panneau de boutons ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 236, 227));

        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Charger les produits
        loadProducts();
        setColumnWidths();

        // Listeners pour les boutons
        addButton.addActionListener(e -> new ProductForm(null, this).setVisible(true));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (int) model.getValueAt(selectedRow, 0);
                new ProductForm(productId, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un produit à modifier.");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (int) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Confirmez-vous la suppression de ce produit ?",
                        "Supprimer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = ProductDAO.deleteProduct(productId);
                    if (success) {
                        loadProducts();
                        JOptionPane.showMessageDialog(this, "Produit supprimé.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un produit à supprimer.");
            }
        });
    }

    public void loadProducts() {
        model.setRowCount(0);
        List<Product> products = ProductDAO.getAllProducts();
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStock()
            });
        }
    }

    private void setColumnWidths() {
        int[] widths = {60, 300, 100, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 250, 240));
        btn.setForeground(new Color(60, 40, 40));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        btn.addMouseListener(new MouseAdapter() {
            Color defaultColor = btn.getBackground();
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 235, 220));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(defaultColor);
            }
        });
    }
}
