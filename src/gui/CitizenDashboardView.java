package gui;

import dao.*;
import enums.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Certificate;
import model.Complaint;
import model.Payment;
import model.User;
import util.AuditLogger;
import util.ReceiptGenerator;

public class CitizenDashboardView {

    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();
    private final CertificateDAO certificateDAO = new CertificateDAOImpl();
    private final TaxDAO taxDAO = new TaxDAOImpl();

    private User currentUser;

    public void show(Stage stage, User user) {
        this.currentUser = user;

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(buildComplaintTab(), buildCertificateTab(), buildTaxTab());

        Label welcome = new Label("Welcome, " + user.getFullName());
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

        Scene scene = new Scene(root, 880, 620);
        LoginView.applyStyles(scene);
        stage.setTitle("Kasthamandap Municipality - Citizen Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    // ---------- shared helper ----------

    private HBox labeledRow(String labelText, javafx.scene.Node control) {
        Label label = new Label(labelText);
        label.setPrefWidth(220);
        label.getStyleClass().add("form-label");
        HBox row = new HBox(10, label, control);
        row.setAlignment(Pos.CENTER_LEFT);
        if (control instanceof Region) {
            ((Region) control).setPrefWidth(260);
        }
        return row;
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ---------------- Complaint Tab ----------------

    private Tab buildComplaintTab() {
        Label sectionTitle = new Label("File a Complaint");
        sectionTitle.getStyleClass().add("section-title");

        ComboBox<ComplaintCategory> categoryBox = new ComboBox<>(FXCollections.observableArrayList(ComplaintCategory.values()));
        categoryBox.setPromptText("Select Problem Type");

        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe the problem");
        descArea.setPrefRowCount(4);

        TextField wardField = new TextField();
        wardField.setPromptText("Ward No");
        TextField houseField = new TextField();
        houseField.setPromptText("House No");

        Button submitButton = new Button("Submit Complaint");
        submitButton.getStyleClass().add("primary-button");

        VBox form = new VBox(10,
                labeledRow("Problem:", categoryBox),
                labeledRow("Ward No:", wardField),
                labeledRow("House No:", houseField),
                new Label("Description:"),
                descArea
        );
        form.getStyleClass().add("form-card");

        TableView<Complaint> table = buildComplaintTable();
        refreshComplaints(table);

        submitButton.setOnAction(e -> {
            if (categoryBox.getValue() == null || descArea.getText().trim().isEmpty()
                    || wardField.getText().trim().isEmpty() || houseField.getText().trim().isEmpty()) {
                showAlert("Please fill in the problem type, description, ward no, and house no.");
                return;
            }
            Complaint complaint = new Complaint(
                    currentUser.getId(), currentUser.getFullName(),
                    categoryBox.getValue().getLabel(), descArea.getText().trim(),
                    wardField.getText().trim(), houseField.getText().trim());

            boolean success = complaintDAO.addComplaint(complaint);
            if (success) {
                AuditLogger.log(currentUser, "Filed a " + categoryBox.getValue().getLabel() + " complaint");
                showAlert("Complaint submitted successfully!\nComplaint Reference No: #" + complaint.getId());
                categoryBox.setValue(null);
                descArea.clear();
                wardField.clear();
                houseField.clear();
                refreshComplaints(table);
            } else {
                showAlert("Failed to submit complaint. Please try again.");
            }
        });

        VBox layout = new VBox(15, sectionTitle, form, submitButton, new Separator(), new Label("My Complaints"), table);
        layout.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        return new Tab("File a Complaint", scrollPane);
    }

    private TableView<Complaint> buildComplaintTable() {
        TableView<Complaint> table = new TableView<>();
        TableColumn<Complaint, Number> idCol = new TableColumn<>("Ref No");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));
        TableColumn<Complaint, String> categoryCol = new TableColumn<>("Problem");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Complaint, String> wardCol = new TableColumn<>("Ward");
        wardCol.setCellValueFactory(new PropertyValueFactory<>("wardNo"));
        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getColumns().addAll(idCol, categoryCol, wardCol, statusCol);
        table.setPrefHeight(200);
        return table;
    }

    private void refreshComplaints(TableView<Complaint> table) {
        ObservableList<Complaint> data = FXCollections.observableArrayList(
                complaintDAO.getComplaintsByCitizen(currentUser.getId()));
        table.setItems(data);
    }

    // ---------------- Certificate Tab ----------------

    private Tab buildCertificateTab() {
        Label sectionTitle = new Label("Certificate Registration");
        sectionTitle.getStyleClass().add("section-title");

        ComboBox<CertificateType> typeBox = new ComboBox<>(FXCollections.observableArrayList(CertificateType.values()));
        typeBox.setPromptText("Select Certificate Type");

        TextField personNameField = new TextField();
        personNameField.setPromptText("Person's Full Name");

        TextField relationField = new TextField();
        relationField.setPromptText("Relation to you (e.g. Self, Son, Daughter, Father)");

        TextField citizenshipField = new TextField();
        citizenshipField.setPromptText("Citizenship Number of the deceased");

        TextField hospitalRegField = new TextField();
        hospitalRegField.setPromptText("Hospital Registration Birth Number");

        VBox dynamicFields = new VBox(10);

        Runnable updateFields = () -> {
            dynamicFields.getChildren().clear();
            CertificateType type = typeBox.getValue();
            if (type == CertificateType.BIRTH) {
                dynamicFields.getChildren().add(labeledRow("Hospital Reg. Birth Number:", hospitalRegField));
            } else if (type == CertificateType.DEATH) {
                dynamicFields.getChildren().add(labeledRow("Citizenship Number:", citizenshipField));
            }
        };
        typeBox.valueProperty().addListener((obs, oldV, newV) -> updateFields.run());

        Button submitButton = new Button("Submit Application");
        submitButton.getStyleClass().add("primary-button");

        VBox form = new VBox(10,
                labeledRow("Certificate Type:", typeBox),
                labeledRow("Person's Name:", personNameField),
                labeledRow("Relation to you:", relationField),
                dynamicFields
        );
        form.getStyleClass().add("form-card");

        TableView<Certificate> table = buildCertificateTable();
        refreshCertificates(table);

        submitButton.setOnAction(e -> {
            CertificateType type = typeBox.getValue();
            if (type == null || personNameField.getText().trim().isEmpty() || relationField.getText().trim().isEmpty()) {
                showAlert("Please select the certificate type and fill in the person's name and relation.");
                return;
            }
            if (type == CertificateType.DEATH && citizenshipField.getText().trim().isEmpty()) {
                showAlert("Please enter the citizenship number of the deceased.");
                return;
            }
            if (type == CertificateType.BIRTH && hospitalRegField.getText().trim().isEmpty()) {
                showAlert("Please enter the hospital registration birth number.");
                return;
            }

            Certificate cert = new Certificate();
            cert.setCitizenId(currentUser.getId());
            cert.setCertificateType(type.getLabel());
            cert.setPersonName(personNameField.getText().trim());
            cert.setRelation(relationField.getText().trim());
            if (type == CertificateType.DEATH) {
                cert.setCitizenshipNumber(citizenshipField.getText().trim());
            } else {
                cert.setHospitalRegNumber(hospitalRegField.getText().trim());
            }

            boolean success = certificateDAO.applyCertificate(cert);
            if (success) {
                AuditLogger.log(currentUser, "Applied for " + type.getLabel());
                showAlert("Application submitted successfully!\nApplication ID: #" + cert.getApplicationId());
                typeBox.setValue(null);
                personNameField.clear();
                relationField.clear();
                citizenshipField.clear();
                hospitalRegField.clear();
                dynamicFields.getChildren().clear();
                refreshCertificates(table);
            } else {
                showAlert("Failed to submit application. Please try again.");
            }
        });

        VBox layout = new VBox(15, sectionTitle, form, submitButton, new Separator(), new Label("My Applications"), table);
        layout.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        return new Tab("Certificate Registration", scrollPane);
    }

    private TableView<Certificate> buildCertificateTable() {
        TableView<Certificate> table = new TableView<>();
        TableColumn<Certificate, Number> idCol = new TableColumn<>("App ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getApplicationId()));
        TableColumn<Certificate, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("certificateType"));
        TableColumn<Certificate, String> nameCol = new TableColumn<>("Person");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("personName"));
        TableColumn<Certificate, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getColumns().addAll(idCol, typeCol, nameCol, statusCol);
        table.setPrefHeight(200);
        return table;
    }

    private void refreshCertificates(TableView<Certificate> table) {
        ObservableList<Certificate> data = FXCollections.observableArrayList(
                certificateDAO.getApplicationsByCitizen(currentUser.getId()));
        table.setItems(data);
    }

    // ---------------- Tax & Fee Payment Tab ----------------

    private double currentAmount = 0;

    private Tab buildTaxTab() {
        Label sectionTitle = new Label("Tax & Fee Payment");
        sectionTitle.getStyleClass().add("section-title");

        ComboBox<PaymentType> typeBox = new ComboBox<>(FXCollections.observableArrayList(PaymentType.values()));
        typeBox.setPromptText("Select Tax / Fee Type");

        ComboBox<PaymentGateway> gatewayBox = new ComboBox<>(FXCollections.observableArrayList(PaymentGateway.values()));
        gatewayBox.setPromptText("Select Payment Gateway");

        // Waste Management Fee fields
        TextField wardField = new TextField();
        wardField.setPromptText("Ward No");
        TextField houseField = new TextField();
        houseField.setPromptText("House No");
        TextField monthsField = new TextField();
        monthsField.setPromptText("Number of Months");

        // Vehicle Tax field
        TextField ccField = new TextField();
        ccField.setPromptText("Vehicle CC");

        // Business Tax fields
        TextField businessTypeField = new TextField();
        businessTypeField.setPromptText("Type of Business (e.g. Retail Shop, Restaurant)");
        ComboBox<BusinessTier> tierBox = new ComboBox<>(FXCollections.observableArrayList(BusinessTier.values()));
        tierBox.setPromptText("Business Tier");

        // Property Tax fields
        TextField propertyWardField = new TextField();
        propertyWardField.setPromptText("Ward No");
        TextField propertyValueField = new TextField();
        propertyValueField.setPromptText("Declared Property Value (Rs.)");

        VBox dynamicFields = new VBox(10);
        Label amountLabel = new Label("Amount Due: Rs. 0.00");
        amountLabel.getStyleClass().add("amount-label");

        Runnable[] recalcHolder = new Runnable[1];

        Runnable updateFields = () -> {
            dynamicFields.getChildren().clear();
            PaymentType type = typeBox.getValue();
            if (type == null) {
                return;
            }
            switch (type) {
                case WASTE_MANAGEMENT_FEE:
                    dynamicFields.getChildren().addAll(
                            labeledRow("Ward No:", wardField),
                            labeledRow("House No:", houseField),
                            labeledRow("Number of Months:", monthsField));
                    break;
                case VEHICLE_TAX:
                    dynamicFields.getChildren().add(labeledRow("Vehicle CC:", ccField));
                    break;
                case BUSINESS_TAX:
                    dynamicFields.getChildren().addAll(
                            labeledRow("Business Type:", businessTypeField),
                            labeledRow("Business Tier:", tierBox));
                    break;
                case PROPERTY_TAX:
                    dynamicFields.getChildren().addAll(
                            labeledRow("Ward No:", propertyWardField),
                            labeledRow("Declared Property Value (Rs.):", propertyValueField));
                    break;
            }
            recalcHolder[0].run();
        };

        Runnable recalc = () -> {
            PaymentType type = typeBox.getValue();
            double amount = 0;
            if (type != null) {
                switch (type) {
                    case WASTE_MANAGEMENT_FEE:
                        // Rate: Rs. 250 per month (PLACEHOLDER — edit to your official rate)
                        amount = 250.0 * parseIntSafe(monthsField.getText());
                        break;
                    case VEHICLE_TAX:
                        amount = calculateVehicleTax(parseIntSafe(ccField.getText()));
                        break;
                    case BUSINESS_TAX:
                        amount = tierBox.getValue() != null ? tierBox.getValue().getAnnualTax() : 0;
                        break;
                    case PROPERTY_TAX:
                        // Rate: 0.5% of declared property value (PLACEHOLDER — edit to your official rate)
                        amount = parseDoubleSafe(propertyValueField.getText()) * 0.005;
                        break;
                }
            }
            currentAmount = amount;
            amountLabel.setText(String.format("Amount Due: Rs. %.2f", amount));
        };
        recalcHolder[0] = recalc;

        typeBox.valueProperty().addListener((obs, o, n) -> updateFields.run());
        monthsField.textProperty().addListener((obs, o, n) -> recalc.run());
        ccField.textProperty().addListener((obs, o, n) -> recalc.run());
        tierBox.valueProperty().addListener((obs, o, n) -> recalc.run());
        propertyValueField.textProperty().addListener((obs, o, n) -> recalc.run());

        Button payButton = new Button("Pay Now");
        payButton.getStyleClass().add("primary-button");

        VBox form = new VBox(10,
                labeledRow("Tax / Fee Type:", typeBox),
                dynamicFields,
                labeledRow("Payment Gateway:", gatewayBox),
                amountLabel
        );
        form.getStyleClass().add("form-card");

        TableView<Payment> table = buildPaymentTable();
        refreshPayments(table);

        payButton.setOnAction(e -> {
            PaymentType type = typeBox.getValue();
            PaymentGateway gateway = gatewayBox.getValue();
            if (type == null || gateway == null) {
                showAlert("Please select a tax/fee type and a payment gateway.");
                return;
            }
            recalc.run();
            if (currentAmount <= 0) {
                showAlert("Please fill in the required details so the amount can be calculated.");
                return;
            }

            Payment payment = new Payment();
            payment.setCitizenId(currentUser.getId());
            payment.setCitizenName(currentUser.getFullName());
            payment.setPaymentType(type.getLabel());
            payment.setGateway(gateway.getLabel());
            payment.setAmount(currentAmount);

            switch (type) {
                case WASTE_MANAGEMENT_FEE:
                    if (wardField.getText().trim().isEmpty() || houseField.getText().trim().isEmpty()
                            || monthsField.getText().trim().isEmpty()) {
                        showAlert("Please fill in ward no, house no, and number of months.");
                        return;
                    }
                    payment.setWardNo(wardField.getText().trim());
                    payment.setHouseNo(houseField.getText().trim());
                    int months = parseIntSafe(monthsField.getText());
                    payment.setMonths(months);
                    payment.setDetails("Waste Management Fee for " + months + " month(s) - Ward "
                            + wardField.getText().trim() + ", House " + houseField.getText().trim());
                    break;
                case VEHICLE_TAX:
                    if (ccField.getText().trim().isEmpty()) {
                        showAlert("Please enter the vehicle CC.");
                        return;
                    }
                    int cc = parseIntSafe(ccField.getText());
                    payment.setVehicleCC(cc);
                    payment.setDetails("Vehicle Tax for a " + cc + "cc vehicle");
                    break;
                case BUSINESS_TAX:
                    if (businessTypeField.getText().trim().isEmpty() || tierBox.getValue() == null) {
                        showAlert("Please enter the business type and select a tier.");
                        return;
                    }
                    payment.setBusinessType(businessTypeField.getText().trim());
                    payment.setBusinessTier(tierBox.getValue().getLabel());
                    payment.setDetails("Business Tax - " + businessTypeField.getText().trim()
                            + " (" + tierBox.getValue().getLabel() + ")");
                    break;
                case PROPERTY_TAX:
                    if (propertyWardField.getText().trim().isEmpty() || propertyValueField.getText().trim().isEmpty()) {
                        showAlert("Please enter the ward no and declared property value.");
                        return;
                    }
                    payment.setWardNo(propertyWardField.getText().trim());
                    double value = parseDoubleSafe(propertyValueField.getText());
                    payment.setPropertyValue(value);
                    payment.setDetails("Property Tax - Declared Value Rs. " + value
                            + ", Ward " + propertyWardField.getText().trim());
                    break;
            }

            boolean success = taxDAO.makePayment(payment);
            if (success) {
                AuditLogger.log(currentUser, "Paid " + type.getLabel() + " - Rs. "
                        + String.format("%.2f", payment.getAmount()));
                String receiptPath = ReceiptGenerator.generateReceipt(payment);
                showAlert("Payment successful!\nReceipt No: " + payment.getPaymentId()
                        + "\nAmount Paid: Rs. " + String.format("%.2f", payment.getAmount())
                        + "\nReceipt saved to: " + receiptPath);
                typeBox.setValue(null);
                gatewayBox.setValue(null);
                dynamicFields.getChildren().clear();
                amountLabel.setText("Amount Due: Rs. 0.00");
                refreshPayments(table);
            } else {
                showAlert("Payment failed. Please try again.");
            }
        });

        VBox layout = new VBox(15, sectionTitle, form, payButton, new Separator(), new Label("My Payments"), table);
        layout.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        return new Tab("Tax & Fee Payment", scrollPane);
    }

    /**
     * PLACEHOLDER slab rates — replace with the municipality's actual vehicle tax schedule.
     */
    private double calculateVehicleTax(int cc) {
        if (cc <= 0) return 0;
        if (cc <= 125) return 3000;
        if (cc <= 250) return 4500;
        if (cc <= 400) return 6500;
        if (cc <= 650) return 9000;
        return 12000;
    }

    private TableView<Payment> buildPaymentTable() {
        TableView<Payment> table = new TableView<>();
        TableColumn<Payment, Number> idCol = new TableColumn<>("Receipt No");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPaymentId()));
        TableColumn<Payment, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        TableColumn<Payment, Number> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getAmount()));
        TableColumn<Payment, String> gatewayCol = new TableColumn<>("Gateway");
        gatewayCol.setCellValueFactory(new PropertyValueFactory<>("gateway"));
        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        table.getColumns().addAll(idCol, typeCol, amountCol, gatewayCol, statusCol);
        table.setPrefHeight(200);
        return table;
    }

    private void refreshPayments(TableView<Payment> table) {
        ObservableList<Payment> data = FXCollections.observableArrayList(
                taxDAO.getPaymentsByCitizen(currentUser.getId()));
        table.setItems(data);
    }
}