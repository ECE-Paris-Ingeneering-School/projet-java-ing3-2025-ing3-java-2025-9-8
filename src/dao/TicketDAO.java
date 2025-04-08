package dao;

import model.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    // Méthode pour insérer un ticket dans le panier
    public static boolean insertTicket(int sessionId, double price, int quantity, int userId) {
        String sql = "INSERT INTO Ticket (session_id, price, quantity, user_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setInt(4, userId);
            int affected = stmt.executeUpdate();
            return (affected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour récupérer les items du panier
    public static List<Ticket> getCartItems(int userId) {
        List<Ticket> items = new ArrayList<>();
        String sql = "SELECT * FROM Ticket WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int sessionId = rs.getInt("session_id");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                int uId = rs.getInt("user_id");
                Timestamp addedDate = rs.getTimestamp("added_date");
                Ticket t = new Ticket(id, sessionId, price, quantity, uId, addedDate);
                items.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Méthode pour supprimer un item du panier
    public static boolean removeItemFromCart(int ticketId) {
        String sql = "DELETE FROM Ticket WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            int affected = stmt.executeUpdate();
            return (affected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode checkoutCart révisée
    public static boolean checkoutCart(int userId) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderDetailsStmt = null;
        PreparedStatement reservationStmt = null;
        PreparedStatement deleteStmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Démarrage de la transaction

            // 1. Récupérer les tickets du panier
            List<Ticket> tickets = getCartItems(userId);
            if (tickets.isEmpty()) {
                System.out.println("Panier vide pour userId = " + userId);
                return false;
            }

            // 2. Calculer le total avec les remises
            double total = 0.0;
            int totalSessionQty = 0;
            double totalSessionPrice = 0.0;
            boolean hasBrassiere = false, hasEnsemble = false;

            for (Ticket t : tickets) {
                String name;
                int sid = t.getSessionId();
                if (sid <= 100) {
                    name = "Séance #" + sid;
                } else if (sid == 2001) {
                    name = "Brassières de sport";
                } else if (sid == 2002) {
                    name = "Ensemble de sport";
                } else if (sid == 2003) {
                    name = "Poids";
                } else if (sid == 2004) {
                    name = "Tapis de yoga";
                } else {
                    name = "Article #" + sid;
                }
                double price = t.getPrice();
                int qty = t.getQuantity();
                double lineTotal = price * qty;

                if (name.startsWith("Séance #")) {
                    totalSessionQty += qty;
                    totalSessionPrice += lineTotal;
                } else {
                    total += lineTotal;
                }
                if (name.equalsIgnoreCase("Brassières de sport")) {
                    hasBrassiere = true;
                } else if (name.equalsIgnoreCase("Ensemble de sport")) {
                    hasEnsemble = true;
                }
            }

            // Remise de 20 % sur les billets de séance si le total de billets est ≥ 4
            if (totalSessionQty >= 4) {
                totalSessionPrice *= 0.8;
            }
            total += totalSessionPrice;

            // Remise additionnelle de 20 % si achat simultané d'une brassière et d'un ensemble
            if (hasBrassiere && hasEnsemble) {
                double sumBE = 0.0;
                for (Ticket t : tickets) {
                    int sid = t.getSessionId();
                    if (sid == 2001 || sid == 2002) {
                        sumBE += t.getPrice() * t.getQuantity();
                    }
                }
                double discount = sumBE * 0.2;
                total -= discount;
            }

            // 3. Insertion dans OrderInfo (facture)
            String insertOrderSql = "INSERT INTO OrderInfo (user_id, order_date, total) VALUES (?, NOW(), ?)";
            orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, total);
            int affected = orderStmt.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }
            ResultSet rs = orderStmt.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            // 4. Insertion dans OrderDetails pour chaque ticket
            String insertOrderDetailsSql = "INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            orderDetailsStmt = conn.prepareStatement(insertOrderDetailsSql);
            for (Ticket t : tickets) {
                orderDetailsStmt.setInt(1, orderId);
                orderDetailsStmt.setInt(2, t.getSessionId()); // Utilise session_id comme identifiant produit
                orderDetailsStmt.setInt(3, t.getQuantity());
                orderDetailsStmt.setDouble(4, t.getPrice());
                orderDetailsStmt.addBatch();
            }
            orderDetailsStmt.executeBatch();

            // 5. Pour les tickets correspondant à des séances, insérer une réservation
            String insertReservationSql = "INSERT INTO Reservation (user_id, session_id, reservation_date) VALUES (?, ?, NOW())";
            reservationStmt = conn.prepareStatement(insertReservationSql);
            for (Ticket t : tickets) {
                if (t.getSessionId() <= 100) { // Considérer session_id ≤ 100 comme séance
                    reservationStmt.setInt(1, userId);
                    reservationStmt.setInt(2, t.getSessionId());
                    reservationStmt.addBatch();
                }
            }
            reservationStmt.executeBatch();

            // 6. Vider le panier
            String deleteSql = "DELETE FROM Ticket WHERE user_id = ?";
            deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, userId);
            deleteStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.rollback();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { if (orderStmt != null) orderStmt.close(); } catch (Exception e) { }
            try { if (orderDetailsStmt != null) orderDetailsStmt.close(); } catch (Exception e) { }
            try { if (reservationStmt != null) reservationStmt.close(); } catch (Exception e) { }
            try { if (deleteStmt != null) deleteStmt.close(); } catch (Exception e) { }
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
