package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import helper.BloodCompatibility;

import java.util.ArrayList;

public class BloodCompatibilityView extends Dialog<Void> {

    public BloodCompatibilityView(Stage owner) {
        setTitle("Blood Compatibility Chart");
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(closeButton);

        VBox mainBox = new VBox(15);
        mainBox.setPadding(new Insets(20));
        mainBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Blood Compatibility");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox selectionBox = new HBox(10);
        selectionBox.setAlignment(Pos.CENTER);

        Label selectLabel = new Label("Select Blood Group: ");
        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setPromptText("Select blood group");

        Button checkButton = new Button("Check Compatibility");

        selectionBox.getChildren().addAll(selectLabel, bloodGroupCombo, checkButton);

        GridPane resultGrid = new GridPane();
        resultGrid.setHgap(10);
        resultGrid.setVgap(10);
        resultGrid.setPadding(new Insets(10));
        resultGrid.setAlignment(Pos.CENTER);

        Label canDonateLabel = new Label("Can Donate To:");
        Label canReceiveLabel = new Label("Can Receive From:");

        TextArea canDonateText = new TextArea();
        canDonateText.setPrefRowCount(2);
        canDonateText.setEditable(false);
        canDonateText.setWrapText(true);

        TextArea canReceiveText = new TextArea();
        canReceiveText.setPrefRowCount(2);
        canReceiveText.setEditable(false);
        canReceiveText.setWrapText(true);

        resultGrid.add(canDonateLabel, 0, 0);
        resultGrid.add(canDonateText, 0, 1);
        resultGrid.add(canReceiveLabel, 0, 2);
        resultGrid.add(canReceiveText, 0, 3);

        // Add compatibility chart table
        TableView<BloodCompatibilityRow> compatibilityTable = createCompatibilityTable();

        checkButton.setOnAction(e -> {
            String bloodGroup = bloodGroupCombo.getValue();
            if (bloodGroup != null) {
                ArrayList<String> canDonateTo = BloodCompatibility.canDonateTo.get(bloodGroup);
                ArrayList<String> canReceiveFrom = BloodCompatibility.canReceiveFrom.get(bloodGroup);

                canDonateText.setText(String.join(", ", canDonateTo));
                canReceiveText.setText(String.join(", ", canReceiveFrom));
            }
        });

        mainBox.getChildren().addAll(titleLabel, selectionBox, resultGrid, compatibilityTable);

        getDialogPane().setContent(mainBox);
    }

    private TableView<BloodCompatibilityRow> createCompatibilityTable() {
        TableView<BloodCompatibilityRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BloodCompatibilityRow, String> bloodGroupCol = new TableColumn<>("Blood Group");
        bloodGroupCol.setCellValueFactory(cellData -> cellData.getValue().bloodGroupProperty());

        TableColumn<BloodCompatibilityRow, String> canDonateToCol = new TableColumn<>("Can Donate To");
        canDonateToCol.setCellValueFactory(cellData -> cellData.getValue().canDonateToProperty());

        TableColumn<BloodCompatibilityRow, String> canReceiveFromCol = new TableColumn<>("Can Receive From");
        canReceiveFromCol.setCellValueFactory(cellData -> cellData.getValue().canReceiveFromProperty());

        table.getColumns().addAll(bloodGroupCol, canDonateToCol, canReceiveFromCol);

        // Add rows
        BloodCompatibility.canDonateTo.forEach((bloodGroup, canDonateTo) -> {
            table.getItems().add(new BloodCompatibilityRow(
                    bloodGroup,
                    String.join(", ", canDonateTo),
                    String.join(", ", BloodCompatibility.canReceiveFrom.get(bloodGroup))
            ));
        });

        return table;
    }

    private static class BloodCompatibilityRow {
        private final javafx.beans.property.SimpleStringProperty bloodGroup;
        private final javafx.beans.property.SimpleStringProperty canDonateTo;
        private final javafx.beans.property.SimpleStringProperty canReceiveFrom;

        public BloodCompatibilityRow(String bloodGroup, String canDonateTo, String canReceiveFrom) {
            this.bloodGroup = new javafx.beans.property.SimpleStringProperty(bloodGroup);
            this.canDonateTo = new javafx.beans.property.SimpleStringProperty(canDonateTo);
            this.canReceiveFrom = new javafx.beans.property.SimpleStringProperty(canReceiveFrom);
        }

        public javafx.beans.property.StringProperty bloodGroupProperty() {
            return bloodGroup;
        }

        public javafx.beans.property.StringProperty canDonateToProperty() {
            return canDonateTo;
        }

        public javafx.beans.property.StringProperty canReceiveFromProperty() {
            return canReceiveFrom;
        }
    }
}
