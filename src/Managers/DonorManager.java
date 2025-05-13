package Managers;

import main.Donor;
import main.DBConnection;
import helper.Validation;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DonorManager {
    private List<Donor> donorList = new ArrayList<>();
    private HashMap<String, List<Donor>> BloodTypeMap = new HashMap<>();

    public void loadDonors() {
        donorList.clear();
        BloodTypeMap.clear();
        String query = "SELECT * FROM donors";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet result = s.executeQuery(query)) {
            while (result.next()) {
                LocalDate lastDonation = null;
                Date sqlDate = result.getDate("last_donation_date");
                if (sqlDate != null) {
                    lastDonation = sqlDate.toLocalDate();
                }
                Donor donor = new Donor(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("age"),
                        result.getString("blood_group"),
                        result.getString("city"),
                        result.getString("contact"),
                        lastDonation
                );
                donorList.add(donor);
                BloodTypeMap.computeIfAbsent(donor.getBloodGroup(), k -> new ArrayList<>()).add(donor);
            }
        } catch (SQLException e) {
            showAlert("Error loading donors: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public List<Donor> getAllDonors() {
        List<Donor> sorted = new ArrayList<>(donorList);
        sorted.sort(Comparator.comparing(d -> d.getName().toLowerCase()));
        return sorted;
    }

    public List<Donor> getEligibleDonors() {
        LocalDate today = LocalDate.now();
        List<Donor> eligible = new ArrayList<>();

        for (Donor d : donorList) {
            if (d.getLastDonationDate() == null || d.getLastDonationDate().plusMonths(3).isBefore(today)) {
                eligible.add(d);
            }
        }
        eligible.sort(Comparator.comparing(d -> d.getName().toLowerCase()));
        return eligible;
    }

    public List<Donor> getDonorsByBloodGroup(String bloodGroup, boolean onlyEligible) {
        LocalDate today = LocalDate.now();
        List<Donor> donors = BloodTypeMap.getOrDefault(bloodGroup, new ArrayList<>());
        List<Donor> filtered = new ArrayList<>();

        for (Donor d : donors) {
            if (!onlyEligible || d.getLastDonationDate() == null || d.getLastDonationDate().plusMonths(3).isBefore(today)) {
                filtered.add(d);
            }
        }

        filtered.sort(Comparator.comparing(d -> d.getName().toLowerCase()));
        return filtered;
    }

    public List<Donor> getDonorsByCity(String city, boolean onlyEligible) {
        LocalDate today = LocalDate.now();
        List<Donor> found = new ArrayList<>();

        for (Donor d : donorList) {
            if (d.getCity().equalsIgnoreCase(city)) {
                if (!onlyEligible || d.getLastDonationDate() == null || d.getLastDonationDate().plusMonths(3).isBefore(today)) {
                    found.add(d);
                }
            }
        }
        found.sort(Comparator.comparing(d -> d.getName().toLowerCase()));
        return found;
    }

    public boolean addDonor(String name, int age, String bloodGroup, String city, String contact, LocalDate lastDonationDate) {
        boolean invalid = false;
        StringBuilder errorMessage = new StringBuilder();

        if (!Validation.isValidName(name)) {
            errorMessage.append("Invalid name. Only letters and spaces allowed (2–50 characters).\n");
            invalid = true;
        }
        if (!Validation.isValidAge(age)) {
            errorMessage.append("Invalid age. Must be between 18-65 to donate.\n");
            invalid = true;
        }
        if (!Validation.isValidBloodGroup(bloodGroup)) {
            errorMessage.append("Invalid blood group. Must be A+, A-, B+, B-, AB+, AB-, O+, or O-.\n");
            invalid = true;
        }
        if (!Validation.isValidCity(city)) {
            errorMessage.append("Invalid city. Only letters and spaces allowed.\n");
            invalid = true;
        }
        if (!Validation.isValidContact(contact)) {
            errorMessage.append("Invalid contact. Must be a 10-digit number.\n");
            invalid = true;
        }

        if (invalid) {
            showAlert(errorMessage.toString(), AlertType.ERROR);
            return false;
        }
        String sql = "INSERT INTO donors(name, age, blood_group, city, contact, last_donation_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, bloodGroup);
            ps.setString(4, city);
            ps.setString(5, contact);
            if (lastDonationDate != null) {
                ps.setDate(6, Date.valueOf(lastDonationDate));
            } else {
                ps.setNull(6, Types.DATE);
            }
            ps.executeUpdate();
            showAlert("Donor added successfully.", AlertType.INFORMATION);
            loadDonors();
            return true;
        } catch (SQLException e) {
            showAlert("Error adding donor: " + e.getMessage(), AlertType.ERROR);
            return false;
        }
    }

    public boolean deleteDonor(int id) {
        String sql = "DELETE FROM donors WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                showAlert("Donor deleted successfully.", AlertType.INFORMATION);
                loadDonors();
                return true;
            } else {
                showAlert("Donor not found.", AlertType.WARNING);
                return false;
            }
        } catch (SQLException e) {
            showAlert("Error deleting donor: " + e.getMessage(), AlertType.ERROR);
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
