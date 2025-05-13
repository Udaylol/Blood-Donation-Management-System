package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import Managers.AccountManager;

public class LoginScreen extends BorderPane {
    private MainApp mainApp;
    private AccountManager accountManager;

    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;

    public LoginScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        this.accountManager = new AccountManager();

        setupUI();
    }

    private void setupUI() {
        // Header
        Text headerText = new Text("Blood Donation Management System");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        StackPane header = new StackPane(headerText);
        header.setPadding(new Insets(25));
        header.getStyleClass().add("header");
        setTop(header);

        // Login Form
        VBox loginForm = createLoginForm();
        setCenter(loginForm);

        // Footer
        HBox footer = new HBox();
        footer.setPadding(new Insets(15));
        footer.setAlignment(Pos.CENTER_RIGHT);
        setBottom(footer);
    }

    private VBox createLoginForm() {
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setMaxWidth(400);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab loginTab = new Tab("Login");
        VBox loginContent = new VBox(10);
        loginContent.setPadding(new Insets(20));

        Tab registerTab = new Tab("Register");
        VBox registerContent = new VBox(10);
        registerContent.setPadding(new Insets(20));

        // Login Fields
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(150);
        loginButton.setOnAction(e -> handleLogin());

        // Register Fields
        TextField regUsernameField = new TextField();
        regUsernameField.setPromptText("Choose a username");

        PasswordField regPasswordField = new PasswordField();
        regPasswordField.setPromptText("Choose a password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");

        Button registerButton = new Button("Create Account");
        registerButton.setPrefWidth(150);
        registerButton.setOnAction(e -> {
            if (!regPasswordField.getText().equals(confirmPasswordField.getText())) {
                showAlert("Passwords do not match!");
            } else {
                handleRegister(regUsernameField.getText(), regPasswordField.getText());
            }
        });

        statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");

        loginContent.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                new HBox(10, loginButton)
        );
        loginContent.setAlignment(Pos.CENTER);

        registerContent.getChildren().addAll(
                new Label("Username:"), regUsernameField,
                new Label("Password:"), regPasswordField,
                new Label("Confirm Password:"), confirmPasswordField,
                new HBox(10, registerButton)
        );
        registerContent.setAlignment(Pos.CENTER);

        loginTab.setContent(loginContent);
        registerTab.setContent(registerContent);

        tabPane.getTabs().addAll(loginTab, registerTab);

        loginBox.getChildren().addAll(tabPane, statusLabel);

        return loginBox;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please enter both username and password");
            return;
        }

        boolean success = accountManager.login(username, password);
        if (success) {
            mainApp.showDonorDashboard();
        } else {
            statusLabel.setText("Invalid credentials. Please try again.");
            statusLabel.getStyleClass().add("error-text");
        }
    }

    private void handleRegister(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please enter both username and password");
            return;
        }

        boolean success = accountManager.createAccount(username, password);
        if (success) {
            statusLabel.setText("Account created successfully. You can now login.");
            statusLabel.getStyleClass().remove("error-text");
            statusLabel.getStyleClass().add("success-text");
        } else {
            statusLabel.setText("Username already exists or error occurred.");
            statusLabel.getStyleClass().add("error-text");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}