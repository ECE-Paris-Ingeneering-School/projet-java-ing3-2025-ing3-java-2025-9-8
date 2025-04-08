package view;

import model.User;
import javax.swing.*;
import java.awt.*;

public class AdminHomePanel extends JPanel {
    private User currentUser;

    public AdminHomePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel label = new JLabel("Bienvenue sur l'accueil Admin", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        // Vous pouvez ajouter des infos sur l'admin, stats globales, etc.
    }
}
