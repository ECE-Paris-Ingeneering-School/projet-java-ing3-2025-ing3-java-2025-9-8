package view;

import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre d'administration avec la même couleur de header que MainFrame (242,224,200).
 */
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

        JPanel headerPanel = createHeaderPanel();
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Par exemple, on ajoute des panneaux d'admin
        contentPanel.add(new AdminHomePanel(user), "HOME");
        contentPanel.add(new AdminSessionsPanel(), "SESSIONS");
        contentPanel.add(new AdminUsersPanel(), "USERS");
        contentPanel.add(new AdminStatsPanel(), "STATS");

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        // Utilise la même couleur que le MainFrame client : (242,224,200)
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(242,224,200));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Admin info gauche
        JLabel adminLabel = new JLabel("Admin : " + currentUser.getUsername(), SwingConstants.LEFT);
        adminLabel.setFont(new Font("Serif", Font.BOLD, 24));
        header.add(adminLabel, BorderLayout.WEST);

        // Menu central
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        menuPanel.setBackground(new Color(242,224,200));

        String[] menuItems = {"Accueil Admin", "Gérer Sessions", "Gérer Utilisateurs", "Statistiques"};
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
            btn.setBackground(new Color(242,224,200));
            btn.setBorder(null);

            btn.addActionListener(e -> {
                String cardId;
                switch (item) {
                    case "Accueil Admin": cardId = "HOME"; break;
                    case "Gérer Sessions": cardId = "SESSIONS"; break;
                    case "Gérer Utilisateurs": cardId = "USERS"; break;
                    case "Statistiques": cardId = "STATS"; break;
                    default: cardId = "HOME";
                }
                cardLayout.show(contentPanel, cardId);
            });
            menuPanel.add(btn);
        }
        header.add(menuPanel, BorderLayout.CENTER);

        // Déconnexion
        JButton logoutButton = new JButton("Déconnexion");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        logoutButton.setBackground(new Color(242,224,200));
        logoutButton.addActionListener(e -> {
            // Retour vers le WelcomeFrame
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        header.add(logoutButton, BorderLayout.EAST);

        return header;
    }
}
