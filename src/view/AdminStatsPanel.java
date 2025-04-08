package view;

import javax.swing.*;
import java.awt.*;

public class AdminStatsPanel extends JPanel {

    public AdminStatsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Statistiques / Reporting", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // ex: un graphe JFreeChart, stats sur les ventes, etc.
    }
}
