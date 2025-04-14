package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class PaymentInfoPanel extends JPanel {
    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField monthField;
    private JTextField yearField;
    private JTextField cvvField;

    public PaymentInfoPanel(User user, Runnable onNext) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Données de carte", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cardNumberField = new JTextField(getOrDefault(user.getCardNumber()), 20);
        cardHolderField = new JTextField(getOrDefault(user.getCardName()), 20);
        monthField = new JTextField(getOrDefault(user.getCardMonth()), 5);
        yearField = new JTextField(getOrDefault(user.getCardYear()), 5);
        cvvField = new JTextField(getOrDefault(user.getCardCvv()), 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Numéro de carte *"), gbc);
        gbc.gridx = 1;
        formPanel.add(cardNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Titulaire de la carte *"), gbc);
        gbc.gridx = 1;
        formPanel.add(cardHolderField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Mois d'expiration *"), gbc);
        gbc.gridx = 1;
        formPanel.add(monthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Année d'expiration *"), gbc);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("CVV *"), gbc);
        gbc.gridx = 1;
        formPanel.add(cvvField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(Color.WHITE);
        JButton nextButton = new JButton("Aller au récapitulatif");
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        nextButton.addActionListener(e -> {
            if (validateFields()) {
                // Mise à jour de l'objet User avec les infos saisies
                user.setCardNumber(cardNumberField.getText().trim());
                user.setCardName(cardHolderField.getText().trim());
                user.setCardMonth(monthField.getText().trim());
                user.setCardYear(yearField.getText().trim());
                user.setCardCvv(cvvField.getText().trim());
                // Mettez à jour la base de données, si nécessaire
                UserDAO.updateUserDeliveryAndPayment(user);
                onNext.run();
                // Fermer la fenêtre parente du PaymentInfoPanel
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    window.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.");
            }
        });
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateFields() {
        return !cardNumberField.getText().isEmpty()
                && !cardHolderField.getText().isEmpty()
                && !monthField.getText().isEmpty()
                && !yearField.getText().isEmpty()
                && !cvvField.getText().isEmpty();
    }

    private String getOrDefault(String value) {
        return value != null ? value : "";
    }
}
