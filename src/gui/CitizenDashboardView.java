package gui;

import dao.ComplaintDAO;
import dao.ComplaintDAOImpl;
import dao.TaxDAO;
import dao.TaxDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Complaint;
import model.TaxPayment;
import model.User;
import model.enums.ComplaintCategory;
import util.FileExporter;
import util.TaxCalculatorService;

public class CitizenDashboardView {

    private final Stage stage;
    private final User citizen;
    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();
    private final TaxDAO taxDAO = new TaxDAOImpl();
    private final ObservableList<TaxPayment> taxHistoryList = FXCollections.observableArrayList();

    public CitizenDashboardView(Stage stage, User citizen) {
        this.stage = stage;
        this.citizen = citizen;
    }

    public void show() {
        stage.setTitle("Kasthamandap Municipality - Citizen Portal");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Header
        Label header = new Label("Welcome, " + citizen.getFullName() + " (Citizen)");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> new LoginView(stage).show());

        BorderPane topBar = new BorderPane();
        topBar.setLeft(header);
        topBar.setRight(logoutBtn);
        root.setTop(topBar);

        // Center Content Tabs
        TabPane tabPane = new TabPane();

        Tab complaintTab = new Tab("File Complaint", createComplaintView());
        complaintTab.setClosable(false);

        Tab taxTab = new Tab("Pay Tax & Receipts", createTaxView());
        taxTab.setClosable(false);

        tabPane.getTabs().addAll(complaintTab, taxTab);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 700, 520);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createComplaintView() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        TextField titleField = new TextField();
        titleField.setPromptText("Complaint Title");

        TextArea descArea = new TextArea();
        descArea.setPromptText("Detailed Description of the Issue");
        descArea.setPrefRowCount(3);

        TextField wardField = new TextField();
        wardField.setPromptText("Ward Number (e.g., 3)");

        ComboBox<ComplaintCategory> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(ComplaintCategory.values());
        categoryBox.setValue(ComplaintCategory.ROAD);

        Button submitBtn = new Button("Submit Municipal Complaint");
        submitBtn.setStyle("-fx-background-color: #1E3A8A; -fx-text-fill: white; -fx-font-weight: bold;");

        Label msg = new Label();
        msg.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        submitBtn.setOnAction(e -> {
            try {
                int ward = Integer.parseInt(wardField.getText().trim());
                Complaint c = new Complaint(citizen.getUserId(), categoryBox.getValue(), titleField.getText().trim(), descArea.getText().trim(), ward);
                if (complaintDAO.createComplaint(c)) {
                    msg.setText("✅ Complaint registered! Municipal officers will review it shortly.");
                    msg.setTextFill(Color.GREEN);
                    titleField.clear();
                    descArea.clear();
                    wardField.clear();
                } else {
                    msg.setText("❌ Failed to file complaint.");
                    msg.setTextFill(Color.RED);
                }
            } catch (Exception ex) {
                msg.setText("❌ Please enter a valid numerical Ward Number.");
                msg.setTextFill(Color.RED);
            }
        });

        layout.getChildren().addAll(new Label("Select Category:"), categoryBox, titleField, descArea, wardField, submitBtn, msg);
        return layout;
    }

    private VBox createTaxView() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        ComboBox<String> taxTypeBox = new ComboBox<>();
        taxTypeBox.getItems().addAll("Property Tax", "Business Tax", "Vehicle Tax");
        taxTypeBox.setValue("Property Tax");

        TextField valField = new TextField();
        valField.setPromptText("Sq Ft (Property) / Tier 1-3 (Business) / CC (Vehicle)");

        TextField wardField = new TextField();
        wardField.setPromptText("Ward Number (1-10)");

        Button calcBtn = new Button("Calculate & Pay Tax");
        calcBtn.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-font-weight: bold;");
        Label statusLbl = new Label();

        // Interactive Table View for Real-time History
        TableView<TaxPayment> table = new TableView<>();
        TableColumn<TaxPayment, String> typeCol = new TableColumn<>("Tax Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("taxType"));

        TableColumn<TaxPayment, Double> amountCol = new TableColumn<>("Amount Paid (NPR)");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.getColumns().addAll(typeCol, amountCol);
        table.setItems(taxHistoryList);
        table.setPrefHeight(150);

        calcBtn.setOnAction(e -> {
            try {
                double amount = 0;
                String selected = taxTypeBox.getValue();
                int val = Integer.parseInt(valField.getText().trim());

                if ("Property Tax".equals(selected)) {
                    int ward = Integer.parseInt(wardField.getText().trim());
                    amount = TaxCalculatorService.calculatePropertyTax(val, ward);
                } else if ("Business Tax".equals(selected)) {
                    amount = TaxCalculatorService.calculateBusinessTax(val);
                } else {
                    amount = TaxCalculatorService.calculateVehicleTax(val);
                }

                TaxPayment payment = new TaxPayment(citizen.getUserId(), selected.toUpperCase().replace(" ", "_"), amount);
                if (taxDAO.payTax(payment)) {
                    FileExporter.exportTaxReceipt(payment, citizen);
                    taxHistoryList.add(payment); // Instantly updates the interactive table!
                    statusLbl.setText("✅ Paid NPR " + amount + " successfully! Receipt exported to /exports");
                    statusLbl.setTextFill(Color.GREEN);
                    valField.clear();
                    wardField.clear();
                }
            } catch (Exception ex) {
                statusLbl.setText("❌ Invalid parameters! Check inputs.");
                statusLbl.setTextFill(Color.RED);
            }
        });

        layout.getChildren().addAll(new Label("Select Tax Category:"), taxTypeBox, valField, wardField, calcBtn, statusLbl, new Label("Payment Activity History:"), table);
        return layout;
    }
}