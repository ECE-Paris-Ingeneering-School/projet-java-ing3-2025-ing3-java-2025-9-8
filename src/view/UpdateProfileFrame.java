package view;

import model.User;
import javax.swing.*;
import java.awt.*;

public class UpdateProfileFrame extends JFrame {
    private User currentUser;

    public UpdateProfileFrame(User user) {
        super("Mettre à jour le profil");
        this.currentUser = user;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ...
        // A implémenter si besoin : mise à jour en BDD
        // ...
    }
}
