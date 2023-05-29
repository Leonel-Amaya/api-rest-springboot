package med.voll.api.medico;

import med.voll.api.DatosDireccion;

public record DatosRegistroMedicos(String nombre, String email, String documento, Especialidad especialidad, DatosDireccion direccion) {
}
