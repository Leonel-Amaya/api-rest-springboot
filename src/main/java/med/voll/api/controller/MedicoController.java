package med.voll.api.controller;

import med.voll.api.medico.DatosRegistroMedicos;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicos")//Mapea endpoint
public class MedicoController {
    @PostMapping
    public void registrarMedico(@RequestBody DatosRegistroMedicos datosRegistroMedicos) {
        System.out.println("El request llega correctamente");
        System.out.println(datosRegistroMedicos);
    }
}
