package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.direccion.DatosDireccion;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/medicos")//Mapea endpoint
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedicos datosRegistroMedicos,
                                          UriComponentsBuilder uriComponentsBuilder) {
        //UriComponentsBuildes es una clase auxiliar que ayuda para obtener los datos del servidor
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedicos));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),medico.getDocumento(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);//Pide un URI que es la URL en la que el recurso será encontrado
    }

    //@GetMapping
    //public List<DatoListadoMedico> listadoMedicos() {
    //    return medicoRepository.findAll().stream().map(DatoListadoMedico::new).toList();
    //}
    //Esta porción del código trae consigo los datos seleccionados pero no de modo de paginación

    @GetMapping
    public ResponseEntity<Page<DatoListadoMedico>> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion) {
//        return medicoRepository.findAll(paginacion).map(DatoListadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatoListadoMedico::new));
    }
    //Trae consigo los datos seleccionados en DatosListadoMedico pero formateados en páginas

    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedicos datosActualizarMedicos) {
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedicos.id());
        medico.actualizarDatos(datosActualizarMedicos);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),medico.getDocumento(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento())));
    }

    @DeleteMapping("/{id}")
    @Transactional
    //DELETE LOGICO
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

//    DELETE BASE DE DATOS
//    public void eliminarMedico(@PathVariable Long id){
//        Medico medico = medicoRepository.getReferenceById(id);
//        medicoRepository.delete(medico);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornarDatosMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),medico.getDocumento(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }
}
