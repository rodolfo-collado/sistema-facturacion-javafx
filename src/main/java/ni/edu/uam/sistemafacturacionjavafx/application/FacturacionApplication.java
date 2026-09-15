package ni.edu.uam.sistemafacturacionjavafx.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class FacturacionApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                FacturacionApplication.class.getResource(
                        "/ni/edu/uam/sistemafacturacionjavafx/fxml/menu-principal.fxml"
        ));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(
                FacturacionApplication.class.getResource(
                        "/ni/edu/uam/sistemafacturacionjavafx/css/application.css"
                ),
                "No se encontró application.css en resources."
        ).toExternalForm());
        stage.setTitle("Sistema de Facturación");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
