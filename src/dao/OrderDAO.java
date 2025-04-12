package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {

    /**
     * Renvoie le chiffre d'affaires total calculé à partir de la table OrderInfo (colonne 'total').
     * Retourne 0 si aucune commande ou s'il y a un souci.
     */
    public static double getTotalRevenue() {
        double totalRevenue = 0.0;
        String sql = "SELECT IFNULL(SUM(total), 0) AS totalRevenue FROM OrderInfo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalRevenue = rs.getDouble("totalRevenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }
}
