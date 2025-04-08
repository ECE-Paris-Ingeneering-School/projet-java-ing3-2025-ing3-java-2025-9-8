package view;

import dao.ProfileDAO;
import model.User;
import model.SessionReservation;
import model.OrderInfoSimple;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProfilePanel extends JPanel {
    private User currentUser;

    public ProfilePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Haut de la page : Titre
        JLabel title = new JLabel("Mon Profil", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        // Centre : un panel pour les infos + un JTabbedPane
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Infos client (nom, prenom, email, mdp)
        centerPanel.add(new JLabel("Nom d'utilisateur : " + user.getUsername()));
        centerPanel.add(new JLabel("Mot de passe : " + user.getPassword()));
        centerPanel.add(new JLabel("Email : " + user.getEmail()));
        centerPanel.add(new JLabel("Nom complet : " + user.getName()));
        centerPanel.add(Box.createVerticalStrut(20));

        // Onglets : Mes reservations, Mes factures
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mes réservations", createReservationsPanel());
        tabbedPane.addTab("Mes factures", createOrdersPanel());

        centerPanel.add(tabbedPane);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        List<SessionReservation> reservations = dao.ProfileDAO.getUserReservations(currentUser.getId());

        String[] colNames = {"ID Réservation", "Date réservation", "Session date", "Session time", "Niveau", "Lieu"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(model);

        for (SessionReservation r : reservations) {
            Object[] rowData = {
                    r.getReservationId(),
                    r.getReservationDate(),
                    r.getSessionDate(),
                    r.getSessionTime(),
                    r.getLevel(),
                    r.getLocationName()
            };
            model.addRow(rowData);
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        List<OrderInfoSimple> orders = dao.ProfileDAO.getUserOrders(currentUser.getId());

        String[] colNames = {"ID Commande", "Date commande", "Total (€)"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(model);

        for (OrderInfoSimple o : orders) {
            Object[] rowData = {
                    o.getOrderId(),
                    o.getOrderDate(),
                    o.getTotal()
            };
            model.addRow(rowData);
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
