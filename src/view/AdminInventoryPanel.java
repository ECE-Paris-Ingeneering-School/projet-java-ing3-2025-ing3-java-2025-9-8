package view;

import javax.swing.*;
import java.awt.*;

public class AdminInventoryPanel extends JPanel {

    public AdminInventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240)); // Fond pastel clair

        // Entête du panneau
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 236, 227));
        JLabel titleLabel = new JLabel("Gestion de l'inventaire", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Création du JTabbedPane avec trois onglets
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Boutique", new AdminBoutiquePanel());
        tabbedPane.addTab("Session Yoga", new AdminSessionYogaPanel());
        tabbedPane.addTab("Offres", new AdminDiscountPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
