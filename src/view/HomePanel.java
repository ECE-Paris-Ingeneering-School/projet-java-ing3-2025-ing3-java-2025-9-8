package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class HomePanel extends JPanel {
    private Image backgroundImage;

    public HomePanel() {
        try {
            backgroundImage = ImageIO.read(new File("logo.jpg"));
        } catch (Exception e) {
            backgroundImage = null;
        }
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel overlay = new JPanel(new GridLayout(3, 1));
        overlay.setOpaque(false);

        JLabel titleLabel = new JLabel("Puppy Yoga Paris", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        overlay.add(titleLabel);

        JLabel subTitleLabel = new JLabel("Des cours de yoga en compagnie de chiots", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        subTitleLabel.setForeground(Color.WHITE);
        overlay.add(subTitleLabel);

        JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; color: white;'>"
                + "Puppy Yoga Paris est né de la rencontre entre la passion du yoga et l'amour des chiots.<br>"
                + "Notre mission : allier bien-être et tendresse pour créer des moments uniques."
                + "</div></html>", SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        overlay.add(descriptionLabel);

        add(overlay, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
