package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) AS count FROM User WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(User user) {
        if (emailExists(user.getEmail())) return false;

        String sql = "INSERT INTO User (username, password, email, name, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getName());
            stmt.setString(5, user.getRole());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User login(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        username,
                        password,
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("role")
                );

                // Champs supplémentaires
                try {
                    user.setPhone(rs.getString("phone"));
                    user.setStreet(rs.getString("street"));
                    user.setComplement(rs.getString("complement"));
                    user.setPostalCode(rs.getString("postalCode"));
                    user.setCity(rs.getString("city"));
                    user.setRegion(rs.getString("region"));

                    user.setCardNumber(rs.getString("cardNumber"));
                    user.setCardName(rs.getString("cardName"));
                    user.setCardMonth(rs.getString("cardMonth"));
                    user.setCardYear(rs.getString("cardYear"));
                    user.setCardCvv(rs.getString("cardCvv"));
                } catch (SQLException e) {
                    // Ignorer les champs manquants si la table n'est pas à jour
                }

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateUserDeliveryAndPayment(User user) {
        String sql = "UPDATE User SET phone=?, street=?, complement=?, postalCode=?, city=?, region=?, " +
                "cardNumber=?, cardName=?, cardMonth=?, cardYear=?, cardCvv=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPhone());
            stmt.setString(2, user.getStreet());
            stmt.setString(3, user.getComplement());
            stmt.setString(4, user.getPostalCode());
            stmt.setString(5, user.getCity());
            stmt.setString(6, user.getRegion());

            stmt.setString(7, user.getCardNumber());
            stmt.setString(8, user.getCardName());
            stmt.setString(9, user.getCardMonth());
            stmt.setString(10, user.getCardYear());
            stmt.setString(11, user.getCardCvv());

            stmt.setInt(12, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("role")
                );

                try {
                    user.setPhone(rs.getString("phone"));
                    user.setStreet(rs.getString("street"));
                    user.setComplement(rs.getString("complement"));
                    user.setPostalCode(rs.getString("postalCode"));
                    user.setCity(rs.getString("city"));
                    user.setRegion(rs.getString("region"));

                    user.setCardNumber(rs.getString("cardNumber"));
                    user.setCardName(rs.getString("cardName"));
                    user.setCardMonth(rs.getString("cardMonth"));
                    user.setCardYear(rs.getString("cardYear"));
                    user.setCardCvv(rs.getString("cardCvv"));
                } catch (SQLException e) {
                    // champs absents : ok
                }

                users.add(user);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static User getUserById(int id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("role")
                );

                try {
                    user.setPhone(rs.getString("phone"));
                    user.setStreet(rs.getString("street"));
                    user.setComplement(rs.getString("complement"));
                    user.setPostalCode(rs.getString("postalCode"));
                    user.setCity(rs.getString("city"));
                    user.setRegion(rs.getString("region"));

                    user.setCardNumber(rs.getString("cardNumber"));
                    user.setCardName(rs.getString("cardName"));
                    user.setCardMonth(rs.getString("cardMonth"));
                    user.setCardYear(rs.getString("cardYear"));
                    user.setCardCvv(rs.getString("cardCvv"));
                } catch (SQLException e) {
                    // champs absents : ok
                }

                return user;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM User WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUser(User user) {
        String sql = "UPDATE User SET username = ?, password = ?, email = ?, name = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getName());
            stmt.setString(5, user.getRole());
            stmt.setInt(6, user.getId());
            return stmt.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
