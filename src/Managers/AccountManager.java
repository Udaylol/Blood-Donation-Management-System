package Managers;

import main.DBConnection;

import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AccountManager {

    public boolean login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If we have a result, login was successful
        } catch (SQLException e) {
            showAlert("Login error: " + e.getMessage(), AlertType.ERROR);
            return false;
        }
    }

    public boolean createAccount(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert("Username already exists. Try a different one.", AlertType.ERROR);
            return false;
        } catch (SQLException e) {
            showAlert("Account creation error: " + e.getMessage(), AlertType.ERROR);
            return false;
        }
    }

    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.toString());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

