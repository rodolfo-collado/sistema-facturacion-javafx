package ni.edu.uam.sistemafacturacionjavafx.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.sistemafacturacionjavafx.model.Cargo;
import ni.edu.uam.sistemafacturacionjavafx.model.Categoria;
import ni.edu.uam.sistemafacturacionjavafx.model.Producto;

import java.math.BigDecimal;
import java.util.UUID;

/** Estado temporal compartido por las ventanas de la aplicación. */
public final class DatosAplicacion {

    private static final ObservableList<Producto> PRODUCTOS = FXCollections.observableArrayList();
    private static final ObservableList<Cargo> CARGOS = FXCollections.observableArrayList();
    private static final ObservableList<Categoria> CATEGORIAS = FXCollections.observableArrayList(
            new Categoria(UUID.randomUUID(), "Alimentos", true),
            new Categoria(UUID.randomUUID(), "Bebidas", true),
            new Categoria(UUID.randomUUID(), "Limpieza", true),
            new Categoria(UUID.randomUUID(), "Tecnología", true)
    );

    private DatosAplicacion() {
    }

    public static ObservableList<Producto> getProductos() {
        return PRODUCTOS;
    }

    public static ObservableList<Cargo> getCargos() {
        return CARGOS;
    }

    public static ObservableList<Categoria> getCategoriasActivas() {
        return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(
                CATEGORIAS.stream().filter(Categoria::isActiva).toList()
        ));
    }

    public static Producto guardarProducto(UUID id, String codigo, String nombre, Categoria categoria,
                                           BigDecimal precio, int existencia, String rutaImagen, boolean activo) {
        String codigoNormalizado = textoObligatorio(codigo, "El código", 40).toUpperCase();
        String nombreNormalizado = textoObligatorio(nombre, "El nombre", 120);

        if (categoria == null || !categoria.isActiva()) {
            throw new IllegalArgumentException("Debe seleccionar una categoría activa.");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (existencia < 0) {
            throw new IllegalArgumentException("La existencia no puede ser negativa.");
        }
        if (PRODUCTOS.stream().anyMatch(producto -> producto.getCodigo().equalsIgnoreCase(codigoNormalizado)
                && !producto.getId().equals(id))) {
            throw new IllegalArgumentException("Ya existe un producto con ese código.");
        }

        Producto producto = buscarProducto(id);
        if (id != null && producto == null) {
            throw new IllegalArgumentException("El producto seleccionado ya no existe.");
        }
        if (producto == null) {
            producto = new Producto(UUID.randomUUID(), codigoNormalizado, nombreNormalizado, categoria,
                    precio, existencia, rutaImagen, activo);
            PRODUCTOS.add(producto);
        } else {
            producto.setCodigo(codigoNormalizado);
            producto.setNombre(nombreNormalizado);
            producto.setCategoria(categoria);
            producto.setPrecio(precio);
            producto.setExistencia(existencia);
            producto.setRutaImagen(rutaImagen);
            producto.setActivo(activo);
        }
        return producto;
    }

    public static Cargo guardarCargo(UUID id, String nombre, String descripcion) {
        String nombreNormalizado = textoObligatorio(nombre, "El nombre", 100);
        String descripcionNormalizada = textoObligatorio(descripcion, "La descripción", 250);

        if (CARGOS.stream().anyMatch(cargo -> cargo.getNombre().equalsIgnoreCase(nombreNormalizado)
                && !cargo.getId().equals(id))) {
            throw new IllegalArgumentException("Ya existe un cargo con ese nombre.");
        }

        Cargo cargo = buscarCargo(id);
        if (id != null && cargo == null) {
            throw new IllegalArgumentException("El cargo seleccionado ya no existe.");
        }
        if (cargo == null) {
            cargo = new Cargo(UUID.randomUUID(), nombreNormalizado, descripcionNormalizada);
            CARGOS.add(cargo);
        } else {
            cargo.setNombre(nombreNormalizado);
            cargo.setDescripcion(descripcionNormalizada);
        }
        return cargo;
    }

    public static void eliminarCargo(UUID id) {
        Cargo cargo = buscarCargo(id);
        if (cargo == null || !CARGOS.remove(cargo)) {
            throw new IllegalArgumentException("El cargo seleccionado ya no existe.");
        }
    }

    private static Producto buscarProducto(UUID id) {
        if (id == null) {
            return null;
        }
        return PRODUCTOS.stream().filter(producto -> producto.getId().equals(id)).findFirst().orElse(null);
    }

    private static Cargo buscarCargo(UUID id) {
        if (id == null) {
            return null;
        }
        return CARGOS.stream().filter(cargo -> cargo.getId().equals(id)).findFirst().orElse(null);
    }

    private static String textoObligatorio(String texto, String campo, int longitudMaxima) {
        String valor = texto == null ? "" : texto.trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        if (valor.length() > longitudMaxima) {
            throw new IllegalArgumentException(campo + " no puede superar " + longitudMaxima + " caracteres.");
        }
        return valor;
    }
}
