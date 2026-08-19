package gui;

import dao.ComplaintDAO;
import dao.ComplaintDAOImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Complaint;
import model.User;

import java.util.List;

public class OfficerDashboardView {

    private final Stage stage;
    private final User officer;
    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();

    public OfficerDashboardView(Stage stage, User officer) {
        this.stage = stage;
        this.officer = officer;
    }

    public void show() {
        stage.setTitle("Kasthamandap Municipality - Officer Portal");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Header
        Label header = new Label("Kasthamandap Admin Panel | Officer: " + officer.getFullName());
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> new LoginView(stage).show());

        BorderPane topBar = new BorderPane();
        topBar.setLeft(header);
        topBar.setRight(logoutBtn);
        root.setTop(topBar);

        // Center Content - Complaints ListView
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(15, 0, 0, 0));

        ListView<String> complaintListView = new ListView<>();
        List<Complaint> complaints = complaintDAO.getAllComplaints();

        for (Complaint c : complaints) {
            complaintListView.getItems().add("ID: " + c.getComplaintId() + " | Ward: " + c.getWardNumber() + " | Category: " + c.getCategory() + " | Title: " + c.getTitle() + " | Status: " + c.getStatus());
        }

        centerBox.getChildren().addAll(new Label("Registered Municipal Complaints:"), complaintListView);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 650, 400);
        stage.setScene(scene);
        stage.show();
    }
}