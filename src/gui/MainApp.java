package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Blood Donation Management System");
        showLoginScreen();
    }

    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(this);
        Scene scene = new Scene(loginScreen, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showDonorDashboard() {
        DonorDashboard dashboard = new DonorDashboard(this);
        Scene scene = new Scene(dashboard, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
