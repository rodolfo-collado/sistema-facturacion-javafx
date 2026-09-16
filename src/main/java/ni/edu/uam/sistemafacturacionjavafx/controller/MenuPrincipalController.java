package ni.edu.uam.sistemafacturacionjavafx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ni.edu.uam.sistemafacturacionjavafx.util.SceneManager;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    private void abrirProductos() {
        try {
            SceneManager.abrirVentana(
                    "/ni/edu/uam/sistemafacturacionjavafx/fxml/producto-view.fxml",
                    "Gestión de productos"
            );
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,
                    "No fue posible abrir Productos.", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void abrirCargos() {
        try {
            SceneManager.abrirVentana(
                    "/ni/edu/uam/sistemafacturacionjavafx/fxml/cargo-view.fxml",
                    "Gestión de cargos"
            );
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,
                    "No fue posible abrir Cargos.", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void acercaDe() {
        new Alert(Alert.AlertType.INFORMATION,
                "Sistema de Facturación\nProgramación de Aplicaciones de Escritorio").showAndWait();
    }

    @FXML
    private void salir() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desea cerrar la aplicación?", ButtonType.OK, ButtonType.CANCEL);
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Platform.exit();
        }
    }
}
