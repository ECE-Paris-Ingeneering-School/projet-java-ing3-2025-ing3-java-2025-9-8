package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public WelcomeFrame() {
        super("Bienvenue");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // 1) Ecran d'accueil
        JPanel welcomePanel = createWelcomePanel();
        // 2) Ecran de login
        JPanel loginPanel = createLoginPanel();
        // 3) Ecran register
        JPanel registerPanel = createRegisterPanel();

        contentPanel.add(welcomePanel, "WELCOME");
        contentPanel.add(loginPanel, "LOGIN");
        contentPanel.add(registerPanel, "REGISTER");

        add(contentPanel);
        setVisible(true);

        cardLayout.show(contentPanel, "WELCOME");
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridLayout(3,1,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JLabel label = new JLabel("Bienvenue sur Puppy Yoga", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(label);

        JButton loginBtn = new JButton("J'ai déjà un compte");
        loginBtn.addActionListener(e -> cardLayout.show(contentPanel, "LOGIN"));
        panel.add(loginBtn);

        JButton registerBtn = new JButton("Je n'ai pas de compte");
        registerBtn.addActionListener(e -> cardLayout.show(contentPanel, "REGISTER"));
        panel.add(registerBtn);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JLabel userLabel = new JLabel("Nom d'utilisateur:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Mot de passe:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Se connecter");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel());
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            User user = UserDAO.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    new AdminFrame(user).setVisible(true);
                } else {
                    new MainFrame(user).setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect !");
            }
        });

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(5,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JLabel userLabel = new JLabel("Nom d'utilisateur:");
        JTextField userField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel nameLabel = new JLabel("Nom complet:");
        JTextField nameField = new JTextField();
        JLabel passLabel = new JLabel("Mot de passe:");
        JPasswordField passField = new JPasswordField();

        JButton registerButton = new JButton("S'inscrire");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel());
        panel.add(registerButton);

        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String fullName = nameField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || email.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            // par défaut, role='client'. (ou 'admin' si vous avez un radio button)
            User newUser = new User(username, password, email, fullName, "client");
            boolean registered = UserDAO.registerUser(newUser);
            if (registered) {
                JOptionPane.showMessageDialog(this, "Inscription réussie !");
                cardLayout.show(contentPanel, "LOGIN");
            } else {
                // e-mail déjà utilisé
                JOptionPane.showMessageDialog(this,
                        "Cette adresse e-mail est déjà utilisée. Vous avez déjà un compte !",
                        "Inscription échouée",
                        JOptionPane.ERROR_MESSAGE);
                // redirection sur LOGIN
                cardLayout.show(contentPanel, "LOGIN");
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame());
    }
}
