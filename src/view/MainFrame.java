package view;

import model.User;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private User currentUser;

    public MainFrame(User user) {
        super("Puppy Yoga Paris");
        this.currentUser = user;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel headerPanel = createHeaderPanel();
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // On ajoute nos panneaux
        contentPanel.add(new HomePanel(), "HOME");
        contentPanel.add(new BookingPanel(currentUser), "BOOKING");
        contentPanel.add(new ContactPanel(), "CONTACT");
        contentPanel.add(new CataloguePanel(currentUser), "CATALOGUE");
        contentPanel.add(new ProfilePanel(currentUser), "PROFILE");

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(242,224,200));
        header.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        // Logo
        ImageIcon logoIcon = new ImageIcon("logo.png");
        Image scaled = logoIcon.getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaled);
        JLabel logoLabel = new JLabel(logoIcon);
        header.add(logoLabel, BorderLayout.WEST);

        // Menu central
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,10));
        menuPanel.setBackground(new Color(242,224,200));

        String[] menuItems = {"Acceuil","Réservation","Contact","Catalogue","Mon profil"};
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(242,224,200));
            btn.setBorder(null);
            btn.setFont(new Font("SansSerif",Font.PLAIN,16));
            btn.addActionListener(e -> {
                String id="";
                switch(item) {
                    case "Acceuil": id="HOME"; break;
                    case "Réservation": id="BOOKING"; break;
                    case "Contact": id="CONTACT"; break;
                    case "Catalogue": id="CATALOGUE"; break;
                    case "Mon profil": id="PROFILE"; break;
                }
                cardLayout.show(contentPanel, id);
            });
            menuPanel.add(btn);
        }
        header.add(menuPanel, BorderLayout.CENTER);

        // Panier
        JLabel cartLabel = new JLabel("🛒");
        cartLabel.setFont(new Font("SansSerif",Font.PLAIN,24));
        cartLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new CartFrame(currentUser,
                        () -> {
                            // Callback déclenché après achat
                            cardLayout.show(contentPanel, "PROFILE");
                            contentPanel.remove(4); // Supprimer l'ancien ProfilePanel
                            contentPanel.add(new ProfilePanel(currentUser), "PROFILE"); // Recharger avec les nouvelles données
                        },
                        () -> {
                            // Callback déclenché après mise à jour de l'utilisateur (paiement ou livraison)
                            cardLayout.show(contentPanel, "PROFILE");
                            contentPanel.remove(4);
                            contentPanel.add(new ProfilePanel(currentUser), "PROFILE");
                        }
                ).setVisible(true);
            }
        });
        header.add(cartLabel, BorderLayout.EAST);

        return header;
    }
}
