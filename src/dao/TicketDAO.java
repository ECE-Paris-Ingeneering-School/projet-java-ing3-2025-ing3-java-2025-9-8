package dao;

import model.Product;
import model.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public static boolean insertTicket(int sessionId, double price, int quantity, int userId) {
        String sql = "INSERT INTO Ticket (session_id, price, quantity, user_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setInt(4, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Ticket> getCartItems(int userId) {
        List<Ticket> items = new ArrayList<>();
        String sql = """
        SELECT t.*, db.breed_name AS race
        FROM Ticket t
        LEFT JOIN Session s ON t.session_id = s.id
        LEFT JOIN DogBreed db ON s.dog_breed_id = db.id
        WHERE t.user_id = ?
        """;
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
                String race = rs.getString("race");

                Ticket t = new Ticket(id, sessionId, price, quantity, uId, addedDate);
                t.setRace(race);
                items.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static List<Ticket> getLastPurchasedItems(int userId) {
        List<Ticket> items = new ArrayList<>();
        String sql = "SELECT od.product_id AS session_id, od.price, od.quantity, oi.order_date " +
                "FROM OrderDetails od JOIN OrderInfo oi ON od.order_id = oi.id " +
                "WHERE oi.user_id = ? ORDER BY oi.order_date DESC LIMIT 10";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int sessionId = rs.getInt("session_id");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                Timestamp addedDate = rs.getTimestamp("order_date");
                Ticket t = new Ticket(0, sessionId, price, quantity, userId, addedDate);
                items.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static List<Ticket> getLastCartItems(int userId) {
        return getLastPurchasedItems(userId);
    }

    public static boolean removeItemFromCart(int ticketId) {
        String sql = "DELETE FROM Ticket WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Modification de checkoutCart pour mettre à jour le stock des produits achetés
    public static boolean checkoutCart(int userId) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderDetailsStmt = null;
        PreparedStatement reservationStmt = null;
        PreparedStatement deleteStmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            List<Ticket> tickets = getCartItems(userId);
            if (tickets.isEmpty()) return false;

            double total = 0.0;
            int totalSessionQty = 0;
            double totalSessionPrice = 0.0;
            boolean hasBrassiere = false, hasEnsemble = false;
            // Liste pour les tickets correspondant à des produits (ID ≥ 101)
            List<Ticket> productTickets = new ArrayList<>();

            for (Ticket t : tickets) {
                String name;
                int sid = t.getSessionId();
                if (sid <= 100) {
                    name = "Séance #" + sid;
                    totalSessionQty += t.getQuantity();
                    totalSessionPrice += t.getPrice() * t.getQuantity();
                } else {
                    // On considère tous les produits dont l'ID >= 101 comme des produits
                    name = "Produit #" + sid;
                    productTickets.add(t);
                    total += t.getPrice() * t.getQuantity();
                }

                if (name.equalsIgnoreCase("Brassières de sport")) {
                    hasBrassiere = true;
                }
                if (name.equalsIgnoreCase("Ensemble de sport")) {
                    hasEnsemble = true;
                }
            }

            // Application d'une éventuelle remise sur les séances
            if (totalSessionQty >= 4) {
                totalSessionPrice *= 0.8;
            }
            total += totalSessionPrice;
            if (hasBrassiere && hasEnsemble) {
                double sumBE = 0.0;
                for (Ticket t : tickets) {
                    int sid = t.getSessionId();
                    if (sid == 2001 || sid == 2002) {
                        sumBE += t.getPrice() * t.getQuantity();
                    }
                }
                total -= sumBE * 0.2;
            }

            // Insertion de la commande dans OrderInfo
            orderStmt = conn.prepareStatement("INSERT INTO OrderInfo (user_id, order_date, total) VALUES (?, NOW(), ?)", Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, total);
            if (orderStmt.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }
            ResultSet rs = orderStmt.getGeneratedKeys();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int orderId = rs.getInt(1);

            // Insertion des détails de commande
            orderDetailsStmt = conn.prepareStatement("INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)");
            for (Ticket t : tickets) {
                orderDetailsStmt.setInt(1, orderId);
                orderDetailsStmt.setInt(2, t.getSessionId());
                orderDetailsStmt.setInt(3, t.getQuantity());
                orderDetailsStmt.setDouble(4, t.getPrice());
                orderDetailsStmt.addBatch();
            }
            orderDetailsStmt.executeBatch();

            // Pour les séances (tickets dont l'ID < 101), insérer dans Reservation
            reservationStmt = conn.prepareStatement("INSERT INTO Reservation (user_id, session_id, reservation_date) VALUES (?, ?, NOW())");
            for (Ticket t : tickets) {
                if (t.getSessionId() <= 100) {
                    reservationStmt.setInt(1, userId);
                    reservationStmt.setInt(2, t.getSessionId());
                    reservationStmt.addBatch();
                }
            }
            reservationStmt.executeBatch();

            // Mettre à jour le stock des produits achetés (tickets dont l'ID >= 101)
            for (Ticket t : productTickets) {
                int productId = t.getSessionId();
                int qtyPurchased = t.getQuantity();
                Product product = ProductDAO.getProductById(productId);
                if (product != null) {
                    int currentStock = product.getStock();
                    int newStock = currentStock - qtyPurchased;
                    if (newStock < 0) {
                        conn.rollback();  // stock insuffisant
                        return false;
                    }
                    boolean stockUpdated = ProductDAO.updateProductStock(productId, newStock);
                    if (!stockUpdated) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Vider le panier
            deleteStmt = conn.prepareStatement("DELETE FROM Ticket WHERE user_id = ?");
            deleteStmt.setInt(1, userId);
            deleteStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { if (orderStmt != null) orderStmt.close(); } catch(Exception ignored) {}
            try { if (orderDetailsStmt != null) orderDetailsStmt.close(); } catch(Exception ignored) {}
            try { if (reservationStmt != null) reservationStmt.close(); } catch(Exception ignored) {}
            try { if (deleteStmt != null) deleteStmt.close(); } catch(Exception ignored) {}
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch(SQLException e) { e.printStackTrace(); }
        }
    }
}
