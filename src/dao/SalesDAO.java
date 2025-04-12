package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class SalesDAO {

    // 1. Évolution mensuelle du chiffre d'affaires
    public static DefaultCategoryDataset getMonthlyRevenueDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT DATE_FORMAT(order_date, '%Y-%m') as month, SUM(total) as revenue " +
                "FROM OrderInfo " +
                "GROUP BY DATE_FORMAT(order_date, '%Y-%m') " +
                "ORDER BY DATE_FORMAT(order_date, '%Y-%m')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                String month = rs.getString("month");
                double revenue = rs.getDouble("revenue");
                dataset.addValue(revenue, "Chiffre d'affaires", month);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    // 2. Répartition des ventes par produit
    public static DefaultPieDataset getSalesByProductDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String sql = "SELECT p.name as productName, IFNULL(SUM(od.quantity), 0) as quantity " +
                "FROM Product p " +
                "LEFT JOIN OrderDetails od on p.id = od.product_id " +
                "GROUP BY p.id, p.name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                String productName = rs.getString("productName");
                int quantity = rs.getInt("quantity");
                dataset.setValue(productName, quantity);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    // 3. Taux de remplissage des sessions (en pourcentage)
    public static DefaultCategoryDataset getOccupancyDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT s.id, s.capacity, IFNULL(sold.sold, 0) as sold " +
                "FROM Session s " +
                "LEFT JOIN (SELECT od.product_id, SUM(od.quantity) as sold FROM OrderDetails od GROUP BY od.product_id) sold " +
                "ON s.id = sold.product_id " +
                "ORDER BY s.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int sessionId = rs.getInt("id");
                int capacity = rs.getInt("capacity");
                int sold = rs.getInt("sold");
                double occupancy = capacity > 0 ? ((double)sold / capacity) * 100 : 0;
                dataset.addValue(occupancy, "Taux de remplissage", "Session " + sessionId);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    // 4. Comparaison des ventes : Sessions vs Boutique
    public static DefaultCategoryDataset getSessionVsBoutiqueDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT CASE WHEN od.product_id <= 100 THEN 'Sessions' ELSE 'Boutique' END as type, " +
                "       SUM(od.quantity * od.price) as revenue " +
                "FROM OrderDetails od " +
                "GROUP BY type";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                String type = rs.getString("type");
                double revenue = rs.getDouble("revenue");
                dataset.addValue(revenue, type, "Global");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }
}
