package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import main.Donor;


public class UpdateDonorDialog extends Dialog<Donor> {
    private TextField nameField;
    private TextField ageField;
    private ComboBox<String> bloodGroupCombo;
    private TextField cityField;
    private TextField contactField;
    private DatePicker lastDonationDatePicker;

    public UpdateDonorDialog(Stage owner, Donor donor) {
        setTitle("Update Donor");
        setHeaderText("Edit Donor Details");

        initOwner(owner);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        nameField = new TextField(donor.getName());
        ageField = new TextField(String.valueOf(donor.getAge()));
        bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setValue(donor.getBloodGroup());

        cityField = new TextField(donor.getCity());
        contactField = new TextField(donor.getContact());
        lastDonationDatePicker = new DatePicker(donor.getLastDonationDate());

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
        grid.add(new Label("Last Donation Date:"), 0, 5);
        grid.add(lastDonationDatePicker, 1, 5);

        getDialogPane().setContent(grid);

        // Convert form inputs into a Donor object
        setResultConverter(new Callback<ButtonType, Donor>() {
            @Override
            public Donor call(ButtonType buttonType) {
                if (buttonType == ButtonType.OK) {
                    return new Donor(
                            donor.getId(), // Keep the same ID
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            bloodGroupCombo.getValue(),
                            cityField.getText(),
                            contactField.getText(),
                            lastDonationDatePicker.getValue()
                    );
                }
                return null;
            }
        });
    }
}
