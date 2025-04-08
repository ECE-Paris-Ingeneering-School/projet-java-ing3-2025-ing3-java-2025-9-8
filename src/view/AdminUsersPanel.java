package view;

import javax.swing.*;
import java.awt.*;

public class AdminUsersPanel extends JPanel {

    public AdminUsersPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Gérer les Utilisateurs", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // ex: un tableau d'utilisateurs (DAO pour lister users),
        // un formulaire pour changer le role, etc.
    }
}
