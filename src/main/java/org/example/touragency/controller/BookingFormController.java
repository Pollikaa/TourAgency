package org.example.touragency.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class BookingFormController {

    public Label tourDaysLabel;
    public Button MainButton;
    @FXML private AnchorPane bookingPane;
    @FXML private Label tourNameLabel;
    @FXML private Label tourCountryLabel;
    @FXML private TextArea tourDescriptionArea;
    @FXML private Spinner<Integer> peopleCountSpinner;
    @FXML private Label totalPriceLabel;
    @FXML private Label tourDurationLabel;
    @FXML private DatePicker startDatePicker;

    private MainFormController.Tour selectedTour;
    private Connection connection;
    private int clientId = -1;

    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        peopleCountSpinner.setValueFactory(valueFactory);

        peopleCountSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());

        startDatePicker.setValue(LocalDate.now().plusDays(1));
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        connectToDatabase();
    }

    public void setSelectedTour(MainFormController.Tour tour) {
        this.selectedTour = tour;
        loadTourData();
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/agency_travel1?useSSL=false&serverTimezone=UTC",
                    "root", "@BaRt7788");
        } catch (Exception e) {
            showAlert("Не вдалося підключитися до БД: " + e.getMessage());
        }
    }

    private void loadTourData() {
        if (selectedTour == null || connection == null) return;

        tourNameLabel.setText("Назва туру: " + selectedTour.getName());
        tourCountryLabel.setText("Країна: " + selectedTour.getCountry());

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT description, days_count FROM tours_travel WHERE name_tours_tr = ?");
            stmt.setString(1, selectedTour.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tourDescriptionArea.setText(rs.getString("description"));
                tourDurationLabel.setText("Тривалість: " + rs.getInt("days_count") + " днів");

            }
        } catch (SQLException e) {
            showAlert("Не вдалося завантажити опис туру: " + e.getMessage());
        }

        updatePrice();
    }

    private void updatePrice() {
        int count = peopleCountSpinner.getValue();
        double total = count * selectedTour.getPrice();
        totalPriceLabel.setText("Загальна ціна: " + total + " доларів $");
    }

    @FXML
    private void confirmBooking() {
        if (connection == null || selectedTour == null) {
            showAlert("Дані туру некоректні.");
            return;
        }

        LocalDate selectedDate = startDatePicker.getValue();
        if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
            showAlert("Оберіть правильну дату початку туру.");
            return;
        }

        try {
            int userId = Session.getUserId(); // Отримати ID користувача

            int peopleCount = peopleCountSpinner.getValue();
            double totalPrice = selectedTour.getPrice() * peopleCount; // Порахувати кінцеву суму

            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO booking (id_client, id_tours, people_count, booking_date, start_date, price_all_count) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");

            stmt.setInt(1, userId);
            stmt.setInt(2, getTourIdByName(selectedTour.getName()));
            stmt.setInt(3, peopleCount);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setDate(5, Date.valueOf(selectedDate));
            stmt.setDouble(6, totalPrice);

            stmt.executeUpdate();

            showAlert("Тур успішно заброньовано!");
        } catch (SQLException e) {
            showAlert("Помилка при збереженні бронювання: " + e.getMessage());
        }
    }


    private int getTourIdByName(String name) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT id_tours FROM tours_travel WHERE name_tours_tr = ?");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_tours");
        }
        throw new SQLException("Не знайдено тур по назві: " + name);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void returnToMainForm(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage =(Stage)bookingPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
    }
}


