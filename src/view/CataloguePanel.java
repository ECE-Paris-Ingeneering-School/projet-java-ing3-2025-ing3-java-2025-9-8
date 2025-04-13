package view;

import dao.TicketDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * CataloguePanel affiche plusieurs produits en grille et permet
 * à l'utilisateur d'ajouter un produit au panier en choisissant la quantité.
 * Les produits sont identifiés avec des IDs (ex. 101,102,...) correspondant à la table Product.
 */
public class CataloguePanel extends JPanel {
    private User currentUser;

    public CataloguePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Catalogue", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        JPanel productsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Utiliser des IDs cohérents avec votre base de données (ex. 101 pour "Chaussettes de yoga", 102 pour "Poids", etc.)
        productsPanel.add(createProductCard("Chaussettes de yoga", 12.0, "photos/chaussettes.png", 101));
        productsPanel.add(createProductCard("Poids", 15.0, "photos/poids.png", 102));
        productsPanel.add(createProductCard("Tapis de yoga", 30.0, "photos/tapis.png", 103));
        productsPanel.add(createProductCard("Barres de céréales", 5.0, "photos/barres.png", 104));
        productsPanel.add(createProductCard("Bouteilles énergisantes", 8.0, "photos/bouteilles.png", 105));
        productsPanel.add(createProductCard("Brassières de sport", 25.0, "photos/brassieres.png", 106));
        productsPanel.add(createProductCard("Ensemble de sport", 45.0, "photos/ensemble.png", 107));
        productsPanel.add(createProductCard("Serviettes", 10.0, "photos/serviettes.png", 108));

        add(productsPanel, BorderLayout.CENTER);
    }

    /**
     * Crée une carte produit avec image, nom, prix et un bouton pour choisir la quantité à ajouter au panier.
     */
    private JPanel createProductCard(String productName, double price, String imagePath, int productId) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);

        // Image du produit
        JLabel picLabel = new JLabel();
        picLabel.setAlignmentX(CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        picLabel.setIcon(new ImageIcon(scaled));

        // Nom et prix
        JLabel nameLabel = new JLabel(productName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(price + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GRAY);
        priceLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Bouton d'ajout au panier avec sélection de la quantité
        JButton buyButton = new JButton("Ajouter au panier");
        buyButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        buyButton.setAlignmentX(CENTER_ALIGNMENT);
        buyButton.addActionListener(e -> {
            // Utilisation d'un JSpinner dans un dialog pour choisir la quantité
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            int option = JOptionPane.showOptionDialog(
                    this,
                    spinner,
                    "Sélectionnez la quantité",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );
            if (option == JOptionPane.OK_OPTION) {
                int quantity = (Integer) spinner.getValue();
                boolean inserted = TicketDAO.insertTicket(productId, price, quantity, currentUser.getId());
                if (inserted) {
                    JOptionPane.showMessageDialog(this, productName + " ajouté au panier !");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout au panier.");
                }
            }
        });

        card.add(Box.createVerticalStrut(10));
        card.add(picLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(buyButton);
        card.add(Box.createVerticalStrut(10));
        return card;
    }
}
