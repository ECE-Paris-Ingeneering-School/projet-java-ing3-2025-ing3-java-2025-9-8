package view;

import dao.TicketDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class PurchaseFrame extends JFrame {

    public PurchaseFrame(JFrame parent,
                         int sessionId,
                         String eventTitle,
                         String eventDate,
                         String eventNiveau,
                         String eventLieu,
                         double unitPrice,
                         int maxQuantity,
                         User currentUser) {
        super("Récapitulatif d'achat");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel=new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(242,224,200));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        JLabel headerLabel=new JLabel("Récapitulatif d'achat",SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif",Font.BOLD,24));
        headerPanel.add(headerLabel,BorderLayout.CENTER);
        add(headerPanel,BorderLayout.NORTH);

        // Contenu
        JPanel contentPanel=new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        contentPanel.add(new JLabel("Titre : "+eventTitle));
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(new JLabel("Date : "+eventDate));
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(new JLabel("Niveau : "+eventNiveau));
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(new JLabel("Lieu : "+eventLieu));
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(new JLabel("Prix unitaire : "+unitPrice+" €"));
        contentPanel.add(Box.createVerticalStrut(15));

        JPanel qtyPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
        qtyPanel.add(new JLabel("Quantité : "));
        JSpinner qtySpinner=new JSpinner(new SpinnerNumberModel(1,1,maxQuantity,1));
        qtyPanel.add(qtySpinner);
        contentPanel.add(qtyPanel);

        add(contentPanel,BorderLayout.CENTER);

        // Footer
        JPanel footerPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        JButton addToCartButton=new JButton("Ajouter au panier");
        JButton cancelButton=new JButton("Retour");
        footerPanel.add(addToCartButton);
        footerPanel.add(cancelButton);
        add(footerPanel,BorderLayout.SOUTH);

        addToCartButton.addActionListener(e->{
            int quantity=(Integer)qtySpinner.getValue();
            // On appelle TicketDAO pour l'ajout
            boolean inserted=TicketDAO.insertTicket(sessionId, unitPrice, quantity, currentUser.getId());
            if(inserted){
                JOptionPane.showMessageDialog(this,"Billet ajouté au panier !");
            } else {
                JOptionPane.showMessageDialog(this,"Erreur lors de l'ajout du billet au panier.");
            }
            dispose();
        });

        cancelButton.addActionListener(e->dispose());
    }
}
