package view;

import javax.swing.*;
import java.awt.*;

public class AdminSessionsPanel extends JPanel {

    public AdminSessionsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Gérer les Sessions", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Ici, vous pouvez afficher la liste des séances, ajouter/supprimer, etc.
        // ex: un JTable pour "Session", avec un CRUD
    }
}
