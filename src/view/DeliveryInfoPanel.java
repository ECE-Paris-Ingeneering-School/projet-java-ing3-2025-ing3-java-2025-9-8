package view;

import model.User;
import javax.swing.*;
import java.awt.*;

public class DeliveryInfoPanel extends JPanel {
    private JTextField nameField, surnameField, phoneField;
    private JTextField streetField, complementField, postalCodeField, cityField;
    private JComboBox<String> regionBox;
    private User currentUser;

    public DeliveryInfoPanel(User user, Runnable onNext) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Données personnelles et de livraison", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Les champs sont initialisés à partir de l'objet User (getOrDefault pour éviter les nulls)
        nameField = new JTextField(getOrDefault(currentUser.getName()));
        surnameField = new JTextField(); // Si vous stockez le prénom et le nom ensemble, gardez ce champ ou adaptez-le
        phoneField = new JTextField(getOrDefault(currentUser.getPhone()));
        streetField = new JTextField(getOrDefault(currentUser.getStreet()));
        complementField = new JTextField(getOrDefault(currentUser.getComplement()));
        postalCodeField = new JTextField(getOrDefault(currentUser.getPostalCode()));
        cityField = new JTextField(getOrDefault(currentUser.getCity()));
        regionBox = new JComboBox<>(new String[]{
                "Île-de-France", "Provence-Alpes-Côte d'Azur",
                "Auvergne-Rhône-Alpes", "Autre"
        });
        if (currentUser.getRegion() != null) {
            regionBox.setSelectedItem(currentUser.getRegion());
        }

        addField(formPanel, gbc, "Prénom *", nameField);
        addField(formPanel, gbc, "Nom *", surnameField);
        addField(formPanel, gbc, "Téléphone portable *", phoneField);
        addField(formPanel, gbc, "Voie *", streetField);
        addField(formPanel, gbc, "Complément", complementField);
        addField(formPanel, gbc, "Code postal *", postalCodeField);
        addField(formPanel, gbc, "Ville *", cityField);

        gbc.gridx = 0;
        formPanel.add(new JLabel("Région *"), gbc);
        gbc.gridx = 1;
        formPanel.add(regionBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(Color.WHITE);
        JButton nextButton = new JButton("Continuer");
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        nextButton.addActionListener(e -> {
            if (isFormValid()) {
                // Mise à jour de l'objet User avec les données saisies
                currentUser.setName(nameField.getText().trim());
                currentUser.setPhone(phoneField.getText().trim());
                currentUser.setStreet(streetField.getText().trim());
                currentUser.setComplement(complementField.getText().trim());
                currentUser.setPostalCode(postalCodeField.getText().trim());
                currentUser.setCity(cityField.getText().trim());
                currentUser.setRegion((String) regionBox.getSelectedItem());
                onNext.run();
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.");
            }
        });
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    public boolean isFormValid() {
        return !nameField.getText().isEmpty()
                && !surnameField.getText().isEmpty()
                && !phoneField.getText().isEmpty()
                && !streetField.getText().isEmpty()
                && !postalCodeField.getText().isEmpty()
                && !cityField.getText().isEmpty();
    }

    private String getOrDefault(String s) {
        return (s == null) ? "" : s;
    }
}
