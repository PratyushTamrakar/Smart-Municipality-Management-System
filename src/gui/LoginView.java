package gui;

import dao.UserDAO;
import dao.UserDAOImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import util.AuditLogger;

public class LoginView {

    private final UserDAO userDAO = new UserDAOImpl();

    public void show(Stage stage) {
        Label titleLabel = new Label("Kasthamandap Municipality");
        titleLabel.getStyleClass().add("header-title");

        Label subtitleLabel = new Label("Smart Municipality Management System");
        subtitleLabel.getStyleClass().add("header-subtitle");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setDefaultButton(true);
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Button registerButton = new Button("New citizen? Register here");
        registerButton.getStyleClass().add("secondary-button");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #c0392b;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.setAlignment(Pos.CENTER);
        form.add(new Label("Email:"), 0, 0);
        form.add(emailField, 1, 0);
        form.add(new Label("Password:"), 0, 1);
        form.add(passwordField, 1, 1);

        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password.");
                return;
            }

            User user = userDAO.authenticate(email, password);
            if (user == null) {
                statusLabel.setText("Invalid credentials. Please try again.");
                return;
            }

            AuditLogger.log(user, "Logged in");

            if (user.isOfficer()) {
                new OfficerDashboardView().show(stage, user);
            } else {
                new CitizenDashboardView().show(stage, user);
            }
        });

        registerButton.setOnAction(e -> showRegisterDialog());

        VBox layout = new VBox(16, titleLabel, subtitleLabel, form, loginButton, registerButton, statusLabel);
        layout.getStyleClass().add("login-card");
        layout.setAlignment(Pos.CENTER);

        VBox pageWrapper = new VBox(layout);
        pageWrapper.setAlignment(Pos.CENTER);
        pageWrapper.setPadding(new Insets(40));

        Scene scene = new Scene(pageWrapper, 500, 460);
        applyStyles(scene);
        stage.setTitle("Kasthamandap Municipality - Login");
        stage.setScene(scene);
        stage.show();
    }

    private void showRegisterDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Citizen Registration");
        dialog.setHeaderText("Create a new citizen account");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == registerButtonType) {
                User newUser = new User(nameField.getText().trim(), emailField.getText().trim(),
                        passwordField.getText(), "CITIZEN");
                boolean success = userDAO.registerUser(newUser);
                Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText(success ? "Registration successful. You can now log in."
                        : "Registration failed. The email may already be in use.");
                alert.showAndWait();
            }
            return null;
        });

        dialog.showAndWait();
    }

    static void applyStyles(Scene scene) {
        if (LoginView.class.getResource("/style.css") != null) {
            scene.getStylesheets().add(LoginView.class.getResource("/style.css").toExternalForm());
        }
    }
}