package view;

import dao.EventDAO;
import model.Event;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookingPanel extends JPanel {

    private JPanel eventsPanel;
    private JComboBox<String> niveauCombo;
    private JTextField dateField;
    private JComboBox<String> lieuCombo;
    private JComboBox<String> teacherCombo;
    private JComboBox<String> breedCombo;

    private User currentUser;

    public BookingPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        // Panel de filtres
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        filterPanel.setBackground(new Color(250, 240, 230));
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Filtrer les événements",
                0, 0, new Font("SansSerif", Font.BOLD, 14), Color.DARK_GRAY));

        // Filtre pour le niveau
        filterPanel.add(new JLabel("Niveau:"));
        niveauCombo = new JComboBox<>(new String[]{"Tous", "débutant", "intermédiaire", "avancé"});
        filterPanel.add(niveauCombo);

        // Filtre pour la date
        filterPanel.add(new JLabel("Date:"));
        dateField = new JTextField(10);
        filterPanel.add(dateField);

        // Filtre pour le lieu
        filterPanel.add(new JLabel("Lieu:"));
        lieuCombo = new JComboBox<>(new String[]{"Tous", "Paris 6", "Neuilly sur Seine", "Paris 4"});
        filterPanel.add(lieuCombo);

        // Filtre pour le professeur
        filterPanel.add(new JLabel("Prof:"));
        teacherCombo = new JComboBox<>(new String[]{"Tous", "Sophie Lemoine", "Marc Dubois", "Claire Martin", "Luc Bernard", "Julie Moreau"});
        filterPanel.add(teacherCombo);

        // Filtre pour la race du chien
        filterPanel.add(new JLabel("Race:"));
        breedCombo = new JComboBox<>(new String[]{"Tous", "Golden Retriever", "Labrador", "Beagle", "Bulldog", "Cocker Spaniel", "Poodle", "Schnauzer"});
        filterPanel.add(breedCombo);

        JButton filterButton = new JButton("Filtrer");
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.NORTH);

        // Panel d'affichage des événements
        eventsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(eventsPanel), BorderLayout.CENTER);

        // Bouton qui lance le filtrage
        filterButton.addActionListener(e -> updateEvents());
        updateEvents();
    }

    private void updateEvents() {
        String niveauFilter  = (String) niveauCombo.getSelectedItem();
        String dateFilter    = dateField.getText().trim();
        String lieuFilter    = (String) lieuCombo.getSelectedItem();
        String teacherFilter = (String) teacherCombo.getSelectedItem();
        String breedFilter   = (String) breedCombo.getSelectedItem();

        List<Event> events = EventDAO.getEventsFiltered(niveauFilter, dateFilter, lieuFilter, teacherFilter, breedFilter);

        eventsPanel.removeAll();
        for (Event ev : events) {
            eventsPanel.add(new EventCard(
                    ev.getSessionId(),
                    ev.getTitle(),
                    ev.getDate(),
                    ev.getNiveau(),
                    ev.getLieu(),
                    ev.getPlacesRestantes(),
                    ev.getTeacherName(),
                    ev.getBreedName(),
                    currentUser
            ));
        }
        eventsPanel.revalidate();
        eventsPanel.repaint();
    }
}
