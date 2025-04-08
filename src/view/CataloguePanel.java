package view;

import dao.TicketDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

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

        // Pour chaque produit, on attribue un identifiant unique (par exemple, 2001 pour Brassières, 2002 pour Ensemble, etc.)
        productsPanel.add(createProductCard("Chaussettes de yoga", 12.0, "photos/chaussettes.png", 1001));
        productsPanel.add(createProductCard("Poids", 15.0, "photos/poids.png", 2003));
        productsPanel.add(createProductCard("Tapis de yoga", 30.0, "photos/tapis.png", 2004));
        productsPanel.add(createProductCard("Barres de céréales", 5.0, "photos/barres.png", 1002));
        productsPanel.add(createProductCard("Bouteilles énergisantes", 8.0, "photos/bouteilles.png", 1003));
        productsPanel.add(createProductCard("Brassières de sport", 25.0, "photos/brassieres.png", 2001));
        productsPanel.add(createProductCard("Ensemble de sport", 45.0, "photos/ensemble.png", 2002));
        productsPanel.add(createProductCard("Serviettes", 10.0, "photos/serviettes.png", 1004));

        add(productsPanel, BorderLayout.CENTER);
    }

    // Méthode modifiée : on passe également l'ID du produit pour pouvoir l'insérer dans le panier
    private JPanel createProductCard(String productName, double price, String imagePath, int productId) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);

        JLabel picLabel = new JLabel();
        picLabel.setAlignmentX(CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaled);
        picLabel.setIcon(icon);

        JLabel nameLabel = new JLabel(productName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(price + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GRAY);
        priceLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton buyButton = new JButton("Ajouter au panier");
        buyButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        buyButton.setAlignmentX(CENTER_ALIGNMENT);
        buyButton.addActionListener(e -> {
            // Appel effectif à l'insertion dans le panier
            boolean inserted = TicketDAO.insertTicket(productId, price, 1, currentUser.getId());
            if (inserted) {
                JOptionPane.showMessageDialog(this, productName + " ajouté au panier !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout au panier.");
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
