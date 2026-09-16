package ni.edu.uam.sistemafacturacionjavafx.fxml;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class FxmlLoadTest {

    private static final String BASE = "/ni/edu/uam/sistemafacturacionjavafx/fxml/";

    @BeforeAll
    static void iniciarJavaFx() throws InterruptedException {
        CountDownLatch inicio = new CountDownLatch(1);
        Platform.startup(inicio::countDown);
        if (!inicio.await(10, TimeUnit.SECONDS)) {
            fail("JavaFX no inició dentro del tiempo esperado.");
        }
    }

    @AfterAll
    static void cerrarJavaFx() {
        Platform.exit();
    }

    @Test
    void cargaTodasLasVistasConSusControlesYControllers() throws Exception {
        ejecutarEnJavaFx(() -> {
            verificarVista("menu-principal.fxml", "#btnProductos", "#btnCargos", "#btnSalir");
            verificarVista("producto-view.fxml", "#btnImagen", "#btnGuardar", "#btnCerrar", "#tblProductos");
            verificarVista("cargo-view.fxml", "#btnNuevo", "#btnEditar", "#btnEliminar", "#btnLimpiar",
                    "#btnRegresar", "#tblCargos");
        });
    }

    private void verificarVista(String archivo, String... selectores) throws Exception {
        URL recurso = getClass().getResource(BASE + archivo);
        assertNotNull(recurso, "No se encontró " + archivo);
        FXMLLoader cargador = new FXMLLoader(recurso);
        Parent raiz = cargador.load();
        assertNotNull(cargador.getController(), "No se creó controller para " + archivo);
        for (String selector : selectores) {
            String fxId = selector.substring(1);
            assertNotNull(cargador.getNamespace().get(fxId), archivo + " no inyectó " + selector);
        }
    }

    private void ejecutarEnJavaFx(Accion accion) throws Exception {
        AtomicReference<Throwable> error = new AtomicReference<>();
        CountDownLatch finalizado = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                accion.ejecutar();
            } catch (Throwable throwable) {
                error.set(throwable);
            } finally {
                finalizado.countDown();
            }
        });
        if (!finalizado.await(10, TimeUnit.SECONDS)) {
            fail("La carga de FXML excedió el tiempo esperado.");
        }
        if (error.get() != null) {
            throw new AssertionError("No fue posible cargar una vista FXML.", error.get());
        }
    }

    @FunctionalInterface
    private interface Accion {
        void ejecutar() throws Exception;
    }
}
