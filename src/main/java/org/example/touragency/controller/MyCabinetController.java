package org.example.touragency.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class MyCabinetController {

    public Button MainFormButton;
    public AnchorPane MyCabinet;
    public Button MainFormButton1;
    public Button SaveOrShowPass;
    public Button SaveChangeButton;
    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    private boolean passwordVisible = false;
    private TextField visiblePasswordField = new TextField();

    private Connection connection;

    @FXML
    private void initialize() {
        connectToDatabase();
        loadClientData();

        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/agency_travel1?useSSL=false&serverTimezone=UTC",
                    "root",
                    "@BaRt7788"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClientData() {
        int clientId = org.example.touragency.controller.Session.getUserId();

        try {
            String query = "SELECT last_name, first_name, middle_name, client_address, сlient_phonee, password FROM client_agency WHERE id_client = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, clientId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lastNameField.setText(rs.getString("last_name"));
                firstNameField.setText(rs.getString("first_name"));
                middleNameField.setText(rs.getString("middle_name"));
                addressField.setText(rs.getString("client_address"));
                phoneField.setText(rs.getString("сlient_phonee"));
                passwordField.setText(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPassword() {
        Pane parentPane = (Pane) passwordField.getParent();

        if (!passwordVisible) {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setLayoutX(passwordField.getLayoutX());
            visiblePasswordField.setLayoutY(passwordField.getLayoutY());
            visiblePasswordField.setPrefWidth(passwordField.getPrefWidth());

            if (!parentPane.getChildren().contains(visiblePasswordField)) {
                parentPane.getChildren().add(visiblePasswordField);
            }

            passwordField.setVisible(false);
            passwordField.setManaged(false);

            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);

            passwordVisible = true;
        } else {
            passwordField.setText(visiblePasswordField.getText());

            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);

            passwordField.setVisible(true);
            passwordField.setManaged(true);

            passwordVisible = false;
        }
    }


    @FXML
    private void SaveChange(ActionEvent actionEvent) {
        saveChanges();
    }

    @FXML
    private void saveChanges() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String middleName = middleNameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String password = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();

        int clientId = org.example.touragency.controller.Session.getUserId();

        try {
            String query = "UPDATE client_agency SET last_name = ?, first_name = ?, middle_name = ?, client_address = ?, сlient_phonee = ?, password = ? WHERE id_client = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            stmt.setString(4, address);
            stmt.setString(5, phone);
            stmt.setString(6, password);
            stmt.setInt(7, clientId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Показати алерт
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успіх");
                alert.setHeaderText(null);
                alert.setContentText("Зміни успішно збережено!");
                alert.showAndWait();

                // Оновити дані на екрані
                loadClientData();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Попередження");
                alert.setHeaderText(null);
                alert.setContentText("Не вдалося зберегти зміни.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText("Помилка збереження");
            alert.setContentText("Сталася помилка при збереженні змін.");
            alert.showAndWait();
        }
    }


    public void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage =(Stage)MyCabinet.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
    }

}

