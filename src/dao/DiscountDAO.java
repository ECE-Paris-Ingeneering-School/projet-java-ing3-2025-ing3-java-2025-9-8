package dao;

import model.Discount;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {

    public static List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        String sql = "SELECT id, name, description, min_quantity, discount_type, discount_amount, target_category, target_id, start_date, end_date FROM Discount";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                discounts.add(new Discount(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("min_quantity"),
                        rs.getString("discount_type"),
                        rs.getDouble("discount_amount"),
                        rs.getString("target_category"),
                        rs.getInt("target_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    public static Discount getDiscountById(int discountId) {
        String sql = "SELECT id, name, description, min_quantity, discount_type, discount_amount, target_category, target_id, start_date, end_date FROM Discount WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, discountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Discount(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("min_quantity"),
                        rs.getString("discount_type"),
                        rs.getDouble("discount_amount"),
                        rs.getString("target_category"),
                        rs.getInt("target_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertDiscount(Discount d) {
        String sql = "INSERT INTO Discount (name, description, min_quantity, discount_type, discount_amount, target_category, target_id, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getName());
            stmt.setString(2, d.getDescription());
            stmt.setInt(3, d.getMinQuantity());
            stmt.setString(4, d.getDiscountType());
            stmt.setDouble(5, d.getDiscountAmount());
            stmt.setString(6, d.getTargetCategory());
            stmt.setInt(7, d.getTargetId());
            stmt.setDate(8, d.getStartDate());
            stmt.setDate(9, d.getEndDate());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateDiscount(Discount d) {
        String sql = "UPDATE Discount SET name=?, description=?, min_quantity=?, discount_type=?, discount_amount=?, target_category=?, target_id=?, start_date=?, end_date=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getName());
            stmt.setString(2, d.getDescription());
            stmt.setInt(3, d.getMinQuantity());
            stmt.setString(4, d.getDiscountType());
            stmt.setDouble(5, d.getDiscountAmount());
            stmt.setString(6, d.getTargetCategory());
            stmt.setInt(7, d.getTargetId());
            stmt.setDate(8, d.getStartDate());
            stmt.setDate(9, d.getEndDate());
            stmt.setInt(10, d.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteDiscount(int discountId) {
        String sql = "DELETE FROM Discount WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, discountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retourne les promotions actives (la date du jour doit être comprise entre start_date et end_date)
    public static List<Discount> getActiveDiscounts() {
        List<Discount> active = new ArrayList<>();
        String sql = "SELECT id, name, description, min_quantity, discount_type, discount_amount, target_category, target_id, start_date, end_date FROM Discount WHERE CURDATE() BETWEEN start_date AND end_date";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                active.add(new Discount(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("min_quantity"),
                        rs.getString("discount_type"),
                        rs.getDouble("discount_amount"),
                        rs.getString("target_category"),
                        rs.getInt("target_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return active;
    }
}
