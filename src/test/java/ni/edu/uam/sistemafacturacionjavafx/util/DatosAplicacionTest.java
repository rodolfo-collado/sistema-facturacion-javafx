package ni.edu.uam.sistemafacturacionjavafx.util;

import ni.edu.uam.sistemafacturacionjavafx.model.Cargo;
import ni.edu.uam.sistemafacturacionjavafx.model.Categoria;
import ni.edu.uam.sistemafacturacionjavafx.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatosAplicacionTest {

    private Categoria categoria;

    @BeforeEach
    void limpiarDatos() {
        DatosAplicacion.getProductos().clear();
        DatosAplicacion.getCargos().clear();
        categoria = DatosAplicacion.getCategoriasActivas().getFirst();
    }

    @Test
    void guardaProductoNormalizandoCodigoYConservaDatosAlConsultarLaListaCompartida() {
        Producto producto = DatosAplicacion.guardarProducto(null, " prd-001 ", " Café ", categoria,
                new BigDecimal("12.50"), 8, "file:/producto.png", true);

        assertNotNull(producto.getId());
        assertEquals("PRD-001", producto.getCodigo());
        assertEquals("Café", producto.getNombre());
        assertSame(producto, DatosAplicacion.getProductos().getFirst());
        assertEquals("file:/producto.png", DatosAplicacion.getProductos().getFirst().getRutaImagen());
    }

    @Test
    void actualizaProductoSinDuplicarRegistro() {
        Producto creado = DatosAplicacion.guardarProducto(null, "PRD-001", "Teclado", categoria,
                new BigDecimal("40"), 3, null, true);

        Producto actualizado = DatosAplicacion.guardarProducto(creado.getId(), "PRD-001", "Teclado mecánico",
                categoria, new BigDecimal("45.75"), 5, null, false);

        assertSame(creado, actualizado);
        assertEquals(1, DatosAplicacion.getProductos().size());
        assertEquals("Teclado mecánico", creado.getNombre());
        assertEquals(new BigDecimal("45.75"), creado.getPrecio());
        assertFalse(creado.isActivo());
    }

    @Test
    void rechazaProductoConCamposOValoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarProducto(
                null, "", "Producto", categoria, BigDecimal.ONE, 0, null, true));
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarProducto(
                null, "PRD-001", "Producto", null, BigDecimal.ONE, 0, null, true));
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarProducto(
                null, "PRD-001", "Producto", categoria, BigDecimal.ZERO, 0, null, true));
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarProducto(
                null, "PRD-001", "Producto", categoria, BigDecimal.ONE, -1, null, true));
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarProducto(
                null, "PRD-001", "Producto", new Categoria(UUID.randomUUID(), "Inactiva", false),
                BigDecimal.ONE, 0, null, true));
    }

    @Test
    void rechazaCodigoDuplicadoSinDistinguirMayusculas() {
        DatosAplicacion.guardarProducto(null, "prd-001", "Primero", categoria, BigDecimal.ONE, 1, null, true);

        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarProducto(
                null, "PRD-001", "Segundo", categoria, BigDecimal.ONE, 1, null, true));
        assertEquals(1, DatosAplicacion.getProductos().size());
    }

    @Test
    void creaEditaYEliminaCargoEnLaMismaListaCompartida() {
        Cargo creado = DatosAplicacion.guardarCargo(null, " Supervisor ", " Coordina el equipo ");
        DatosAplicacion.guardarCargo(creado.getId(), "Supervisor de tienda", "Coordina el equipo de tienda");

        assertEquals(1, DatosAplicacion.getCargos().size());
        assertEquals("Supervisor de tienda", DatosAplicacion.getCargos().getFirst().getNombre());

        DatosAplicacion.eliminarCargo(creado.getId());
        assertEquals(0, DatosAplicacion.getCargos().size());
    }

    @Test
    void rechazaCargosDuplicadosYEliminacionInexistente() {
        DatosAplicacion.guardarCargo(null, "Cajero", "Atiende cobros");

        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarCargo(
                null, " cajero ", "Otro texto"));
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.eliminarCargo(UUID.randomUUID()));
        assertEquals(1, DatosAplicacion.getCargos().size());
    }

    @Test
    void rechazaActualizarRegistrosQueYaNoExisten() {
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarProducto(
                UUID.randomUUID(), "PRD-001", "Producto", categoria, BigDecimal.ONE, 1, null, true));
        assertThrows(IllegalArgumentException.class, () -> DatosAplicacion.guardarCargo(
                UUID.randomUUID(), "Cargo", "Descripción"));
        assertEquals(0, DatosAplicacion.getProductos().size());
        assertEquals(0, DatosAplicacion.getCargos().size());
    }
}
