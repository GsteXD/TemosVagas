package br.fatec.TemosVagas.controllers.candidato;

import br.fatec.TemosVagas.dtos.candidato.CandidatoDTO;
import br.fatec.TemosVagas.dtos.candidato.CandidatoEditarDTO;
import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.services.candidato.CandidatoService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


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

    @PutMapping("/atualizar")
    public @ResponseBody ResponseEntity<CandidatoEditarDTO> atualizar(@RequestBody @Valid CandidatoEditarDTO candidatoDTO) {
        Candidato candidato = candidatoService.atualizar(CandidatoEditarDTO.toCandidato(candidatoDTO));
        return ResponseEntity.ok().body(CandidatoEditarDTO.valueOf(candidato));
    }

    @GetMapping("/procurar/{id}")
    public @ResponseBody ResponseEntity<CandidatoDTO> procurar(@PathVariable Long id) {
        Candidato candidato = candidatoService.findById(id);
        return ResponseEntity.ok().body(CandidatoDTO.valueOf(candidato));
    }

    @GetMapping("/carregar")
    public @ResponseBody ResponseEntity<CandidatoDTO> carregar() {
        Candidato candidato = candidatoService.carregarUsuarioLogado();
        return ResponseEntity.ok().body(CandidatoDTO.valueOf(candidato));
    }

}
