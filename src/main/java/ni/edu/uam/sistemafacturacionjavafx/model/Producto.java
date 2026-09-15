package ni.edu.uam.sistemafacturacionjavafx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private UUID id;
    private String codigo;
    private String nombre;
    private Categoria categoria;
    private BigDecimal precio;
    private int existencia;
    private String rutaImagen;
    private boolean isActivo;
}
