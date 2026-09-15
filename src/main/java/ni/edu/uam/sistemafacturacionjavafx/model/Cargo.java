package ni.edu.uam.sistemafacturacionjavafx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {
    private UUID id;
    private String nombre;
    private String descripcion;
}
