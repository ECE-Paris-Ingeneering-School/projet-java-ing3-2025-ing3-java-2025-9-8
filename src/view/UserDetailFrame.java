package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class UserDetailFrame extends JFrame {

    public UserDetailFrame(int userId) {
        setTitle("Détails de l'utilisateur");
        setSize(400, 300);
        setLocationRelativeTo(null);

        User user = UserDAO.getUserById(userId); // Assurez-vous que cette méthode existe

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (user != null) {
            panel.add(new JLabel("ID : " + user.getId()));
            panel.add(new JLabel("Username : " + user.getUsername()));
            panel.add(new JLabel("Email : " + user.getEmail()));
            panel.add(new JLabel("Nom : " + user.getName()));
            panel.add(new JLabel("Rôle : " + user.getRole()));
            panel.add(new JLabel("Mot de passe : " + user.getPassword())); // À utiliser avec précaution
        } else {
            panel.add(new JLabel("Utilisateur non trouvé."));
        }

        add(panel);
    }
}
