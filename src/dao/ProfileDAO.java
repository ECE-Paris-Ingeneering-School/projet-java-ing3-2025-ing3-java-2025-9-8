package dao;

import model.SessionReservation;
import model.OrderInfoSimple;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileDAO {

    // Récupérer la liste des réservations
    public static List<SessionReservation> getUserReservations(int userId) {
        List<SessionReservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id AS reservationId, r.reservation_date, "
                + "       s.session_date, s.session_time, s.level, l.name AS locationName "
                + "FROM Reservation r "
                + "JOIN Session s ON s.id = r.session_id "
                + "JOIN Location l ON l.id = s.location_id "
                + "WHERE r.user_id = ? "
                + "ORDER BY r.reservation_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int reservationId = rs.getInt("reservationId");
                Timestamp reservationDate = rs.getTimestamp("reservation_date");
                Date sessDate = rs.getDate("session_date");
                Time sessTime = rs.getTime("session_time");
                String level = rs.getString("level");
                String locationName = rs.getString("locationName");

                reservations.add(new SessionReservation(
                        reservationId,
                        reservationDate,
                        sessDate,
                        sessTime,
                        level,
                        locationName
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Récupérer la liste des commandes (factures)
    public static List<OrderInfoSimple> getUserOrders(int userId) {
        List<OrderInfoSimple> orders = new ArrayList<>();
        String sql = "SELECT id, order_date, total "
                + "FROM OrderInfo "
                + "WHERE user_id = ? "
                + "ORDER BY order_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int orderId = rs.getInt("id");
                Timestamp orderDate = rs.getTimestamp("order_date");
                double total = rs.getDouble("total");
                orders.add(new OrderInfoSimple(orderId, orderDate, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
