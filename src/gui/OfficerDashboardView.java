package gui;

import dao.*;
import enums.CertificateStatus;
import enums.ComplaintStatus;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AuditLog;
import model.Certificate;
import model.Complaint;
import model.Payment;
import model.User;
import util.AuditLogger;

public class OfficerDashboardView {

    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();
    private final CertificateDAO certificateDAO = new CertificateDAOImpl();
    private final TaxDAO taxDAO = new TaxDAOImpl();
    private final AuditLogDAO auditLogDAO = new AuditLogDAOImpl();

    private User currentUser;

    public void show(Stage stage, User user) {
        this.currentUser = user;

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(
                buildComplaintsTab(),
                buildCertificatesTab(),
                buildPaymentsTab(),
                buildAuditLogTab()
        );

        Label welcome = new Label("Welcome, " + user.getFullName() + " (Officer)");
        welcome.getStyleClass().add("topbar-welcome");

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction(e -> {
            AuditLogger.log(currentUser, "Logged out");
            new LoginView().show(stage);
        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(welcome, spacer, logoutButton);
        topBar.getStyleClass().add("topbar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 950, 650);
        LoginView.applyStyles(scene);
        stage.setTitle("Kasthamandap Municipality - Officer Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ---------------- Complaints Tab ----------------

    private Tab buildComplaintsTab() {
        TableView<Complaint> table = new TableView<>();
        TableColumn<Complaint, Number> idCol = new TableColumn<>("Ref No");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));
        TableColumn<Complaint, String> citizenCol = new TableColumn<>("Citizen");
        citizenCol.setCellValueFactory(new PropertyValueFactory<>("citizenName"));
        TableColumn<Complaint, String> categoryCol = new TableColumn<>("Problem");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Complaint, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(220);
        TableColumn<Complaint, String> wardCol = new TableColumn<>("Ward");
        wardCol.setCellValueFactory(new PropertyValueFactory<>("wardNo"));
        TableColumn<Complaint, String> houseCol = new TableColumn<>("House");
        houseCol.setCellValueFactory(new PropertyValueFactory<>("houseNo"));
        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getColumns().addAll(idCol, citizenCol, categoryCol, descCol, wardCol, houseCol, statusCol);
        table.setItems(FXCollections.observableArrayList(complaintDAO.getAllComplaints()));

        ComboBox<ComplaintStatus> statusBox = new ComboBox<>(FXCollections.observableArrayList(ComplaintStatus.values()));
        statusBox.setPromptText("New Status");

        Button updateButton = new Button("Update Status");
        updateButton.getStyleClass().add("primary-button");
        updateButton.setOnAction(e -> {
            Complaint selected = table.getSelectionModel().getSelectedItem();
            if (selected == null || statusBox.getValue() == null) {
                showAlert("Select a complaint and a new status.");
                return;
            }
            boolean success = complaintDAO.updateComplaintStatus(selected.getId(), statusBox.getValue().name());
            if (success) {
                AuditLogger.log(currentUser, "Updated complaint #" + selected.getId() + " to " + statusBox.getValue().getLabel());
                table.setItems(FXCollections.observableArrayList(complaintDAO.getAllComplaints()));
            } else {
                showAlert("Failed to update status.");
            }
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> table.setItems(FXCollections.observableArrayList(complaintDAO.getAllComplaints())));

        HBox controls = new HBox(10, statusBox, updateButton, refreshButton);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10, 0, 0, 0));

        VBox layout = new VBox(10, table, controls);
        layout.setPadding(new Insets(15));

        return new Tab("Complaints", layout);
    }

    // ---------------- Certificates Tab ----------------

    private Tab buildCertificatesTab() {
        TableView<Certificate> table = new TableView<>();
        TableColumn<Certificate, Number> idCol = new TableColumn<>("App ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getApplicationId()));
        TableColumn<Certificate, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("certificateType"));
        TableColumn<Certificate, String> nameCol = new TableColumn<>("Person");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("personName"));
        TableColumn<Certificate, String> relationCol = new TableColumn<>("Relation");
        relationCol.setCellValueFactory(new PropertyValueFactory<>("relation"));
        TableColumn<Certificate, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getColumns().addAll(idCol, typeCol, nameCol, relationCol, statusCol);
        table.setItems(FXCollections.observableArrayList(certificateDAO.getAllApplications()));

        Button approveButton = new Button("Approve");
        approveButton.getStyleClass().add("primary-button");
        Button rejectButton = new Button("Reject");
        rejectButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 6;");

        approveButton.setOnAction(e -> processCertificate(table, CertificateStatus.APPROVED));
        rejectButton.setOnAction(e -> processCertificate(table, CertificateStatus.REJECTED));

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> table.setItems(FXCollections.observableArrayList(certificateDAO.getAllApplications())));

        HBox controls = new HBox(10, approveButton, rejectButton, refreshButton);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10, 0, 0, 0));

        VBox layout = new VBox(10, table, controls);
        layout.setPadding(new Insets(15));

        return new Tab("Certificate Applications", layout);
    }

    private void processCertificate(TableView<Certificate> table, CertificateStatus newStatus) {
        Certificate selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an application first.");
            return;
        }
        boolean success = certificateDAO.updateCertificateStatus(selected.getApplicationId(), newStatus.name());
        if (success) {
            AuditLogger.log(currentUser, newStatus.getLabel() + " certificate application #" + selected.getApplicationId());
            table.setItems(FXCollections.observableArrayList(certificateDAO.getAllApplications()));
        } else {
            showAlert("Failed to update the application.");
        }
    }

    // ---------------- Payments Tab (view only) ----------------

    private Tab buildPaymentsTab() {
        TableView<Payment> table = new TableView<>();
        TableColumn<Payment, Number> idCol = new TableColumn<>("Receipt No");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPaymentId()));
        TableColumn<Payment, String> citizenCol = new TableColumn<>("Citizen");
        citizenCol.setCellValueFactory(new PropertyValueFactory<>("citizenName"));
        TableColumn<Payment, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        TableColumn<Payment, String> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));
        detailsCol.setPrefWidth(220);
        TableColumn<Payment, Number> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getAmount()));
        TableColumn<Payment, String> gatewayCol = new TableColumn<>("Gateway");
        gatewayCol.setCellValueFactory(new PropertyValueFactory<>("gateway"));
        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        table.getColumns().addAll(idCol, citizenCol, typeCol, detailsCol, amountCol, gatewayCol, statusCol);
        table.setItems(FXCollections.observableArrayList(taxDAO.getAllPayments()));

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> table.setItems(FXCollections.observableArrayList(taxDAO.getAllPayments())));

        VBox layout = new VBox(10, table, refreshButton);
        layout.setPadding(new Insets(15));

        return new Tab("Tax & Fee Payments", layout);
    }

    // ---------------- Audit Log Tab (view only) ----------------

    private Tab buildAuditLogTab() {
        TableView<AuditLog> table = new TableView<>();
        TableColumn<AuditLog, Number> idCol = new TableColumn<>("Log ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getLogId()));
        TableColumn<AuditLog, Number> userCol = new TableColumn<>("User ID");
        userCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getUserId()));
        TableColumn<AuditLog, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        TableColumn<AuditLog, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setPrefWidth(260);
        table.getColumns().addAll(idCol, userCol, roleCol, actionCol);
        table.setItems(FXCollections.observableArrayList(auditLogDAO.getAllLogs()));

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> table.setItems(FXCollections.observableArrayList(auditLogDAO.getAllLogs())));

        VBox layout = new VBox(10, table, refreshButton);
        layout.setPadding(new Insets(15));

        return new Tab("Audit Logs", layout);
    }
}