package view;

import model.User;
import javax.swing.*;
import java.awt.*;

public class EventCard extends JPanel {
    private User currentUser;

    public EventCard(int sessionId,
                     String title,
                     String date,
                     String niveau,
                     String lieu,
                     int placesRestantes,
                     String teacherName,
                     String breedName,
                     User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setBackground(Color.WHITE);

        // Création des étiquettes d'information
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel profLabel = new JLabel("Prof : " + teacherName);
        profLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel breedLabel = new JLabel("Chien : " + breedName);
        breedLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lieuLabel = new JLabel("Lieu : " + lieu);
        lieuLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel niveauLabel = new JLabel("Niveau : " + niveau);
        niveauLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Définition du bouton selon la disponibilité
        boolean disponible = (placesRestantes > 0);
        JButton actionBtn = new JButton(disponible ? "Acheter billet" : "Billeterie close");
        actionBtn.setEnabled(disponible);
        actionBtn.setBackground(disponible ? new Color(30, 144, 255) : Color.LIGHT_GRAY);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setAlignmentX(CENTER_ALIGNMENT);

        // Charge l'image du chien en fonction de la race
        JLabel dogPicLabel = new JLabel();
        dogPicLabel.setAlignmentX(CENTER_ALIGNMENT);
        ImageIcon icon = getDogIcon(breedName);
        if (icon != null) {
            dogPicLabel.setIcon(icon);
        }

        if (disponible) {
            actionBtn.addActionListener(e -> {
                double unitPrice = 20.0;
                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                new PurchaseFrame(parent,
                        sessionId,
                        title,
                        date,
                        niveau,
                        lieu,
                        unitPrice,
                        placesRestantes,
                        currentUser).setVisible(true);
            });
        }

        // Ajout des composants sur le panneau
        add(Box.createVerticalStrut(10));
        add(dogPicLabel);
        add(titleLabel);
        add(dateLabel);
        add(profLabel);
        add(breedLabel);
        add(lieuLabel);
        add(niveauLabel);
        add(Box.createVerticalStrut(10));
        add(actionBtn);
        add(Box.createVerticalStrut(10));
    }

    private ImageIcon getDogIcon(String breedName) {
        String path = null;
        if (breedName.equalsIgnoreCase("Golden Retriever")) {
            path = "photos/golden.png";
        } else if (breedName.equalsIgnoreCase("Labrador")) {
            path = "photos/labrador.png";
        } else if (breedName.equalsIgnoreCase("Beagle")) {
            path = "photos/beagle.png";
        } else if (breedName.equalsIgnoreCase("Bulldog")) {
            path = "photos/bulldog.png";
        } else if (breedName.equalsIgnoreCase("Cocker Spaniel")) {
            path = "photos/cocker.png";
        } else if (breedName.equalsIgnoreCase("Poodle")) {
            path = "photos/poodle.png";
        } else if (breedName.equalsIgnoreCase("Schnauzer")) {
            path = "photos/schnauzer.png";
        }
        if (path != null) {
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        return null;
    }
}
