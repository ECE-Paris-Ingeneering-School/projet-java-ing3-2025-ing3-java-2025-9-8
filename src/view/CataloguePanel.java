package view;

import dao.ProductDAO;
import dao.TicketDAO;
import model.Product;
import model.Ticket;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CataloguePanel extends JPanel {
    private User currentUser;

    public CataloguePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Catalogue", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        // FlowLayout centré pour afficher les cartes produit
        JPanel productsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        productsPanel.setBackground(Color.WHITE);

        // Exemple : vous pouvez adapter la liste de produits / IDs :
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
     * Crée une carte produit avec image, nom, prix et un bouton pour ajouter au panier.
     * La quantité sélectionnable sera plafonnée en fonction du stock restant (bdd - panier).
     *
     * @param productName Nom du produit (affiché en label).
     * @param price       Prix du produit (affiché en label).
     * @param imagePath   Chemin de l'image du produit.
     * @param productId   ID du produit correspondant à votre BDD (≥101).
     * @return Le JPanel représentant la carte produit.
     */
    private JPanel createProductCard(String productName, double price, String imagePath, int productId) {
        // Création du panel "carte produit"
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(220, 280)); // Ajustez la taille à votre convenance
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Récupérer l'objet Product pour connaître son stock
        Product product = ProductDAO.getProductById(productId);
        int stockEnBase = (product != null) ? product.getStock() : 0;

        // Image du produit
        JLabel picLabel = new JLabel();
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        picLabel.setIcon(new ImageIcon(scaled));

        // Nom du produit
        JLabel nameLabel = new JLabel(productName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Prix
        JLabel priceLabel = new JLabel(price + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GRAY);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bouton d'ajout au panier
        JButton buyButton = new JButton("Ajouter au panier");
        buyButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButton.setPreferredSize(new Dimension(140, 30));

        buyButton.addActionListener(e -> {
            if (stockEnBase <= 0) {
                JOptionPane.showMessageDialog(card,
                        "Produit en rupture de stock !",
                        "Stock épuisé",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Calculer la quantité déjà présente dans le panier pour ce produit
            List<Ticket> currentItems = TicketDAO.getCartItems(currentUser.getId());
            int currentQty = 0;
            for (Ticket t : currentItems) {
                if (t.getSessionId() == productId) {
                    currentQty += t.getQuantity();
                }
            }
            int stockRestant = stockEnBase - currentQty;
            if (stockRestant <= 0) {
                JOptionPane.showMessageDialog(card,
                        "Vous avez déjà ajouté la totalité du stock disponible pour ce produit.",
                        "Alerte stock",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Créer un spinner plafonné à stockRestant
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, stockRestant, 1));
            int option = JOptionPane.showOptionDialog(
                    card,
                    spinner,
                    "Sélectionnez la quantité (max " + stockRestant + ")",
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
                    JOptionPane.showMessageDialog(card,
                            productName + " ajouté(e) au panier !",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(card,
                            "Erreur lors de l'ajout au panier.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Assemblage des composants
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
