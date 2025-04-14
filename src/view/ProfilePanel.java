package view;

import dao.ProfileDAO;
import model.OrderInfoSimple;
import model.SessionReservation;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProfilePanel extends JPanel {
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel infosPanel;
    private JPanel paiementPanel;
    // Tables pour "Mes achats" et "Mes factures"
    private JTable ordersTable;
    private JTable reservationsTable;

    public ProfilePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Mon Profil", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Menu avec boutons
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        menuPanel.setBackground(Color.WHITE);

        JButton achatsBtn = new JButton("Mes achats");
        JButton infosBtn = new JButton("Données personnelles");
        JButton paiementBtn = new JButton("Moyens de paiement");
        JButton refreshBtn = new JButton("Actualiser");
        JButton logoutBtn = new JButton("Se déconnecter");

        achatsBtn.setFocusPainted(false);
        infosBtn.setFocusPainted(false);
        paiementBtn.setFocusPainted(false);
        refreshBtn.setFocusPainted(false);
        logoutBtn.setFocusPainted(false);

        menuPanel.add(achatsBtn);
        menuPanel.add(infosBtn);
        menuPanel.add(paiementBtn);
        menuPanel.add(refreshBtn);
        menuPanel.add(logoutBtn);
        add(menuPanel, BorderLayout.CENTER);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createAchatsPanel(), "ACHATS");
        contentPanel.add(createInfosPanel(), "INFOS");
        contentPanel.add(createPaiementPanel(), "PAIEMENT");

        add(contentPanel, BorderLayout.SOUTH);

        achatsBtn.addActionListener(e -> cardLayout.show(contentPanel, "ACHATS"));
        infosBtn.addActionListener(e -> {
            refreshInfosPanel();
            cardLayout.show(contentPanel, "INFOS");
        });
        paiementBtn.addActionListener(e -> {
            refreshPaiementPanel();
            cardLayout.show(contentPanel, "PAIEMENT");
        });
        refreshBtn.addActionListener(e -> refreshPanels());
        logoutBtn.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new WelcomeFrame();
        });

        cardLayout.show(contentPanel, "ACHATS");
    }

    private JPanel createAchatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        // Onglet réservations (Mes achats)
        tabs.addTab("Mes réservations", createReservationsPanel());
        // Onglet commandes (Mes factures)
        tabs.addTab("Mes factures", createOrdersPanel());
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        List<SessionReservation> reservations = ProfileDAO.getUserReservations(currentUser.getId());
        String[] colNames = {"ID Réservation", "Date réservation", "Session date", "Session time", "Niveau", "Lieu"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        reservationsTable = new JTable(model);
        for (SessionReservation r : reservations) {
            model.addRow(new Object[]{
                    r.getReservationId(),
                    r.getReservationDate(),
                    r.getSessionDate(),
                    r.getSessionTime(),
                    r.getLevel(),
                    r.getLocationName()
            });
        }
        panel.add(new JScrollPane(reservationsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        List<OrderInfoSimple> orders = ProfileDAO.getUserOrders(currentUser.getId());
        String[] colNames = {"ID Commande", "Date commande", "Total (€)"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        ordersTable = new JTable(model);
        for (OrderInfoSimple o : orders) {
            model.addRow(new Object[]{
                    o.getOrderId(),
                    o.getOrderDate(),
                    o.getTotal()
            });
        }
        panel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInfosPanel() {
        infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.setBackground(Color.WHITE);
        infosPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        refreshInfosPanel();
        return infosPanel;
    }

    public void refreshInfosPanel() {
        if (infosPanel != null) {
            infosPanel.removeAll();
            infosPanel.add(new JLabel("Nom complet : " + currentUser.getName()));
            infosPanel.add(Box.createVerticalStrut(10));
            infosPanel.add(new JLabel("Email : " + currentUser.getEmail()));
            infosPanel.add(Box.createVerticalStrut(10));
            infosPanel.add(new JLabel("Téléphone : " + currentUser.getPhone()));
            infosPanel.add(Box.createVerticalStrut(10));
            infosPanel.add(new JLabel("Adresse : " + currentUser.getStreet()));
            infosPanel.add(Box.createVerticalStrut(10));
            infosPanel.add(new JLabel("Complément : " + currentUser.getComplement()));
            infosPanel.add(Box.createVerticalStrut(10));
            infosPanel.add(new JLabel("Code postal : " + currentUser.getPostalCode()));
            infosPanel.add(Box.createVerticalStrut(10));
            infosPanel.add(new JLabel("Ville : " + currentUser.getCity()));
            infosPanel.add(Box.createVerticalStrut(10));
            infosPanel.add(new JLabel("Région : " + currentUser.getRegion()));
            infosPanel.revalidate();
            infosPanel.repaint();
        }
    }

    private JPanel createPaiementPanel() {
        paiementPanel = new JPanel();
        paiementPanel.setLayout(new BoxLayout(paiementPanel, BoxLayout.Y_AXIS));
        paiementPanel.setBackground(Color.WHITE);
        paiementPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        refreshPaiementPanel();
        return paiementPanel;
    }

    public void refreshPaiementPanel() {
        if (paiementPanel != null) {
            paiementPanel.removeAll();
            String cardNumber = currentUser.getCardNumber();
            String masked = (cardNumber != null && cardNumber.length() >= 4)
                    ? "**** **** **** " + cardNumber.substring(cardNumber.length() - 4)
                    : "Non renseigné";
            paiementPanel.add(new JLabel("Carte enregistrée : " + masked));
            paiementPanel.add(Box.createVerticalStrut(10));
            paiementPanel.add(new JLabel("Expiration : " + currentUser.getCardMonth() + "/" + currentUser.getCardYear()));
            paiementPanel.add(Box.createVerticalStrut(10));
            paiementPanel.add(new JLabel("Titulaire : " + currentUser.getCardName()));
            paiementPanel.revalidate();
            paiementPanel.repaint();
        }
    }

    public void refreshPanels() {
        contentPanel.removeAll();
        contentPanel.add(createAchatsPanel(), "ACHATS");
        contentPanel.add(createInfosPanel(), "INFOS");
        contentPanel.add(createPaiementPanel(), "PAIEMENT");
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
