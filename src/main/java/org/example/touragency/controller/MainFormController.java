package org.example.touragency.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class MainFormController {

    public Region topSpacer;
    public Button myToursButton;
    public Button myCabinetButton;
    public Button logoutButton;
    public ChoiceBox<String> countryChoiceBox;
    public Button bookTourButton;
    public TextField maxPriceField;
    public TextField SearchNameField;
    public TableView<Tour> tourTableView;
    public TableColumn<Tour, String> nameColumn;
    public TableColumn<Tour, String> countryColumn;
    public TableColumn<Tour, Integer> daysColumn;
    public TableColumn<Tour, Double> priceColumn;
    public AnchorPane root;

    private Tour selectedTourForBooking;
    private Connection connection;

    public static class Tour {
        private final String name;
        private final String country;
        private final int days;
        private final double price;

        public Tour(String name, String country, int days, double price) {
            this.name = name;
            this.country = country;
            this.days = days;
            this.price = price;
        }

        public String getName() { return name; }
        public String getCountry() { return country; }
        public int getDays() { return days; }
        public double getPrice() { return price; }
    }

    public void initialize() {
        try {
            connectToDatabase();
        } catch (SQLException e) {
            showAlert("Помилка підключення до бази даних: " + e.getMessage());
            return;
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadCountries();
        addListeners();
        loadFilteredTours();

        tourTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedTourForBooking = newSelection;
            bookTourButton.setDisable(newSelection == null);
        });

        bookTourButton.setDisable(true);
    }

    private void connectToDatabase() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Конфігурація бази даних
            String DB_URL = "jdbc:mysql://localhost:3306/agency_travel1?useSSL=false&serverTimezone=UTC";
            String DB_USER = "root";
            String DB_PASS = "@BaRt7788";
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("✅ Підключення до БД успішне.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Не знайдено драйвер MySQL.");
        }
    }

    private void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connectToDatabase();
            }
        } catch (SQLException e) {
            showAlert("Помилка повторного з’єднання з БД: " + e.getMessage());
        }
    }

    private void loadCountries() {
        ensureConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT country FROM tours_travel");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                countryChoiceBox.getItems().add(rs.getString("country"));
            }
        } catch (SQLException e) {
            showAlert("Помилка завантаження країн: " + e.getMessage());
        }
    }

    private void addListeners() {
        countryChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadFilteredTours());
        maxPriceField.textProperty().addListener((obs, oldVal, newVal) -> loadFilteredTours());
        SearchNameField.textProperty().addListener((obs, oldVal, newVal) -> loadFilteredTours());
    }

    private void loadFilteredTours() {
        ensureConnection();
        ObservableList<Tour> tours = FXCollections.observableArrayList();
        String selectedCountry = countryChoiceBox.getValue();
        String maxPriceText = maxPriceField.getText().trim();
        String nameText = SearchNameField.getText().trim();

        StringBuilder query = new StringBuilder("SELECT name_tours_tr AS name, country, days_count AS days, price_per_person AS price FROM tours_travel WHERE 1=1");

        if (selectedCountry != null && !selectedCountry.isEmpty()) {
            query.append(" AND country = ?");
        }
        if (!maxPriceText.isEmpty()) {
            query.append(" AND price_per_person <= ?");
        }
        if (!nameText.isEmpty()) {
            query.append(" AND LOWER(name_tours_tr) LIKE ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;

            if (selectedCountry != null && !selectedCountry.isEmpty()) {
                stmt.setString(index++, selectedCountry);
            }
            if (!maxPriceText.isEmpty()) {
                stmt.setDouble(index++, Double.parseDouble(maxPriceText));
            }
            if (!nameText.isEmpty()) {
                stmt.setString(index, "%" + nameText.toLowerCase() + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tours.add(new Tour(
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getInt("days"),
                        rs.getDouble("price")
                ));
            }

            tourTableView.setItems(tours);

            double parsedMaxPrice = -1;
            try {
                if (!maxPriceText.isEmpty()) {
                    parsedMaxPrice = Double.parseDouble(maxPriceText);
                }
            } catch (NumberFormatException e) {
                showAlert("Некоректна ціна: " + maxPriceText);
            }

            if (tours.isEmpty() && (parsedMaxPrice == -1 || parsedMaxPrice >= 100)) {
                showAlert("Не знайдено турів для вибраних критеріїв.");
            }


        } catch (SQLException e) {
            showAlert("Помилка при завантаженні турів: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Некоректна ціна: " + maxPriceText);
        }
    }

    public void toCabinetForm(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/MyCabinetForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("My Cabinet Form");
        stage.setResizable(false);
        stage.centerOnScreen();
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    public void ToMyToursForm(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/MyToursForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("My Tours Form");
        stage.setResizable(false);
        stage.centerOnScreen();
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    public void logOut(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Session.clear();
    }

    public void bookTour(ActionEvent actionEvent) throws IOException {
        if (selectedTourForBooking != null) {
            System.out.println("Бронювання: " + selectedTourForBooking.getName() + " (" + selectedTourForBooking.getCountry() + ")");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/touragency/BookingForm.fxml"));
            Parent parent = loader.load();

            // Передаємо дані у BookingFormController
            BookingFormController controller = loader.getController();
            controller.setSelectedTour(selectedTourForBooking);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Booking Form");
            stage.setResizable(false);
            stage.centerOnScreen();
            Platform.runLater(() -> scene.getRoot().requestFocus());

        } else {
            showAlert("Виберіть тур для бронювання.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

