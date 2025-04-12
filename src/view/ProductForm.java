package view;

import dao.ProductDAO;
import model.Product;
import javax.swing.*;
import java.awt.*;

public class ProductForm extends JFrame {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField imagePathField;
    private JButton saveButton;

    private Product product; // Sera null pour un ajout, non-null pour modification
    private AdminBoutiquePanel parentPanel;

    public ProductForm(Integer productId, AdminBoutiquePanel parentPanel) {
        this.parentPanel = parentPanel;
        if (productId != null) {
            product = ProductDAO.getProductById(productId);
        }
        setTitle(product != null ? "Modifier Produit" : "Ajouter Produit");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Nom :"));
        nameField = new JTextField(product != null ? product.getName() : "");
        formPanel.add(nameField);

        formPanel.add(new JLabel("Prix (€) :"));
        priceField = new JTextField(product != null ? String.valueOf(product.getPrice()) : "");
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stock :"));
        stockField = new JTextField(product != null ? String.valueOf(product.getStock()) : "");
        formPanel.add(stockField);

        formPanel.add(new JLabel("Chemin Image :"));
        imagePathField = new JTextField(product != null ? product.getImagePath() : "");
        formPanel.add(imagePathField);

        saveButton = new JButton(product != null ? "Modifier" : "Ajouter");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveProduct());
    }

    private void saveProduct() {
        String name = nameField.getText().trim();
        double price;
        int stock;
        try {
            price = Double.parseDouble(priceField.getText().trim());
            stock = Integer.parseInt(stockField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prix ou stock invalide.");
            return;
        }
        String imagePath = imagePathField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom du produit est requis.");
            return;
        }

        if (product == null) {
            Product newProd = new Product(0, name, price, imagePath, stock);
            boolean ok = ProductDAO.insertProduct(newProd);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Produit ajouté.");
                parentPanel.loadProducts();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
            }
        } else {
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setImagePath(imagePath);
            boolean ok = ProductDAO.updateProduct(product);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Produit modifié.");
                parentPanel.loadProducts();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
            }
        }
    }
}
