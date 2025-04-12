package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminUsersPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public AdminUsersPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240)); // Fond pastel

        // ------------------(  En-tête : titre  )------------------
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 236, 227));
        JLabel titleLabel = new JLabel("Gérer les Utilisateurs");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // ------------------(  Tableau  )------------------
        // Colonnes : ID, Username, Email, Nom, Rôle
        String[] columns = {"ID", "Username", "Email", "Nom complet", "Rôle"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // On empêche l’édition directe
            }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setForeground(new Color(50, 50, 50));
        table.setBackground(new Color(255, 250, 240));
        table.setSelectionBackground(new Color(255, 220, 200));

        // Style de l'en-tête
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(255, 236, 227));
        table.getTableHeader().setForeground(new Color(60, 40, 40));
        table.getTableHeader().setReorderingAllowed(false);

        // Centrage du texte
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Désactiver l’auto-resize pour fixer la largeur des colonnes
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // JScrollPane sans bordure grise, fond pastel
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(255, 250, 240));
        scrollPane.setPreferredSize(new Dimension(1000, 350)); // Ajuster selon vos besoins

        // Panel pour centrer horizontalement le tableau
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerPanel.setBackground(new Color(255, 250, 240));
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // ------------------(  Panneau de Boutons  )------------------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 236, 227));

        JButton viewButton = new JButton("Voir détails");
        JButton editButton = new JButton("Modifier rôle");
        JButton deleteButton = new JButton("Supprimer");

        styleButton(viewButton);
        styleButton(editButton);
        styleButton(deleteButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Charger les utilisateurs depuis la base
        loadUsers();
        setColumnWidths();

        // Centrer le texte dans toutes les colonnes
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ------------------( Listeners )------------------
        viewButton.addActionListener(e -> viewUserDetails());
        editButton.addActionListener(e -> modifyUserRole());
        deleteButton.addActionListener(e -> deleteUser());
    }

    /**
     * Charge tous les utilisateurs depuis la BDD et remplit le tableau.
     */
    public void loadUsers() {
        model.setRowCount(0);
        List<User> users = UserDAO.getAllUsers();
        for (User u : users) {
            // ID, Username, Email, Nom, Rôle
            model.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getName(),
                    u.getRole()
            });
        }
    }

    /**
     * Ajuste la largeur des colonnes pour afficher les données sans barre horizontale.
     */
    private void setColumnWidths() {
        // Indice colonnes : 0..4 => ID, Username, Email, Nom complet, Rôle
        int[] widths = {60, 150, 250, 200, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Style pastel pour les boutons, effet relief + hover
     */
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

    // ------------------( Méthodes pour les boutons )------------------

    /**
     * Affiche les détails complets de l'utilisateur sélectionné
     * (Vous pouvez créer un UserDetailFrame si besoin).
     */
    private void viewUserDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) model.getValueAt(selectedRow, 0);
            new UserDetailFrame(userId).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur.");
        }
    }

    /**
     * Modifie le rôle de l'utilisateur (via un EditUserFrame ou équivalent).
     */
    private void modifyUserRole() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) model.getValueAt(selectedRow, 0);
            new EditUserFrame(userId, this).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur.");
        }
    }

    /**
     * Supprime l'utilisateur sélectionné.
     */
    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Confirmez-vous la suppression de cet utilisateur ?",
                    "Supprimer", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = UserDAO.deleteUser(userId);
                if (success) {
                    loadUsers();
                    JOptionPane.showMessageDialog(this, "Utilisateur supprimé.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur.");
        }
    }
}
