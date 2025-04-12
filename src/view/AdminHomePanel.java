package view;

import dao.OrderDAO;
import dao.SessionDAO;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class AdminHomePanel extends JPanel {
    private User currentUser;

    public AdminHomePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        // Couleur de fond légèrement rosée / beige (par exemple)
        setBackground(new Color(255, 248, 240));

        // -- Titre en haut --
        JLabel titleLabel = new JLabel("Bienvenue sur l'accueil Admin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // -- Panneau central (BoxLayout Y_AXIS) pour le contenu principal --
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false); // pour laisser voir la couleur de fond du parent
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Calcul des stats
        int totalUsers = UserDAO.getAllUsers().size();
        int totalSessions = SessionDAO.getAllSessions().size();
        double totalRevenue = OrderDAO.getTotalRevenue(); // Assure-toi que OrderDAO existe

        // Ajout d'un label de "connecté en tant que"
        JLabel adminNameLabel = new JLabel("Connecté en tant que : " + currentUser.getUsername(), SwingConstants.CENTER);
        adminNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        adminNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(adminNameLabel);
        centerPanel.add(Box.createVerticalStrut(15));

        // Un label plus chaleureux
        JLabel welcomeMsg = new JLabel("<html><div style='text-align:center;'>"
                + "Ravi de vous revoir&nbsp;!<br>"
                + "Voici un aperçu rapide de l’activité&nbsp;:</div></html>", SwingConstants.CENTER);
        welcomeMsg.setFont(new Font("SansSerif", Font.ITALIC, 16));
        welcomeMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(welcomeMsg);
        centerPanel.add(Box.createVerticalStrut(20));

        // Infos : nombre d’utilisateurs
        JLabel userCountLabel = new JLabel("Nombre total d’utilisateurs : " + totalUsers, SwingConstants.CENTER);
        userCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(userCountLabel);
        centerPanel.add(Box.createVerticalStrut(5));

        // Infos : nombre de sessions
        JLabel sessionsCountLabel = new JLabel("Nombre de sessions yoga planifiées : " + totalSessions, SwingConstants.CENTER);
        sessionsCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sessionsCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(sessionsCountLabel);
        centerPanel.add(Box.createVerticalStrut(5));

        // Infos : chiffre d’affaires
        JLabel revenueLabel = new JLabel("Chiffre d’affaires total : " + String.format("%.2f", totalRevenue) + " €", SwingConstants.CENTER);
        revenueLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        revenueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(revenueLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Petit texte informatif
        JLabel infoLabel = new JLabel("<html><div style='text-align:center;'>"
                + "N'hésitez pas à consulter régulièrement l'inventaire et les statistiques&nbsp;!<br>"
                + "Vous pouvez également gérer les utilisateurs et surveiller leurs réservations.</div></html>",
                SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(infoLabel);

        // On ajoute le panel au center (et on va le centrer verticalement)
        add(centerPanel, BorderLayout.CENTER);

        // -- On peut ajouter un glue en haut et en bas pour centrer verticalement --
        // Mais un simple BoxLayout + la dimension de la fenêtre fera l’affaire.
    }
}
