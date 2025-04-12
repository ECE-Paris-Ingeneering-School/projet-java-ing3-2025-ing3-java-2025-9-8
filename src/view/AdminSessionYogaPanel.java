package view;

import dao.SessionDAO;
import model.Session;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminSessionYogaPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public AdminSessionYogaPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240)); // Fond pastel clair

        // ---- Titre ----
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 236, 227));
        JLabel titleLabel = new JLabel("Gestion des Sessions Yoga");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // ---- Tableau ----
        String[] columns = {"ID", "Date", "Heure", "Niveau", "Capacité", "Professeur", "Race", "Lieu"};
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

        // Pour centrer le texte dans toutes les cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        // Activez AUTO_RESIZE_ALL_COLUMNS pour que le tableau s'adapte à la largeur du scrollpane
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(255, 250, 240));
        scrollPane.setPreferredSize(new Dimension(1000, 350));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerPanel.setBackground(new Color(255, 250, 240));
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // ---- Panneau de Boutons ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 236, 227));

        JButton addButton = new JButton("Ajouter Session");
        JButton editButton = new JButton("Modifier Session");
        JButton deleteButton = new JButton("Supprimer Session");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadSessions();

        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        setColumnWidths();

        addButton.addActionListener(e -> new SessionForm(null, this).setVisible(true));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int sessionId = (int) model.getValueAt(selectedRow, 0);
                new SessionForm(sessionId, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une session à modifier.");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int sessionId = (int) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Confirmez-vous la suppression de cette session ?",
                        "Supprimer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = SessionDAO.deleteSession(sessionId);
                    if (success) {
                        loadSessions();
                        JOptionPane.showMessageDialog(this, "Session supprimée avec succès.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une session à supprimer.");
            }
        });
    }

    public void loadSessions() {
        model.setRowCount(0);
        List<Session> sessions = SessionDAO.getAllSessions();
        for (Session s : sessions) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getSessionDate(),
                    s.getSessionTime(),
                    s.getLevel(),
                    s.getCapacity(),
                    s.getTeacherName(),
                    s.getBreedName(),
                    s.getLocationName()
            });
        }
    }

    private void setColumnWidths() {
        int[] widths = {60, 120, 120, 100, 100, 180, 180, 180};
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
