package view;

import model.User;
import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public AdminFrame(User user) {
        super("Puppy Yoga - Administration");
        this.currentUser = user;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Initialiser d'abord le cardLayout et contentPanel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Ajouter les différents panneaux dans le CardLayout
        contentPanel.add(new AdminHomePanel(user), "HOME");
        contentPanel.add(new AdminInventoryPanel(), "INVENTORY");  // Gère Boutique & Session Yoga
        contentPanel.add(new AdminUsersPanel(), "USERS");
        contentPanel.add(new AdminStatsPanel(), "STATS");

        // Créer le header avec le menu
        JPanel headerPanel = createHeaderPanel();

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(242, 224, 200));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Nous ne mettons plus le nom dans le coin gauche ici.
        // Menu central
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        menuPanel.setBackground(new Color(242, 224, 200));
        String[] menuItems = {"Accueil Admin", "Gestion de l'inventaire", "Gérer Utilisateurs", "Statistiques"};
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
            btn.setBackground(new Color(242, 224, 200));
            btn.setBorder(null);
            btn.addActionListener(e -> {
                String cardId;
                switch (item) {
                    case "Accueil Admin":
                        cardId = "HOME";
                        break;
                    case "Gestion de l'inventaire":
                        cardId = "INVENTORY";
                        break;
                    case "Gérer Utilisateurs":
                        cardId = "USERS";
                        break;
                    case "Statistiques":
                        cardId = "STATS";
                        break;
                    default:
                        cardId = "HOME";
                }
                cardLayout.show(contentPanel, cardId);
            });
            menuPanel.add(btn);
        }
        header.add(menuPanel, BorderLayout.CENTER);

        // Bouton déconnexion à droite
        JButton logoutButton = new JButton("Déconnexion");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        logoutButton.setBackground(new Color(242, 224, 200));
        logoutButton.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        header.add(logoutButton, BorderLayout.EAST);

        return header;
    }
}
