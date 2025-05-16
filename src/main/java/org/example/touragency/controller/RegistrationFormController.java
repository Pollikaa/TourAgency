package org.example.touragency.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.touragency.db.DataBase;

import java.sql.Connection;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationFormController {
    public PasswordField ComfirmPassword;
    public Button Registration;
    public TextField txtUserSecondName;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public TextField txtThirdName;
    public TextField HomeAdress;
    public TextField PhoneNumber;
    public AnchorPane pane;
    public Label lblPassword1;
    public Label lblPassword2;
    public TextField txtPasswordVisible;
    public CheckBox cbShowPassword;
    public CheckBox cbShowConfirmPassword;
    public TextField ComfirmPasswordVisible;


    public void initialize() {
        lblPassword1.setVisible(false);
        lblPassword2.setVisible(false);
        txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());

        // Логіка показу/приховування
        cbShowPassword.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                txtPasswordVisible.setVisible(true);
                txtPasswordVisible.setManaged(true);
                txtPassword.setVisible(false);
                txtPassword.setManaged(false);
            } else {
                txtPasswordVisible.setVisible(false);
                txtPasswordVisible.setManaged(false);
                txtPassword.setVisible(true);
                txtPassword.setManaged(true);
            }
        });
        ComfirmPasswordVisible.textProperty().bindBidirectional(ComfirmPassword.textProperty());

// Логіка перемикання
        cbShowConfirmPassword.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                ComfirmPassword.setVisible(false);
                ComfirmPassword.setManaged(false);
                ComfirmPasswordVisible.setVisible(true);
                ComfirmPasswordVisible.setManaged(true);
            } else {
                ComfirmPasswordVisible.setVisible(false);
                ComfirmPasswordVisible.setManaged(false);
                ComfirmPassword.setVisible(true);
                ComfirmPassword.setManaged(true);
            }
        });
    }

    public void btnSignup(ActionEvent actionEvent) {
        register();

    }

    public void ComfirmPasswordOnAction(ActionEvent actionEvent) {
        register();
    }

    public void register() {
        String lastName = txtUserSecondName.getText().trim();
        String firstName = txtUserName.getText().trim();
        String middleName = txtThirdName.getText().trim();
        String address = HomeAdress.getText().trim();
        String phone = PhoneNumber.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = ComfirmPassword.getText();

        // Перевірка на пусті поля
        if (lastName.isEmpty() || firstName.isEmpty() || middleName.isEmpty()
                || address.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Помилка", "Будь ласка, заповніть усі поля.");
            return;
        }

        // Перевірка формату номера телефону: +380XXXXXXXXX
        if (!Pattern.matches("\\+380\\d{9}", phone)) {
            showAlert("Невірний формат телефону", "Номер телефону має бути у форматі +380XXXXXXXXX.");
            PhoneNumber.requestFocus();
            return;
        }

        // Перевірка відповідності паролів
        if (!password.equals(confirmPassword)) {
            setBorderColor("error");
            lblPassword1.setVisible(true);
            lblPassword2.setVisible(true);
            ComfirmPassword.requestFocus();
            return;
        }

        setBorderColor("normal");
        lblPassword1.setVisible(false);
        lblPassword2.setVisible(false);

        Connection connection = DataBase.getInstance().getConnection();

        String insertQuery = "INSERT INTO `client_agency` (last_name, first_name, middle_name, client_address, сlient_phonee, password) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            var preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, middleName);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, password); // Можна замінити на хешований пароль

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Успіх", "Користувача успішно зареєстровано!");
                clearForm();
            }
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/login.fxml")));
            Scene scene = new Scene(parent);
            Stage stage =(Stage)pane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Помилка при реєстрації", e.getMessage());
        }
    }

    private void setBorderColor(String state) {
        if ("error".equals(state)) {
            txtPassword.getStyleClass().add("field-error");
            ComfirmPassword.getStyleClass().add("field-error");
        } else {
            txtPassword.getStyleClass().removeAll("field-error");
            ComfirmPassword.getStyleClass().removeAll("field-error");
            txtPassword.getStyleClass().add("field-normal");
            ComfirmPassword.getStyleClass().add("field-normal");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        txtUserSecondName.clear();
        txtUserName.clear();
        txtThirdName.clear();
        HomeAdress.clear();
        PhoneNumber.clear();
        txtPassword.clear();
        ComfirmPassword.clear();
    }
}
