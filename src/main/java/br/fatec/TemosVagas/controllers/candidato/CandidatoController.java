package br.fatec.TemosVagas.controllers.candidato;

import br.fatec.TemosVagas.dtos.CandidatoDTO;
import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.services.candidato.CandidatoService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidato")
public class CandidatoController {

    @Autowired
    CandidatoService candidatoService;

    // ResponseEntity<CandidatoDTO> --> Informamos que o retorno da resposta usará como base o CandidatoDTO
    @PostMapping("/cadastrar")
    public @ResponseBody ResponseEntity<CandidatoDTO> cadastrar(@RequestBody @Valid CandidatoDTO candidatoDTO) {
        Candidato candidato = candidatoService.cadastrar(CandidatoDTO.toCandidato(candidatoDTO));
        return ResponseEntity.ok().body(CandidatoDTO.valueOf(candidato));
    }

    @GetMapping("/procurar/{id}")
    public @ResponseBody ResponseEntity<CandidatoDTO> procurar(@PathVariable Long id) {
        Candidato candidato = candidatoService.findById(id);
        return ResponseEntity.ok().body(CandidatoDTO.valueOf(candidato));
    }

}
