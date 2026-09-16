package ni.edu.uam.sistemafacturacionjavafx.fxml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FxmlContractTest {

    private static final String BASE = "/ni/edu/uam/sistemafacturacionjavafx/fxml/";

    @Test
    void cadaAccionFXMLTieneUnHandlerEnSuController() throws Exception {
        verificarVista("menu-principal.fxml");
        verificarVista("producto-view.fxml");
        verificarVista("cargo-view.fxml");
    }

    private void verificarVista(String archivo) throws Exception {
        try (InputStream recurso = getClass().getResourceAsStream(BASE + archivo)) {
            assertNotNull(recurso, "No se encontró " + archivo);
            Document documento = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(recurso);
            Element raiz = documento.getDocumentElement();
            String controller = raiz.getAttribute("fx:controller");
            assertFalseBlank(controller, "La vista debe declarar controller: " + archivo);
            Class<?> claseController = Class.forName(controller);
            NodeList nodos = documento.getElementsByTagName("*");
            int acciones = 0;
            for (int indice = 0; indice < nodos.getLength(); indice++) {
                Element elemento = (Element) nodos.item(indice);
                String accion = elemento.getAttribute("onAction");
                if (elemento.getTagName().equals("Button") || elemento.getTagName().equals("MenuItem")) {
                    assertTrue(accion.startsWith("#"),
                            () -> archivo + " tiene un control sin acción: " + elemento.getAttribute("text"));
                }
                if (!accion.isBlank()) {
                    acciones++;
                    String nombreMetodo = accion.substring(1);
                    assertTrue(Arrays.stream(claseController.getDeclaredMethods())
                                    .map(Method::getName)
                                    .anyMatch(nombreMetodo::equals),
                            () -> archivo + " referencia el handler inexistente " + accion);
                }
            }
            assertTrue(acciones > 0, archivo + " no tiene acciones para verificar");
        }
    }

    private void assertFalseBlank(String valor, String mensaje) {
        assertTrue(valor != null && !valor.isBlank(), mensaje);
    }
}
