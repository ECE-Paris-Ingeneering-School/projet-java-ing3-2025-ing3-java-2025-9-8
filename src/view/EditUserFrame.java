package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class EditUserFrame extends JFrame {
    private User user;
    private AdminUsersPanel parentPanel;

    public EditUserFrame(int userId, AdminUsersPanel parentPanel) {
        this.parentPanel = parentPanel;
        setTitle("Modifier l'utilisateur");
        setSize(400, 200);
        setLocationRelativeTo(null);

        user = UserDAO.getUserById(userId);
        if(user == null) {
            JOptionPane.showMessageDialog(this, "Utilisateur non trouvé.");
            dispose();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Afficher les informations non modifiables
        panel.add(new JLabel("Username :"));
        panel.add(new JLabel(user.getUsername()));
        panel.add(new JLabel("Email :"));
        panel.add(new JLabel(user.getEmail()));
        panel.add(new JLabel("Nom :"));
        panel.add(new JLabel(user.getName()));

        // Permettre de modifier le rôle
        panel.add(new JLabel("Rôle :"));
        String[] roles = {"client", "admin"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setSelectedItem(user.getRole());
        panel.add(roleCombo);

        JButton saveBtn = new JButton("Enregistrer");
        panel.add(new JLabel()); // case vide pour l'alignement
        panel.add(saveBtn);

        add(panel);

        saveBtn.addActionListener(e -> {
            user.setRole((String) roleCombo.getSelectedItem());
            boolean success = UserDAO.updateUser(user);
            if(success){
                JOptionPane.showMessageDialog(this, "Utilisateur mis à jour.");
                parentPanel.loadUsers();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
            }
        });
    }
}
