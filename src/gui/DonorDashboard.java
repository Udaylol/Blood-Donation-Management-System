package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import Managers.DonorManager;
import main.Donor;

import java.util.Optional;

import javafx.scene.control.Alert.AlertType;

public class DonorDashboard extends BorderPane {
    private MainApp mainApp;
    private DonorManager donorManager;
    private DonorTableView donorTableView;

    public DonorDashboard(MainApp mainApp) {
        this.mainApp = mainApp;
        this.donorManager = new DonorManager();
        donorManager.loadDonors();

        setupUI();
    }

    private void setupUI() {
        // Header
        HBox header = createHeader();
        setTop(header);

        // Sidebar
        VBox sidebar = createSidebar();
        setLeft(sidebar);

        // Main content
        donorTableView = new DonorTableView(donorManager.getAllDonors());
        setCenter(donorTableView);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setSpacing(10);
        header.getStyleClass().add("header");

        Label titleLabel = new Label("Blood Donation Management System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> mainApp.showLoginScreen());

        header.getChildren().addAll(titleLabel, spacer, logoutButton);
        return header;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(250);
        sidebar.getStyleClass().add("sidebar");

        Button addDonorButton = new Button("Add Donor");
        addDonorButton.setMaxWidth(Double.MAX_VALUE);
        addDonorButton.setOnAction(e -> showAddDonorDialog());

        Button listAllButton = new Button("List All Donors");
        listAllButton.setMaxWidth(Double.MAX_VALUE);
        listAllButton.setOnAction(e -> listAllDonors());

        Button listEligibleButton = new Button("List Eligible Donors");
        listEligibleButton.setMaxWidth(Double.MAX_VALUE);
        listEligibleButton.setOnAction(e -> listEligibleDonors());

        Label searchLabel = new Label("Search Options");
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        searchLabel.setPadding(new Insets(10, 0, 5, 0));

        VBox searchByCity = createSearchByCity();
        VBox searchByBloodGroup = createSearchByBloodGroup();

        Button bloodCompatibilityButton = new Button("Blood Compatibility");
        bloodCompatibilityButton.setMaxWidth(Double.MAX_VALUE);
        bloodCompatibilityButton.setOnAction(e -> showBloodCompatibilityDialog());

        Button updateDonorButton = new Button("Update Donor");
        updateDonorButton.setMaxWidth(Double.MAX_VALUE);
        updateDonorButton.setOnAction(e -> showUpdateDonorDialog());

        Button deleteDonorButton = new Button("Delete Donor");
        deleteDonorButton.setMaxWidth(Double.MAX_VALUE);
        deleteDonorButton.setOnAction(e -> showDeleteDonorDialog());

        sidebar.getChildren().addAll(
                addDonorButton,
                listAllButton,
                listEligibleButton,
                searchLabel,
                searchByCity,
                searchByBloodGroup,
                bloodCompatibilityButton,
                updateDonorButton,
                deleteDonorButton
        );

        return sidebar;
    }

    private VBox createSearchByCity() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5, 0, 10, 0));

        Label label = new Label("Search by City");
        TextField cityField = new TextField();
        cityField.setPromptText("Enter city name");

        HBox buttonBox = new HBox(5);
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(120);
        searchButton.setOnAction(e -> searchByCity(cityField.getText(), false));

        Button eligibleButton = new Button("Eligible Only");
        eligibleButton.setPrefWidth(120);
        eligibleButton.setOnAction(e -> searchByCity(cityField.getText(), true));

        buttonBox.getChildren().addAll(searchButton, eligibleButton);
        box.getChildren().addAll(label, cityField, buttonBox);

        return box;
    }

    private VBox createSearchByBloodGroup() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5, 0, 10, 0));

        Label label = new Label("Search by Blood Group");
        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setPromptText("Select blood group");
        bloodGroupCombo.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox = new HBox(5);
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(120);
        searchButton.setOnAction(e -> {
            if (bloodGroupCombo.getValue() != null) {
                searchByBloodGroup(bloodGroupCombo.getValue(), false);
            }
        });

        Button eligibleButton = new Button("Eligible Only");
        eligibleButton.setPrefWidth(120);
        eligibleButton.setOnAction(e -> {
            if (bloodGroupCombo.getValue() != null) {
                searchByBloodGroup(bloodGroupCombo.getValue(), true);
            }
        });

        buttonBox.getChildren().addAll(searchButton, eligibleButton);
        box.getChildren().addAll(label, bloodGroupCombo, buttonBox);

        return box;
    }

    private void showAddDonorDialog() {
        AddDonorDialog dialog = new AddDonorDialog(mainApp.getPrimaryStage());
        Optional<Donor> result = dialog.showAndWait();

        if (result.isPresent()) {
            Donor newDonor = result.get();
            donorManager.addDonor(
                    newDonor.getName(),
                    newDonor.getAge(),
                    newDonor.getBloodGroup(),
                    newDonor.getCity(),
                    newDonor.getContact(),
                    newDonor.getLastDonationDate()
            );
            refreshDonorTable();
        }
    }

    private void listAllDonors() {
        donorTableView.setDonors(donorManager.getAllDonors());
    }

    private void listEligibleDonors() {
        donorTableView.setDonors(donorManager.getEligibleDonors());
    }

    private void searchByCity(String city, boolean onlyEligible) {
        if (city.isEmpty()) {
            showAlert("Please enter a city name", AlertType.WARNING);
            return;
        }
        donorTableView.setDonors(donorManager.getDonorsByCity(city, onlyEligible));
    }

    private void searchByBloodGroup(String bloodGroup, boolean onlyEligible) {
        donorTableView.setDonors(donorManager.getDonorsByBloodGroup(bloodGroup, onlyEligible));
    }

    private void showBloodCompatibilityDialog() {
        BloodCompatibilityView dialog = new BloodCompatibilityView(mainApp.getPrimaryStage());
        dialog.showAndWait();
    }

    private void showDeleteDonorDialog() {
        Donor selectedDonor = donorTableView.getSelectedDonor();
        if (selectedDonor == null) {
            showAlert("Please select a donor to delete", AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Donor");
        confirmation.setHeaderText("Delete Donor Confirmation");
        confirmation.setContentText("Are you sure you want to delete donor: " + selectedDonor.getName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            donorManager.deleteDonor(selectedDonor.getId());
            refreshDonorTable();
        }
    }
    private void showUpdateDonorDialog() {
        Donor selectedDonor = donorTableView.getSelectedDonor();
        if (selectedDonor == null) {
            showAlert("Please select a donor to update", AlertType.WARNING);
            return;
        }

        UpdateDonorDialog dialog = new UpdateDonorDialog(mainApp.getPrimaryStage(), selectedDonor);
        Optional<Donor> result = dialog.showAndWait();

        if (result.isPresent()) {
            Donor updatedDonor = result.get();
            donorManager.updateDonor(
                    updatedDonor.getId(),
                    updatedDonor.getName(),
                    updatedDonor.getAge(),
                    updatedDonor.getBloodGroup(),
                    updatedDonor.getCity(),
                    updatedDonor.getContact(),
                    updatedDonor.getLastDonationDate()
            );
            refreshDonorTable();
        }
    }


    private void refreshDonorTable() {
        donorManager.loadDonors();
        donorTableView.setDonors(donorManager.getAllDonors());
    }


    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.toString());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}