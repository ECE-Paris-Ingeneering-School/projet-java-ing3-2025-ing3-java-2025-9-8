package dao;

import model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public static List<Event> getEventsFiltered(String niveauFilter,
                                                String dateFilter,
                                                String lieuFilter,
                                                String teacherFilter,
                                                String breedFilter) {
        List<Event> events = new ArrayList<>();

        String sql = "SELECT s.id AS sessionId, "
                + "       s.session_date, s.session_time, s.level, s.capacity, "
                + "       l.name AS locationName, "
                + "       CONCAT(t.first_name, ' ', t.last_name) AS teacherName, "
                + "       d.breed_name AS breedName, "
                + "       (SELECT IFNULL(SUM(od.quantity),0) "
                + "        FROM OrderDetails od "
                + "        JOIN OrderInfo o ON o.id = od.order_id "
                + "        WHERE od.product_id = s.id) AS sold "
                + "FROM `Session` s "
                + "JOIN Location l ON s.location_id = l.id "
                + "JOIN Teacher t ON s.teacher_id = t.id "
                + "JOIN DogBreed d ON s.dog_breed_id = d.id";

        boolean addWhere = false;
        if (!niveauFilter.equalsIgnoreCase("Tous")
                || !dateFilter.isEmpty()
                || !lieuFilter.equalsIgnoreCase("Tous")
                || !teacherFilter.equalsIgnoreCase("Tous")
                || !breedFilter.equalsIgnoreCase("Tous")) {
            sql += " WHERE";
            if (!niveauFilter.equalsIgnoreCase("Tous")) {
                sql += " s.level = ?";
                addWhere = true;
            }
            if (!dateFilter.isEmpty()) {
                if (addWhere) sql += " AND";
                sql += " s.session_date = ?";
                addWhere = true;
            }
            if (!lieuFilter.equalsIgnoreCase("Tous")) {
                if (addWhere) sql += " AND";
                sql += " l.name = ?";
                addWhere = true;
            }
            if (!teacherFilter.equalsIgnoreCase("Tous")) {
                if (addWhere) sql += " AND";
                sql += " CONCAT(t.first_name, ' ', t.last_name) = ?";
                addWhere = true;
            }
            if (!breedFilter.equalsIgnoreCase("Tous")) {
                if (addWhere) sql += " AND";
                sql += " d.breed_name = ?";
            }
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index=1;
            if (!niveauFilter.equalsIgnoreCase("Tous")) {
                stmt.setString(index++, niveauFilter);
            }
            if (!dateFilter.isEmpty()) {
                stmt.setDate(index++, Date.valueOf(dateFilter));
            }
            if (!lieuFilter.equalsIgnoreCase("Tous")) {
                stmt.setString(index++, lieuFilter);
            }
            if (!teacherFilter.equalsIgnoreCase("Tous")) {
                stmt.setString(index++, teacherFilter);
            }
            if (!breedFilter.equalsIgnoreCase("Tous")) {
                stmt.setString(index++, breedFilter);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int sessionId = rs.getInt("sessionId");
                Date sessionDate = rs.getDate("session_date");
                Time sessionTime = rs.getTime("session_time");
                String level = rs.getString("level");
                int capacity = rs.getInt("capacity");
                String locationName = rs.getString("locationName");
                String teacherName = rs.getString("teacherName");
                String breedName = rs.getString("breedName");
                int sold = rs.getInt("sold");

                int placesRestantes = capacity - sold;
                String title = "Session " + sessionId + " à " + locationName;
                String dateStr = sessionDate.toString() + " " + sessionTime.toString();

                Event ev = new Event(sessionId, title, dateStr, level, locationName, placesRestantes);
                ev.setTeacherName(teacherName);
                ev.setBreedName(breedName);

                events.add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
