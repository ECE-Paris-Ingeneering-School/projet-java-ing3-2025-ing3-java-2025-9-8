package view;

import dao.SessionDAO;
import model.Session;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;

public class SessionForm extends JFrame {
    private JTextField dateField;     // format attendu : yyyy-mm-dd
    private JTextField timeField;     // format attendu : hh:mm:ss
    private JComboBox<String> levelCombo;
    private JSpinner capacitySpinner;

    // Nouveaux champs pour le professeur, la race du chien et la localisation
    private JComboBox<String> teacherCombo;
    private JComboBox<String> dogBreedCombo;
    private JComboBox<String> locationCombo;

    private JButton saveButton;

    // La session à modifier (null si ajout)
    private Session session;
    // Pour mettre à jour la liste des sessions dans le panel parent
    private AdminSessionYogaPanel parentPanel;

    // Tableaux de valeurs statiques pour le choix des options.
    // Ces mappings peuvent ensuite être remplacés par des chargements depuis la BDD.
    private static String[] teachers = {"Sophie Lemoine", "Marc Dubois", "Claire Martin", "Luc Bernard", "Julie Moreau"};
    private static String[] breeds = {"Golden Retriever", "Labrador", "Beagle", "Bulldog", "Cocker Spaniel", "Poodle", "Schnauzer"};
    private static String[] locations = {"Paris 6", "Neuilly sur Seine", "Paris 4"};

    public SessionForm(Integer sessionId, AdminSessionYogaPanel parentPanel) {
        this.parentPanel = parentPanel;
        if(sessionId != null) {
            session = SessionDAO.getSessionById(sessionId);
        }
        setTitle(session != null ? "Modifier Session" : "Ajouter Session");
        setSize(500, 400); // Taille ajustée pour contenir tous les champs
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Création d'un panel de formulaire (7 lignes, 2 colonnes)
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Date
        formPanel.add(new JLabel("Date (yyyy-mm-dd) :"));
        dateField = new JTextField(session != null ? session.getSessionDate().toString() : "");
        formPanel.add(dateField);

        // 2. Heure
        formPanel.add(new JLabel("Heure (hh:mm:ss) :"));
        timeField = new JTextField(session != null ? session.getSessionTime().toString() : "");
        formPanel.add(timeField);

        // 3. Niveau
        formPanel.add(new JLabel("Niveau :"));
        String[] levels = {"débutant", "intermédiaire", "avancé"};
        levelCombo = new JComboBox<>(levels);
        if(session != null) {
            levelCombo.setSelectedItem(session.getLevel());
        }
        formPanel.add(levelCombo);

        // 4. Capacité
        formPanel.add(new JLabel("Capacité :"));
        capacitySpinner = new JSpinner(new SpinnerNumberModel(session != null ? session.getCapacity() : 10, 1, 1000, 1));
        formPanel.add(capacitySpinner);

        // 5. Professeur
        formPanel.add(new JLabel("Professeur :"));
        teacherCombo = new JComboBox<>(teachers);
        if(session != null) {
            // On suppose que l'ID du professeur correspond à (teacherId - 1) dans le tableau.
            teacherCombo.setSelectedIndex(session.getTeacherId() - 1);
        }
        formPanel.add(teacherCombo);

        // 6. Race du chien
        formPanel.add(new JLabel("Race du chien :"));
        dogBreedCombo = new JComboBox<>(breeds);
        if(session != null) {
            dogBreedCombo.setSelectedIndex(session.getDogBreedId() - 1);
        }
        formPanel.add(dogBreedCombo);

        // 7. Localisation
        formPanel.add(new JLabel("Localisation :"));
        locationCombo = new JComboBox<>(locations);
        if(session != null) {
            locationCombo.setSelectedIndex(session.getLocationId() - 1);
        }
        formPanel.add(locationCombo);

        // Bouton d'enregistrement
        saveButton = new JButton(session != null ? "Modifier" : "Ajouter");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton pour sauvegarder la session
        saveButton.addActionListener(e -> saveSession());
    }

    private void saveSession() {
        // Récupère les valeurs saisies par l'utilisateur
        String dateText = dateField.getText().trim();  // Format yyyy-mm-dd
        String timeText = timeField.getText().trim();  // Format hh:mm:ss
        String level = (String) levelCombo.getSelectedItem();
        int capacity = (Integer) capacitySpinner.getValue();

        // Récupère les IDs depuis les JComboBox (mapping : index + 1)
        int teacherId = teacherCombo.getSelectedIndex() + 1;
        int dogBreedId = dogBreedCombo.getSelectedIndex() + 1;
        int locationId = locationCombo.getSelectedIndex() + 1;

        Date sessionDate;
        Time sessionTime;
        try {
            sessionDate = Date.valueOf(dateText);
            sessionTime = Time.valueOf(timeText);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Date ou heure invalide.\nFormat attendu : yyyy-mm-dd / hh:mm:ss");
            return;
        }

        if (session == null) {
            // Création d'une nouvelle session avec toutes les options
            Session newSession = new Session(0, sessionDate, sessionTime, level, capacity, locationId, teacherId, dogBreedId);
            boolean success = SessionDAO.insertSession(newSession);
            if (success) {
                JOptionPane.showMessageDialog(this, "Nouvelle session ajoutée avec succès !");
                parentPanel.loadSessions();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l’ajout de la session.");
            }
        } else {
            // Modification d'une session existante
            session.setSessionDate(sessionDate);
            session.setSessionTime(sessionTime);
            session.setLevel(level);
            session.setCapacity(capacity);
            session.setTeacherId(teacherId);
            session.setDogBreedId(dogBreedId);
            session.setLocationId(locationId);

            boolean success = SessionDAO.updateSession(session);
            if (success) {
                JOptionPane.showMessageDialog(this, "Session modifiée avec succès !");
                parentPanel.loadSessions();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la session.");
            }
        }
    }
}
