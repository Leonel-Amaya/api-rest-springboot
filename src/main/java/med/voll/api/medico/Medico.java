package med.voll.api.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.direccion.Direccion;

@Entity(name = "Medico")
@Table(name = "medicos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String documento;
    private boolean activo;
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    @Embedded
    private Direccion direccion;

    public Medico(DatosRegistroMedicos datosRegistroMedicos) {
        this.activo = true;
        this.nombre = datosRegistroMedicos.nombre();
        this.email = datosRegistroMedicos.email();
        this.telefono = datosRegistroMedicos.telefono();
        this.documento = datosRegistroMedicos.documento();
        this.especialidad = datosRegistroMedicos.especialidad();
        this.direccion = new Direccion(datosRegistroMedicos.direccion());
    }

    public void actualizarDatos(DatosActualizarMedicos datosActualizarMedicos) {
        if (datosActualizarMedicos.nombre() != null) {
            this.nombre = datosActualizarMedicos.nombre();
        }
        if (datosActualizarMedicos.documento() != null) {
            this.documento = datosActualizarMedicos.documento();
        }
        if (datosActualizarMedicos.direccion() != null) {
            this.direccion = direccion.actualizarDatos(datosActualizarMedicos.direccion());
        }
    }

    public void desactivarMedico() {
        this.activo = false;
    }
}
