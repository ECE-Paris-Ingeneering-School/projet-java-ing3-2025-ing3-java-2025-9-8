package dao;

import model.Discount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {

    /**
     * Retourne la liste de toutes les offres (Discount) dans la BDD.
     */
    public static List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        String sql = "SELECT id, name, description, conditions, discount_value FROM Discount";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                String cond = rs.getString("conditions");
                String value = rs.getString("discount_value");
                discounts.add(new Discount(id, name, desc, cond, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    /**
     * Récupère une offre par son ID.
     */
    public static Discount getDiscountById(int discountId) {
        String sql = "SELECT id, name, description, conditions, discount_value FROM Discount WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, discountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Discount(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("conditions"),
                        rs.getString("discount_value")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insère une nouvelle offre dans la table Discount.
     */
    public static boolean insertDiscount(Discount d) {
        String sql = "INSERT INTO Discount (name, description, conditions, discount_value) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getName());
            stmt.setString(2, d.getDescription());
            stmt.setString(3, d.getConditions());
            stmt.setString(4, d.getValue());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour une offre existante.
     */
    public static boolean updateDiscount(Discount d) {
        String sql = "UPDATE Discount SET name=?, description=?, conditions=?, discount_value=? "
                + "WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getName());
            stmt.setString(2, d.getDescription());
            stmt.setString(3, d.getConditions());
            stmt.setString(4, d.getValue());
            stmt.setInt(5, d.getId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime une offre par son ID.
     */
    public static boolean deleteDiscount(int discountId) {
        String sql = "DELETE FROM Discount WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, discountId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
