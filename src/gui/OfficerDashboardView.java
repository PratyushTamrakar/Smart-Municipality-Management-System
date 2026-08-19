package gui;

import dao.ComplaintDAO;
import dao.ComplaintDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Complaint;
import model.User;
import model.enums.ComplaintStatus;

public class OfficerDashboardView {

    private final Stage stage;
    private final User officer;
    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();
    private final ObservableList<Complaint> complaintList = FXCollections.observableArrayList();

    public OfficerDashboardView(Stage stage, User officer) {
        this.stage = stage;
        this.officer = officer;
    }

    public void show() {
        stage.setTitle("Kasthamandap Municipality - Officer Admin Portal");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Header
        Label header = new Label("Officer Admin Panel | Logged in as: " + officer.getFullName());
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> new LoginView(stage).show());

        BorderPane topBar = new BorderPane();
        topBar.setLeft(header);
        topBar.setRight(logoutBtn);
        root.setTop(topBar);

        // Center Table View
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(15, 0, 0, 0));

        TableView<Complaint> table = new TableView<>();

        TableColumn<Complaint, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("complaintId"));

        TableColumn<Complaint, Integer> wardCol = new TableColumn<>("Ward");
        wardCol.setCellValueFactory(new PropertyValueFactory<>("wardNumber"));

        TableColumn<Complaint, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Complaint, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, wardCol, catCol, titleCol, statusCol);
        loadComplaints();
        table.setItems(complaintList);

        // Interactive Controls
        ComboBox<ComplaintStatus> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(ComplaintStatus.values());
        statusBox.setValue(ComplaintStatus.IN_PROGRESS);

        Button updateStatusBtn = new Button("Update Selected Complaint Status");
        updateStatusBtn.setStyle("-fx-background-color: #1E3A8A; -fx-text-fill: white; -fx-font-weight: bold;");

        Label msg = new Label();

        updateStatusBtn.setOnAction(e -> {
            Complaint selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (complaintDAO.updateComplaintStatus(selected.getComplaintId(), statusBox.getValue())) {
                    msg.setText("✅ Updated Complaint #" + selected.getComplaintId() + " to " + statusBox.getValue());
                    loadComplaints(); // Refresh table immediately
                }
            } else {
                msg.setText("⚠️ Please select a complaint from the table first!");
            }
        });

        HBox actionBox = new HBox(10, new Label("Set Status:"), statusBox, updateStatusBtn);
        centerBox.getChildren().addAll(new Label("Active Municipal Complaints:"), table, actionBox, msg);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 720, 480);
        stage.setScene(scene);
        stage.show();
    }

    private void loadComplaints() {
        complaintList.clear();
        complaintList.addAll(complaintDAO.getAllComplaints());
    }
}