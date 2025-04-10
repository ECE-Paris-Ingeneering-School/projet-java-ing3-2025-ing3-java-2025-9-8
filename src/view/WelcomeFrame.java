package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public WelcomeFrame() {
        super("Bienvenue chez Puppy Yoga");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel welcomePanel = createWelcomePanel();
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();

        contentPanel.add(welcomePanel, "WELCOME");
        contentPanel.add(loginPanel, "LOGIN");
        contentPanel.add(registerPanel, "REGISTER");

        add(contentPanel);
        setVisible(true);
        cardLayout.show(contentPanel, "WELCOME");
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 248, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JLabel title = new JLabel("BIENVENUE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Connectez-vous avec votre adresse ou créez un compte", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton loginBtn = new JButton("Se connecter");
        stylizeDarkButton(loginBtn);
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> cardLayout.show(contentPanel, "LOGIN"));

        JButton registerBtn = new JButton("Créer un compte");
        stylizeBorderButton(registerBtn);
        registerBtn.setAlignmentX(CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> cardLayout.show(contentPanel, "REGISTER"));

        panel.add(title);
        panel.add(subtitle);
        panel.add(loginBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(registerBtn);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = createCenteredPanel();

        JLabel title = new JLabel("Connexion", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JLabel userLabel = new JLabel("Nom d'utilisateur :");
        JLabel passLabel = new JLabel("Mot de passe :");
        stylizeLabel(userLabel);
        stylizeLabel(passLabel);

        JCheckBox showPassword = new JCheckBox("Afficher le mot de passe");
        showPassword.setBackground(new Color(255, 248, 230));
        showPassword.setAlignmentX(CENTER_ALIGNMENT);
        showPassword.addActionListener(e -> passField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•'));

        JButton loginButton = new JButton("Se connecter");
        stylizeDarkButton(loginButton);
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            User user = UserDAO.login(username, password);
            if (user != null) {
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

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(showPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(loginButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = createCenteredPanel();

        JLabel title = new JLabel("Créer un compte", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JTextField userField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField nameField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JLabel userLabel = new JLabel("Nom d'utilisateur :");
        JLabel emailLabel = new JLabel("Email :");
        JLabel nameLabel = new JLabel("Nom complet :");
        JLabel passLabel = new JLabel("Mot de passe :");
        stylizeLabel(userLabel);
        stylizeLabel(emailLabel);
        stylizeLabel(nameLabel);
        stylizeLabel(passLabel);

        JButton registerButton = new JButton("S'inscrire");
        stylizeDarkButton(registerButton);
        registerButton.setAlignmentX(CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String fullName = nameField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || email.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            User newUser = new User(username, password, email, fullName, "client");
            boolean registered = UserDAO.registerUser(newUser);
            if (registered) {
                User user = UserDAO.login(username, password);
                if (user != null) {
                    new MainFrame(user).setVisible(true);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cette adresse e-mail est déjà utilisée.",
                        "Inscription échouée",
                        JOptionPane.ERROR_MESSAGE);
                cardLayout.show(contentPanel, "LOGIN");
            }
        });

        panel.add(title);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(registerButton);

        return panel;
    }

    private JPanel createCenteredPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 248, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        return panel;
    }

    private void stylizeButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(new Color(242, 224, 200));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void stylizeDarkButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void stylizeBorderButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button.setMaximumSize(new Dimension(200, 40));
    }

    private void stylizeLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame());
    }
}