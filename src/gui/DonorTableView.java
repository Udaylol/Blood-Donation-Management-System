package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import main.Donor;

import java.time.LocalDate;
import java.util.List;

public class DonorTableView extends VBox {
    private TableView<Donor> tableView;
    private ObservableList<Donor> donorData;

    public DonorTableView(List<Donor> donors) {
        this.donorData = FXCollections.observableArrayList(donors);
        setupUI();
    }

    private void setupUI() {
        setPadding(new Insets(10));
        setSpacing(10);

        Label titleLabel = new Label("Donor List");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        tableView = new TableView<>();
        tableView.setEditable(true);

        // Define columns
        TableColumn<Donor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Donor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<Donor, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageColumn.setPrefWidth(50);

        TableColumn<Donor, String> bloodGroupColumn = new TableColumn<>("Blood Group");
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        bloodGroupColumn.setPrefWidth(100);

        TableColumn<Donor, String> cityColumn = new TableColumn<>("City");
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityColumn.setPrefWidth(120);

        TableColumn<Donor, String> contactColumn = new TableColumn<>("Contact");
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactColumn.setPrefWidth(120);

        TableColumn<Donor, LocalDate> lastDonationColumn = new TableColumn<>("Last Donation");
        lastDonationColumn.setCellValueFactory(new PropertyValueFactory<>("lastDonationDate"));
        lastDonationColumn.setPrefWidth(120);
        lastDonationColumn.setCellFactory(column -> {
            return new TableCell<Donor, LocalDate>() {
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText("Never");
                    } else {
                        setText(date.toString());
                    }
                }
            };
        });

        tableView.getColumns().addAll(idColumn, nameColumn, ageColumn, bloodGroupColumn,
                cityColumn, contactColumn, lastDonationColumn);

        // IMPORTANT: Set items to the table before adding to layout
        tableView.setItems(donorData);

        VBox.setVgrow(tableView, Priority.ALWAYS);

        HBox statusBox = new HBox(10);
        Label statusLabel = new Label();
        statusLabel.textProperty().bind(javafx.beans.binding.Bindings.createStringBinding(
                () -> "Total Donors: " + tableView.getItems().size(),
                tableView.getItems()
        ));

        statusBox.getChildren().add(statusLabel);

        getChildren().addAll(titleLabel, tableView, statusBox);

        // Force refresh the table
        tableView.refresh();
    }

    public void setDonors(List<Donor> donors) {
        donorData.clear();
        if (donors != null) {
            donorData.addAll(donors);
        }
        // Force refresh
        tableView.refresh();
        System.out.println("Updated donors list. Size: " + donorData.size());
    }

    public Donor getSelectedDonor() {
        return tableView.getSelectionModel().getSelectedItem();
    }

}