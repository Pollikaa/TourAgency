package org.example.touragency.controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.touragency.db.DataBase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginFormController {
    public TextField txtUserSecondName;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button LoginButton;
    public Button RegisterButton;
    public AnchorPane root;

    public void btnSignin(ActionEvent actionEvent) {
        try (
                Connection connection = DataBase.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM client_agency WHERE last_name=? AND first_name=? AND password=?")
        ) {
            String lastName = txtUserSecondName.getText();
            String userName = txtUserName.getText();
            String password = txtPassword.getText();

            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    int userId = resultSet.getInt("id_client");
                    Session.setUser(userId, userName, lastName); // Зберігаємо дані в сесію

                    Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/MainForm.fxml")));
                    Scene scene = new Scene(parent);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/style.css")).toExternalForm());
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Main Form");
                    stage.centerOnScreen();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Прізвище, ім'я або пароль не співпадають!");
                    alert.showAndWait();
                    txtUserSecondName.clear();
                    txtUserName.clear();
                    txtPassword.requestFocus();
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    public void btnSignup(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/touragency/RegistrationForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Registration Form");
        stage.setResizable(false);
        stage.centerOnScreen();
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }
}
