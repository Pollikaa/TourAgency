module org.example.touragency {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.touragency to javafx.fxml;
    exports org.example.touragency;
    exports org.example.touragency.controller;
    opens org.example.touragency.controller to javafx.fxml;
    exports org.example.touragency.db;
    opens org.example.touragency.db to javafx.fxml;
}