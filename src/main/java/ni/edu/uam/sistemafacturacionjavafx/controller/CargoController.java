package ni.edu.uam.sistemafacturacionjavafx.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ni.edu.uam.sistemafacturacionjavafx.model.Cargo;
import ni.edu.uam.sistemafacturacionjavafx.util.DatosAplicacion;

import java.util.UUID;

/** Controlador de la vista de gestión de cargos. */
public class CargoController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TableView<Cargo> tblCargos;
    @FXML private TableColumn<Cargo, String> colId;
    @FXML private TableColumn<Cargo, String> colNombre;
    @FXML private TableColumn<Cargo, String> colDescripcion;

    private UUID cargoEnEdicion;

    @FXML
    private void initialize() {
        txtId.setDisable(true);
        tblCargos.setItems(DatosAplicacion.getCargos());
        colId.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getId().toString()));
        colNombre.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getNombre()));
        colDescripcion.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getDescripcion()));
        tblCargos.getSelectionModel().selectedItemProperty().addListener((observable, anterior, cargo) -> {
            if (cargo != null) {
                cargarCargo(cargo);
            }
        });
    }

    @FXML
    private void nuevo() {
        guardarNuevo();
    }

    @FXML
    private void editar() {
        if (cargoEnEdicion == null) {
            mostrarError("Seleccione un cargo de la tabla para editarlo.");
            return;
        }
        try {
            DatosAplicacion.guardarCargo(cargoEnEdicion, txtNombre.getText(), txtDescripcion.getText());
            tblCargos.refresh();
            mostrarInformacion("Cargo actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void eliminar() {
        if (cargoEnEdicion == null) {
            mostrarError("Seleccione un cargo de la tabla para eliminarlo.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desea eliminar el cargo seleccionado?", javafx.scene.control.ButtonType.OK,
                javafx.scene.control.ButtonType.CANCEL);
        if (confirmacion.showAndWait().orElse(javafx.scene.control.ButtonType.CANCEL)
                != javafx.scene.control.ButtonType.OK) {
            return;
        }
        try {
            DatosAplicacion.eliminarCargo(cargoEnEdicion);
            limpiarFormulario();
            mostrarInformacion("Cargo eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        limpiarFormulario();
    }

    @FXML
    private void regresar(ActionEvent evento) {
        ((javafx.scene.Node) evento.getSource()).getScene().getWindow().hide();
    }

    private void guardarNuevo() {
        try {
            DatosAplicacion.guardarCargo(null, txtNombre.getText(), txtDescripcion.getText());
            limpiarFormulario();
            mostrarInformacion("Cargo guardado correctamente.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarCargo(Cargo cargo) {
        cargoEnEdicion = cargo.getId();
        txtId.setText(cargo.getId().toString());
        txtId.setDisable(true);
        txtNombre.setText(cargo.getNombre());
        txtDescripcion.setText(cargo.getDescripcion());
    }

    private void limpiarFormulario() {
        cargoEnEdicion = null;
        txtId.clear();
        txtId.setDisable(true);
        txtNombre.clear();
        txtDescripcion.clear();
        tblCargos.getSelectionModel().clearSelection();
        txtNombre.requestFocus();
    }

    private void mostrarInformacion(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }

    private void mostrarError(String mensaje) {
        new Alert(Alert.AlertType.ERROR, mensaje).showAndWait();
    }
}
