package view;

import model.Ticket;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CheckoutFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public CheckoutFrame(User user, List<Ticket> cartItems, Runnable onCheckoutComplete, Runnable onUserUpdateCallback) {
        super("Validation de la commande");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Étape 1 : Informations de livraison
        DeliveryInfoPanel deliveryPanel = new DeliveryInfoPanel(user, () -> {
            cardLayout.show(cardPanel, "PAYMENT");
        });

        // Étape 2 : Informations bancaires
        PaymentInfoPanel paymentPanel = new PaymentInfoPanel(user, () -> {
            // ✅ Mise à jour de la BDD
            dao.UserDAO.updateUserDeliveryAndPayment(user);

            // ✅ Mise à jour du profil visuellement
            if (onUserUpdateCallback != null) onUserUpdateCallback.run();

            cardLayout.show(cardPanel, "SUMMARY");
        });

        // Étape 3 : Résumé de la commande
        OrderSummaryPanel summaryPanel = new OrderSummaryPanel(user, cartItems, () -> {
            if (onCheckoutComplete != null) onCheckoutComplete.run();
            dispose(); // ferme la fenêtre
        });

        cardPanel.add(deliveryPanel, "DELIVERY");
        cardPanel.add(paymentPanel, "PAYMENT");
        cardPanel.add(summaryPanel, "SUMMARY");

        add(cardPanel);
        setVisible(true);
    }
}
