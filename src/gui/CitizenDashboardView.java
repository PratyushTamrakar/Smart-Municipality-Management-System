package gui;

import dao.ComplaintDAO;
import dao.ComplaintDAOImpl;
import dao.TaxDAO;
import dao.TaxDAOImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
        logoutBtn.setOnAction(e -> new LoginView(stage).show());

        BorderPane topBar = new BorderPane();
        topBar.setLeft(header);
        topBar.setRight(logoutBtn);
        root.setTop(topBar);

        // Center Content Tabs
        TabPane tabPane = new TabPane();

        // Tab 1: File Complaint
        Tab complaintTab = new Tab("File Complaint", createComplaintView());
        complaintTab.setClosable(false);

        // Tab 2: Pay Municipal Tax
        Tab taxTab = new Tab("Pay Municipal Tax", createTaxView());
        taxTab.setClosable(false);

        tabPane.getTabs().addAll(complaintTab, taxTab);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createComplaintView() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        TextField titleField = new TextField();
        titleField.setPromptText("Complaint Title");

        TextArea descArea = new TextArea();
        descArea.setPromptText("Detailed Description");
        descArea.setPrefRowCount(3);

        TextField wardField = new TextField();
        wardField.setPromptText("Ward Number (e.g., 3)");

        ComboBox<ComplaintCategory> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(ComplaintCategory.values());
        categoryBox.setValue(ComplaintCategory.ROAD);

        Button submitBtn = new Button("Submit Complaint");
        Label msg = new Label();

        submitBtn.setOnAction(e -> {
            try {
                int ward = Integer.parseInt(wardField.getText().trim());
                Complaint c = new Complaint(citizen.getUserId(), categoryBox.getValue(), titleField.getText().trim(), descArea.getText().trim(), ward);
                if (complaintDAO.createComplaint(c)) {
                    msg.setText("✅ Complaint filed successfully!");
                } else {
                    msg.setText("❌ Failed to file complaint.");
                }
            } catch (Exception ex) {
                msg.setText("❌ Please enter a valid Ward Number.");
            }
        });

        layout.getChildren().addAll(new Label("Category:"), categoryBox, titleField, descArea, wardField, submitBtn, msg);
        return layout;
    }

    private VBox createTaxView() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        ComboBox<String> taxTypeBox = new ComboBox<>();
        taxTypeBox.getItems().addAll("Property Tax", "Business Tax", "Vehicle Tax");
        taxTypeBox.setValue("Property Tax");

        TextField valField = new TextField();
        valField.setPromptText("Enter Sq Ft (Property) / Tier 1-3 (Business) / CC (Vehicle)");

        TextField wardField = new TextField();
        wardField.setPromptText("Ward Number (1-10)");

        Button calcBtn = new Button("Calculate & Pay Tax");
        Label statusLbl = new Label();

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
                    statusLbl.setText("✅ Payment Successful! Amount: Rs. " + amount + " (Receipt Exported)");
                }
            } catch (Exception ex) {
                statusLbl.setText("❌ Invalid input parameters!");
            }
        });

        layout.getChildren().addAll(new Label("Select Tax Type:"), taxTypeBox, valField, wardField, calcBtn, statusLbl);
        return layout;
    }
}