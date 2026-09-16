package ni.edu.uam.sistemafacturacionjavafx.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ni.edu.uam.sistemafacturacionjavafx.model.Categoria;
import ni.edu.uam.sistemafacturacionjavafx.model.Producto;
import ni.edu.uam.sistemafacturacionjavafx.util.DatosAplicacion;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class ProductoController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtExistencia;
    @FXML private CheckBox chkActivo;
    @FXML private ImageView imgProducto;
    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colExistencia;
    @FXML private TableColumn<Producto, String> colEstado;

    private UUID productoEnEdicion;
    private String rutaImagen;

    @FXML
    private void initialize() {
        cmbCategoria.setItems(DatosAplicacion.getCategoriasActivas());
        tblProductos.setItems(DatosAplicacion.getProductos());

        colCodigo.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getCodigo()));
        colNombre.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getNombre()));
        colCategoria.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getCategoria().getNombre()));
        colPrecio.setCellValueFactory(celda -> new SimpleObjectProperty<>(celda.getValue().getPrecio()));
        colPrecio.setCellFactory(columna -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(BigDecimal precio, boolean empty) {
                super.updateItem(precio, empty);
                setText(empty || precio == null ? null : NumberFormat.getCurrencyInstance(Locale.US).format(precio));
            }
        });
        colExistencia.setCellValueFactory(celda -> new SimpleObjectProperty<>(celda.getValue().getExistencia()));
        colEstado.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().isActivo() ? "Activo" : "Inactivo"));

        tblProductos.getSelectionModel().selectedItemProperty().addListener((observable, anterior, producto) -> {
            if (producto != null) {
                cargarProducto(producto);
            }
        });
    }

    @FXML
    private void seleccionarImagen(ActionEvent evento) {
        FileChooser selector = new FileChooser();
        selector.setTitle("Seleccionar imagen del producto");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
        File archivo = selector.showOpenDialog(((javafx.scene.Node) evento.getSource()).getScene().getWindow());
        if (archivo == null) {
            return;
        }

        String nuevaRuta = archivo.toURI().toString();
        Image imagen = new Image(nuevaRuta);
        if (imagen.isError()) {
            mostrarError("La imagen seleccionada no se pudo cargar.");
            return;
        }
        rutaImagen = nuevaRuta;
        imgProducto.setImage(imagen);
    }

    @FXML
    private void guardar() {
        try {
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            int existencia = Integer.parseInt(txtExistencia.getText().trim());
            DatosAplicacion.guardarProducto(productoEnEdicion, txtCodigo.getText(), txtNombre.getText(),
                    cmbCategoria.getValue(), precio, existencia, rutaImagen, chkActivo.isSelected());
            tblProductos.refresh();
            limpiarFormulario();
            mostrarInformacion("Producto guardado correctamente.");
        } catch (NumberFormatException e) {
            mostrarError("Precio y existencia deben contener valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void cerrar(ActionEvent evento) {
        ((javafx.scene.Node) evento.getSource()).getScene().getWindow().hide();
    }

    private void cargarProducto(Producto producto) {
        productoEnEdicion = producto.getId();
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cmbCategoria.setValue(producto.getCategoria());
        txtPrecio.setText(producto.getPrecio().toPlainString());
        txtExistencia.setText(String.valueOf(producto.getExistencia()));
        chkActivo.setSelected(producto.isActivo());
        rutaImagen = producto.getRutaImagen();
        imgProducto.setImage(rutaImagen == null || rutaImagen.isBlank() ? null : new Image(rutaImagen, true));
    }

    private void limpiarFormulario() {
        productoEnEdicion = null;
        rutaImagen = null;
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtPrecio.clear();
        txtExistencia.clear();
        chkActivo.setSelected(true);
        imgProducto.setImage(null);
        tblProductos.getSelectionModel().clearSelection();
        txtCodigo.requestFocus();
    }

    private void mostrarInformacion(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }

    private void mostrarError(String mensaje) {
        new Alert(Alert.AlertType.ERROR, mensaje).showAndWait();
    }
}
