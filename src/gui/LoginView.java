package gui;

import dao.AuditLogDAO;
import dao.AuditLogDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.AuditLog;
import model.User;
import model.enums.Role;

public class LoginView {

    private final Stage stage;
    private final UserDAO userDAO = new UserDAOImpl();
    private final AuditLogDAO auditLogDAO = new AuditLogDAOImpl();

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Kasthamandap Municipality - Login");

        // Top Header
        Label headerLabel = new Label("KASTHAMANDAP MUNICIPALITY");
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        headerLabel.setTextFill(Color.web("#1E3A8A"));

        Label subHeaderLabel = new Label("Smart Citizen & Municipal Management System");
        subHeaderLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        subHeaderLabel.setTextFill(Color.GRAY);

        VBox headerBox = new VBox(5, headerLabel, subHeaderLabel);
        headerBox.setAlignment(Pos.CENTER);

        // Form Fields
        Label emailLabel = new Label("Email Address:");
        TextField emailField = new TextField();
        emailField.setPromptText("enter email...");

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("enter password...");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #1E3A8A; -fx-text-fill: white; -fx-font-weight: bold;");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Button registerBtn = new Button("Register New Citizen");
        registerBtn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #1E293B;");
        registerBtn.setMaxWidth(Double.MAX_VALUE);

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));

        // Form Layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(12);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setPadding(new Insets(20));

        formGrid.add(emailLabel, 0, 0);
        formGrid.add(emailField, 1, 0);
        formGrid.add(passLabel, 0, 1);
        formGrid.add(passField, 1, 1);

        VBox buttonBox = new VBox(8, loginBtn, registerBtn, messageLabel);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox mainLayout = new VBox(15, headerBox, formGrid, buttonBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #F8FAFC;");

        // Action Listeners
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                messageLabel.setTextFill(Color.RED);
                return;
            }

            var userOpt = userDAO.login(email, password);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                auditLogDAO.logAction(new AuditLog(user.getUserId(), "JAVAFX_LOGIN", "Logged into Kasthamandap Portal"));

                messageLabel.setText("Login successful! Redirecting...");
                messageLabel.setTextFill(Color.GREEN);

                if (user.getRole() == Role.CITIZEN) {
                    new CitizenDashboardView(stage, user).show();
                } else {
                    new OfficerDashboardView(stage, user).show();
                }
            } else {
                messageLabel.setText("Invalid email or password!");
                messageLabel.setTextFill(Color.RED);
            }
        });

        registerBtn.setOnAction(e -> openRegisterDialog());

        Scene scene = new Scene(mainLayout, 420, 380);
        stage.setScene(scene);
        stage.show();
    }

    private void openRegisterDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Register Citizen - Kasthamandap Municipality");
        dialog.setHeaderText("Create a new Citizen Account");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameF = new TextField();
        nameF.setPromptText("Full Name");
        TextField emailF = new TextField();
        emailF.setPromptText("Email");
        PasswordField passF = new PasswordField();
        passF.setPromptText("Password");

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameF, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailF, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passF, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                User u = new User();
                u.setFullName(nameF.getText().trim());
                u.setEmail(emailF.getText().trim());
                u.setPassword(passF.getText().trim());
                return userDAO.registerUser(u);
            }
            return false;
        });

        dialog.showAndWait().ifPresent(success -> {
            Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle("Registration Status");
            alert.setContentText(success ? "Account created successfully! You can now log in." : "Failed to register account.");
            alert.showAndWait();
        });
    }
}