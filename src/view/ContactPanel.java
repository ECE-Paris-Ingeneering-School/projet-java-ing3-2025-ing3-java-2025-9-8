package view;

import javax.swing.*;
import java.awt.*;

public class ContactPanel extends JPanel {
    public ContactPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel title=new JLabel("Contact",SwingConstants.CENTER);
        title.setFont(new Font("Serif",Font.BOLD,32));
        add(title,BorderLayout.NORTH);

        JLabel content=new JLabel("<html><div style='text-align:center;font-size:16px;'>"
                +"Adresse: 123 Rue du Chiot<br>"
                +"Téléphone: +33 1 23 45 67 89<br>"
                +"Email: contact@puppy-yoga.com"
                +"</div></html>", SwingConstants.CENTER);
        add(content,BorderLayout.CENTER);
    }
}
