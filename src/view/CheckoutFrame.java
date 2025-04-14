package view;

import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * CheckoutFrame orchestre le processus de paiement en deux étapes :
 * 1. DeliveryInfoPanel : saisie/confirmation des informations personnelles et de livraison.
 * 2. PaymentInfoPanel : saisie des informations bancaires.
 * Une fois ces deux étapes complétées, CheckoutFrame se ferme et appelle le callback onFinish.
 */
public class CheckoutFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private User currentUser;
    private Runnable onFinish; // Callback une fois les infos de livraison et paiement validées

    public CheckoutFrame(User user, Runnable onFinish) {
        super("Validation de la commande");
        this.currentUser = user;
        this.onFinish = onFinish;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panneau 1 : informations personnelles et de livraison
        DeliveryInfoPanel deliveryPanel = new DeliveryInfoPanel(currentUser, () -> {
            // Une fois les infos de livraison validées, passage au panneau de paiement
            cardLayout.show(cardPanel, "PAYMENT");
        });

        // Panneau 2 : informations bancaires
        PaymentInfoPanel paymentPanel = new PaymentInfoPanel(currentUser, () -> {
            // Après validation des infos de paiement, fermer CheckoutFrame et appeler onFinish
            JOptionPane.showMessageDialog(this,
                    "Vos informations de paiement ont été enregistrées.\nVous serez redirigé vers le récapitulatif de votre commande.");
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
            if (onFinish != null) {
                onFinish.run();
            }
        });

        cardPanel.add(deliveryPanel, "DELIVERY");
        cardPanel.add(paymentPanel, "PAYMENT");

        add(cardPanel);
        setVisible(true);
    }
}
