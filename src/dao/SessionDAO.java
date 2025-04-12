package dao;

import model.Session;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {

    // 1) Récupère toutes les sessions via JOIN sur Teacher, DogBreed, Location
    public static List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT s.id, s.session_date, s.session_time, s.level, s.capacity, "
                + "       s.teacher_id, s.location_id, s.dog_breed_id, "
                + "       CONCAT(t.first_name, ' ', t.last_name) AS teacherName, "
                + "       d.breed_name AS breedName, "
                + "       l.name AS locationName "
                + "FROM Session s "
                + "JOIN Teacher t ON s.teacher_id = t.id "
                + "JOIN DogBreed d ON s.dog_breed_id = d.id "
                + "JOIN Location l ON s.location_id = l.id "
                + "ORDER BY s.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                Date date = rs.getDate("session_date");
                Time time = rs.getTime("session_time");
                String level = rs.getString("level");
                int capacity = rs.getInt("capacity");

                int teacherId = rs.getInt("teacher_id");
                int locationId = rs.getInt("location_id");
                int dogBreedId = rs.getInt("dog_breed_id");

                String teacherName = rs.getString("teacherName");
                String breedName = rs.getString("breedName");
                String locationName = rs.getString("locationName");

                // On crée l'objet Session
                Session sObj = new Session(id, date, time, level, capacity,
                        locationId, teacherId, dogBreedId);
                // On y place les champs textuels
                sObj.setTeacherName(teacherName);
                sObj.setBreedName(breedName);
                sObj.setLocationName(locationName);

                sessions.add(sObj);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    // 2) Insère une nouvelle session
    public static boolean insertSession(Session session) {
        String sql = "INSERT INTO Session (session_date, session_time, level, capacity, location_id, teacher_id, dog_breed_id)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, session.getSessionDate());
            stmt.setTime(2, session.getSessionTime());
            stmt.setString(3, session.getLevel());
            stmt.setInt(4, session.getCapacity());
            stmt.setInt(5, session.getLocationId());
            stmt.setInt(6, session.getTeacherId());
            stmt.setInt(7, session.getDogBreedId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3) Met à jour la session (date, heure, niveau, capacité, location, teacher, dogBreed)
    public static boolean updateSession(Session session) {
        String sql = "UPDATE Session SET session_date = ?, session_time = ?, level = ?, capacity = ?, "
                + "location_id = ?, teacher_id = ?, dog_breed_id = ? "
                + "WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, session.getSessionDate());
            stmt.setTime(2, session.getSessionTime());
            stmt.setString(3, session.getLevel());
            stmt.setInt(4, session.getCapacity());
            stmt.setInt(5, session.getLocationId());
            stmt.setInt(6, session.getTeacherId());
            stmt.setInt(7, session.getDogBreedId());
            stmt.setInt(8, session.getId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4) Supprime une session
    public static boolean deleteSession(int sessionId) {
        String sql = "DELETE FROM Session WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5) Récupère une session spécifique (JOIN également, pour avoir teacherName, etc.)
    public static Session getSessionById(int id) {
        String sql = "SELECT s.id, s.session_date, s.session_time, s.level, s.capacity, "
                + "       s.teacher_id, s.location_id, s.dog_breed_id, "
                + "       CONCAT(t.first_name, ' ', t.last_name) AS teacherName, "
                + "       d.breed_name AS breedName, "
                + "       l.name AS locationName "
                + "FROM Session s "
                + "JOIN Teacher t ON s.teacher_id = t.id "
                + "JOIN DogBreed d ON s.dog_breed_id = d.id "
                + "JOIN Location l ON s.location_id = l.id "
                + "WHERE s.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                int sessionId = rs.getInt("id");
                Date date = rs.getDate("session_date");
                Time time = rs.getTime("session_time");
                String level = rs.getString("level");
                int capacity = rs.getInt("capacity");

                int teacherId = rs.getInt("teacher_id");
                int locationId = rs.getInt("location_id");
                int dogBreedId = rs.getInt("dog_breed_id");

                String teacherName = rs.getString("teacherName");
                String breedName = rs.getString("breedName");
                String locationName = rs.getString("locationName");

                Session session = new Session(sessionId, date, time, level, capacity,
                        locationId, teacherId, dogBreedId);
                session.setTeacherName(teacherName);
                session.setBreedName(breedName);
                session.setLocationName(locationName);
                return session;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
