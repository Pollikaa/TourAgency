package org.example.touragency.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class MyToursFormController {

    public Button MainFormButton;
    public AnchorPane myTours;
    @FXML
    private TableView<MyTour> myToursTableView;
    @FXML
    private TableColumn<MyTour, String> nameColumn;
    @FXML
    private TableColumn<MyTour, Date> bookingDateColumn;
    @FXML
    private TableColumn<MyTour, Date> startDateColumn;
    @FXML
    private TableColumn<MyTour, Integer> daysColumn;

    private Connection connection;

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage =(Stage)myTours.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
    }

    public static class MyTour {
        private final String tourName;
        private final Date bookingDate;
        private final Date startDate;
        private final int days;

        public MyTour(String tourName, Date bookingDate, Date startDate, int days) {
            this.tourName = tourName;
            this.bookingDate = bookingDate;
            this.startDate = startDate;
            this.days = days;
        }

        public String getTourName() { return tourName; }
        public Date getBookingDate() { return bookingDate; }
        public Date getStartDate() { return startDate; }
        public int getDays() { return days; }
    }

    @FXML
    public void initialize() {
        try {
            connectToDatabase();
        } catch (SQLException e) {
            showAlert("Помилка підключення до БД: " + e.getMessage());
            return;
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tourName"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

        loadMyTours();
        myToursTableView.setRowFactory(tv -> {
            TableRow<MyTour> row = new TableRow<>();
            row.setStyle("-fx-padding: 15px 0;");
            return row;
        });
    }

    private void connectToDatabase() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String DB_URL = "jdbc:mysql://localhost:3306/agency_travel1?useSSL=false&serverTimezone=UTC";
            String DB_USER = "root";
            String DB_PASS = "@BaRt7788";
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("✅ Підключення до БД успішне (MyToursFormController).");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Не знайдено драйвер MySQL.");
        }
    }

    private void loadMyTours() {
        ObservableList<MyTour> tours = FXCollections.observableArrayList();

        int currentClientId = Session.getUserId();

        String query = "SELECT t.name_tours_tr AS tour_name, b.booking_date, b.start_date, t.days_count " +
                "FROM booking b " +
                "JOIN tours_travel t ON b.id_tours = t.id_tours " +
                "WHERE b.id_client = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, currentClientId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tours.add(new MyTour(
                        rs.getString("tour_name"),
                        rs.getDate("booking_date"),
                        rs.getDate("start_date"),
                        rs.getInt("days_count")
                ));
            }

            myToursTableView.setItems(tours);

        } catch (SQLException e) {
            showAlert("Помилка завантаження моїх турів: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, message, javafx.scene.control.ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

