package br.fatec.TemosVagas.controllers;

import br.fatec.TemosVagas.dtos.CandidatoDTO;
import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.services.CandidatoService;
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
    public @ResponseBody ResponseEntity<CandidatoDTO> cadastrar(@RequestBody CandidatoDTO candidatoDTO) {
        Candidato candidato = candidatoService.cadastrar(CandidatoDTO.toCandidato(candidatoDTO));
        return ResponseEntity.ok().body(CandidatoDTO.valueOf(candidato));
    }

}
