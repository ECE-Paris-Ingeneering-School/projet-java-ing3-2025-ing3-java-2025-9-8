package view;

import dao.DiscountDAO;
import dao.ProductDAO;
import dao.SessionDAO;
import model.Discount;
import model.Product;
import model.Session;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class DiscountForm extends JFrame {

    private JTextField nameField;
    private JTextField descField;
    private JSpinner minQuantitySpinner;
    private JComboBox<String> discountTypeCombo;   // "gratuit" ou "pourcentage"
    private JSpinner discountAmountSpinner;
    private JComboBox<String> targetCategoryCombo;   // "Produit" ou "Session"
    private JComboBox<String> targetItemCombo;         // Liste d'articles ou sessions
    private JTextField startDateField;
    private JTextField endDateField;

    private Discount currentDiscount;
    private AdminDiscountPanel parentPanel;

    public DiscountForm(Integer discountId, AdminDiscountPanel parentPanel) {
        this.parentPanel = parentPanel;
        if (discountId != null) {
            currentDiscount = DiscountDAO.getDiscountById(discountId);
        }
        setTitle(currentDiscount != null ? "Modifier l'Offre" : "Ajouter une Offre");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Nom de la promo
        formPanel.add(new JLabel("Nom de l'offre :"));
        nameField = new JTextField(currentDiscount != null ? currentDiscount.getName() : "");
        formPanel.add(nameField);

        // Description
        formPanel.add(new JLabel("Description :"));
        descField = new JTextField(currentDiscount != null ? currentDiscount.getDescription() : "");
        formPanel.add(descField);

        // Nombre minimum d'articles
        formPanel.add(new JLabel("Nombre min d'articles :"));
        minQuantitySpinner = new JSpinner(new SpinnerNumberModel(currentDiscount != null ? currentDiscount.getMinQuantity() : 3, 1, 1000, 1));
        formPanel.add(minQuantitySpinner);

        // Type de réduction
        formPanel.add(new JLabel("Type de réduction :"));
        discountTypeCombo = new JComboBox<>(new String[]{"gratuit", "pourcentage"});
        if (currentDiscount != null) {
            discountTypeCombo.setSelectedItem(currentDiscount.getDiscountType());
        }
        formPanel.add(discountTypeCombo);

        // Montant de la réduction
        formPanel.add(new JLabel("Montant de la réduction :"));
        discountAmountSpinner = new JSpinner(new SpinnerNumberModel(currentDiscount != null ? currentDiscount.getDiscountAmount() : 1.0, 0.0, 1000.0, 1.0));
        formPanel.add(discountAmountSpinner);

        // Catégorie cible
        formPanel.add(new JLabel("Catégorie cible :"));
        targetCategoryCombo = new JComboBox<>(new String[]{"Produit", "Session"});
        if (currentDiscount != null) {
            targetCategoryCombo.setSelectedItem(currentDiscount.getTargetCategory());
        }
        formPanel.add(targetCategoryCombo);

        // Article cible (dépendant de la catégorie)
        formPanel.add(new JLabel("Article cible :"));
        targetItemCombo = new JComboBox<>();
        loadTargetItems();
        formPanel.add(targetItemCombo);

        // Date de début
        formPanel.add(new JLabel("Début (yyyy-mm-dd) :"));
        startDateField = new JTextField(currentDiscount != null && currentDiscount.getStartDate() != null ? currentDiscount.getStartDate().toString() : "");
        formPanel.add(startDateField);

        // Date de fin
        formPanel.add(new JLabel("Fin (yyyy-mm-dd) :"));
        endDateField = new JTextField(currentDiscount != null && currentDiscount.getEndDate() != null ? currentDiscount.getEndDate().toString() : "");
        formPanel.add(endDateField);

        JButton saveButton = new JButton(currentDiscount != null ? "Modifier" : "Ajouter");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        targetCategoryCombo.addActionListener(e -> loadTargetItems());

        saveButton.addActionListener(e -> saveDiscount());
    }

    private void loadTargetItems() {
        targetItemCombo.removeAllItems();
        String category = (String) targetCategoryCombo.getSelectedItem();
        if ("Produit".equalsIgnoreCase(category)) {
            List<Product> products = ProductDAO.getAllProducts();
            for (Product p : products) {
                // Exemple: "Produit #101 - Chaussettes de yoga"
                targetItemCombo.addItem("Produit #" + p.getId() + " - " + p.getName());
            }
        } else {
            List<Session> sessions = SessionDAO.getAllSessions();
            for (Session s : sessions) {
                targetItemCombo.addItem("Session #" + s.getId() + " - " + s.getLevel());
            }
        }
    }

    private void saveDiscount() {
        String name = nameField.getText().trim();
        String description = descField.getText().trim();
        int minQ = (Integer) minQuantitySpinner.getValue();
        String type = (String) discountTypeCombo.getSelectedItem();
        double amount = (Double) discountAmountSpinner.getValue();
        String category = (String) targetCategoryCombo.getSelectedItem();

        // Extraction de l'ID depuis la chaîne (ex: "Produit #101 - Chaussettes de yoga")
        int targetId = 0;
        String sel = (String) targetItemCombo.getSelectedItem();
        if (sel != null && sel.contains("#")) {
            String[] parts = sel.split("#");
            if (parts.length > 1) {
                String[] sub = parts[1].trim().split(" ");
                try {
                    targetId = Integer.parseInt(sub[0]);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }

        String startStr = startDateField.getText().trim();
        String endStr = endDateField.getText().trim();
        if (name.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires (Nom et Dates).");
            return;
        }

        Date startDate, endDate;
        try {
            startDate = Date.valueOf(startStr);
            endDate = Date.valueOf(endStr);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Les dates doivent être au format yyyy-mm-dd.");
            return;
        }

        if (currentDiscount == null) {
            Discount newD = new Discount(0, name, description, minQ, type, amount, category, targetId, startDate, endDate);
            if (DiscountDAO.insertDiscount(newD)) {
                JOptionPane.showMessageDialog(this, "Promotion ajoutée !");
                parentPanel.loadDiscounts();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la promotion.");
            }
        } else {
            currentDiscount.setName(name);
            currentDiscount.setDescription(description);
            currentDiscount.setMinQuantity(minQ);
            currentDiscount.setDiscountType(type);
            currentDiscount.setDiscountAmount(amount);
            currentDiscount.setTargetCategory(category);
            currentDiscount.setTargetId(targetId);
            currentDiscount.setStartDate(startDate);
            currentDiscount.setEndDate(endDate);
            if (DiscountDAO.updateDiscount(currentDiscount)) {
                JOptionPane.showMessageDialog(this, "Promotion modifiée !");
                parentPanel.loadDiscounts();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la promotion.");
            }
        }
    }
}
