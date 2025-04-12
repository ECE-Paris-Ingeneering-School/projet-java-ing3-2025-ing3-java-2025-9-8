package view;

import dao.DiscountDAO;
import model.Discount;

import javax.swing.*;
import java.awt.*;

public class DiscountForm extends JFrame {
    private JTextField nameField;
    private JTextField descField;
    private JTextField condField;
    private JTextField valueField;

    private JButton saveButton;

    private Discount currentDiscount;
    private AdminDiscountPanel parentPanel;

    public DiscountForm(Integer discountId, AdminDiscountPanel parentPanel) {
        this.parentPanel = parentPanel;
        if (discountId != null) {
            currentDiscount = DiscountDAO.getDiscountById(discountId);
        }
        setTitle(currentDiscount != null ? "Modifier l'Offre" : "Ajouter une Offre");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Nom de l'offre :"));
        nameField = new JTextField(currentDiscount != null ? currentDiscount.getName() : "");
        formPanel.add(nameField);

        formPanel.add(new JLabel("Description :"));
        descField = new JTextField(currentDiscount != null ? currentDiscount.getDescription() : "");
        formPanel.add(descField);

        formPanel.add(new JLabel("Conditions :"));
        condField = new JTextField(currentDiscount != null ? currentDiscount.getConditions() : "");
        formPanel.add(condField);

        formPanel.add(new JLabel("Remise :"));
        valueField = new JTextField(currentDiscount != null ? currentDiscount.getValue() : "");
        formPanel.add(valueField);

        saveButton = new JButton(currentDiscount != null ? "Modifier" : "Ajouter");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveDiscount());
    }

    private void saveDiscount() {
        String name = nameField.getText().trim();
        String description = descField.getText().trim();
        String conditions = condField.getText().trim();
        String value = valueField.getText().trim();

        // Contrôles basiques
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom de l'offre est requis.");
            return;
        }

        // Ajouter ou modifier ?
        if (currentDiscount == null) {
            // Ajout
            Discount newD = new Discount(0, name, description, conditions, value);
            boolean ok = DiscountDAO.insertDiscount(newD);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Offre ajoutée avec succès.");
                parentPanel.loadDiscounts(); // rafraîchit le tableau
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'offre.");
            }
        } else {
            // Modification
            currentDiscount.setName(name);
            currentDiscount.setDescription(description);
            currentDiscount.setConditions(conditions);
            currentDiscount.setValue(value);

            boolean ok = DiscountDAO.updateDiscount(currentDiscount);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Offre modifiée avec succès.");
                parentPanel.loadDiscounts();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'offre.");
            }
        }
    }
}
