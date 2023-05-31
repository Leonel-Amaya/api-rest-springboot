package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.DatoListadoMedico;
import med.voll.api.medico.DatosRegistroMedicos;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")//Mapea endpoint
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public void registrarMedico(@RequestBody @Valid DatosRegistroMedicos datosRegistroMedicos) {
        medicoRepository.save(new Medico(datosRegistroMedicos));
    }

    //@GetMapping
    //public List<DatoListadoMedico> listadoMedicos() {
    //    return medicoRepository.findAll().stream().map(DatoListadoMedico::new).toList();
    //}
    //Esta porción del código trae consigo los datos seleccionados pero no de modo de paginación

    @GetMapping
    public Page<DatoListadoMedico> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion) {
        return medicoRepository.findAll(paginacion).map(DatoListadoMedico::new);
    }
    //Trae consigo los datos seleccionados en DatosListadoMedico pero formateados en páginas

    @PutMapping
    public void actualizarMedico() {

    }
}
