package view;

import dao.DiscountDAO;
import model.Discount;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminDiscountPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public AdminDiscountPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240));

        // --- Titre ---
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 236, 227));
        JLabel titleLabel = new JLabel("Gestion des Offres / Promotions");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Tableau ---
        // 5 colonnes : ID, Nom, Description, Conditions, Remise
        String[] columns = {"ID", "Nom", "Description", "Conditions", "Remise"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) {
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

        // Centrer le texte
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(255, 250, 240));
        scrollPane.setPreferredSize(new Dimension(900, 350));

        // Panel pour centrer horizontalement
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerPanel.setBackground(new Color(255, 250, 240));
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // --- Panneau de boutons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 236, 227));

        JButton addButton = new JButton("Ajouter Offre");
        JButton editButton = new JButton("Modifier Offre");
        JButton deleteButton = new JButton("Supprimer Offre");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Chargement initial
        loadDiscounts();
        setColumnWidths();

        // Appliquer le centerRenderer
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Listeners
        addButton.addActionListener(e -> {
            // Ouvre un DiscountForm sans ID => ajout
            new DiscountForm(null, this).setVisible(true);
        });
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int discountId = (int) model.getValueAt(selectedRow, 0);
                // Ouvre DiscountForm en mode édition
                new DiscountForm(discountId, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une offre à modifier.");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int discountId = (int) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Confirmez-vous la suppression de cette offre ?",
                        "Supprimer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = DiscountDAO.deleteDiscount(discountId);
                    if (success) {
                        loadDiscounts();
                        JOptionPane.showMessageDialog(this, "Offre supprimée.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une offre à supprimer.");
            }
        });
    }

    /**
     * Récupère les offres depuis la BDD via DiscountDAO.
     */
    public void loadDiscounts() {
        model.setRowCount(0);
        List<Discount> discounts = DiscountDAO.getAllDiscounts();
        for (Discount d : discounts) {
            model.addRow(new Object[]{
                    d.getId(),
                    d.getName(),
                    d.getDescription(),
                    d.getConditions(),
                    d.getValue()
            });
        }
    }

    private void setColumnWidths() {
        // ID, Nom, Description, Conditions, Remise
        int[] widths = {60, 150, 300, 200, 100};
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
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 235, 220));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(defaultColor);
            }
        });
    }
}
