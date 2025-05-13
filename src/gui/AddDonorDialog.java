package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import helper.Validation;
import main.Donor;

public class AddDonorDialog extends Dialog<Donor> {
    private TextField nameField;
    private TextField ageField;
    private ComboBox<String> bloodGroupCombo;
    private TextField cityField;
    private TextField contactField;
    private DatePicker lastDonationPicker;

    public AddDonorDialog(Stage owner) {
        setTitle("Add New Donor");
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create and add form fields
        nameField = new TextField();
        nameField.setPromptText("Enter name");

        ageField = new TextField();
        ageField.setPromptText("Enter age");
        TextFormatter<Integer> ageFormatter = new TextFormatter<>(
                new IntegerStringConverter(), null,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                }
        );
        ageField.setTextFormatter(ageFormatter);

        bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setPromptText("Select blood group");

        cityField = new TextField();
        cityField.setPromptText("Enter city");

        contactField = new TextField();
        contactField.setPromptText("Enter contact (10 digits)");
        TextFormatter<String> contactFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,10}")) {
                return change;
            }
            return null;
        });
        contactField.setTextFormatter(contactFormatter);

        lastDonationPicker = new DatePicker();
        lastDonationPicker.setPromptText("Select last donation date");
        lastDonationPicker.setMaxWidth(Double.MAX_VALUE);

// Add labels and fields to grid
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(new Label("Blood Group:"), 0, 2);
        grid.add(bloodGroupCombo, 1, 2);
        grid.add(new Label("City:"), 0, 3);
        grid.add(cityField, 1, 3);
        grid.add(new Label("Contact:"), 0, 4);
        grid.add(contactField, 1, 4);
        grid.add(new Label("Last Donation:"), 0, 5);
        grid.add(lastDonationPicker, 1, 5);

        getDialogPane().setContent(grid);

// Convert the result to a Donor object when the add button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (validateInputs()) {
                    return new Donor(
                            0, // Database will assign ID
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            bloodGroupCombo.getValue(),
                            cityField.getText(),
                            contactField.getText(),
                            lastDonationPicker.getValue()
                    );
                }
            }
            return null;
        });
    }

    private boolean validateInputs() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String bloodGroup = bloodGroupCombo.getValue();
        String city = cityField.getText();
        String contact = contactField.getText();

        StringBuilder errorMessage = new StringBuilder();

        if (!Validation.isValidName(name)) {
            errorMessage.append("- Invalid name. Only letters and spaces allowed (2–50 characters).\n");
        }

        int age = 0;
        try {
            age = Integer.parseInt(ageText);
            if (!Validation.isValidAge(age)) {
                errorMessage.append("- Invalid age. Must be between 18-65 to donate.\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("- Please enter a valid age.\n");
        }

        if (bloodGroup == null || !Validation.isValidBloodGroup(bloodGroup)) {
            errorMessage.append("- Please select a valid blood group.\n");
        }

        if (!Validation.isValidCity(city)) {
            errorMessage.append("- Invalid city. Only letters and spaces allowed.\n");
        }

        if (!Validation.isValidContact(contact)) {
            errorMessage.append("- Invalid contact. Must be a 10-digit number.\n");
        }

        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }
}