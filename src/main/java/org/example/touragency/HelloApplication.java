package org.example.touragency;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.touragency.db.DataBase;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Створення об'єкта бази даних і вивід статусу з'єднання
        Connection connection = DataBase.getInstance().getConnection();
        // Завантаження графічного інтерфейсу
        Parent parent= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }


    public static void main(String[] args) {
        launch();
    }
}