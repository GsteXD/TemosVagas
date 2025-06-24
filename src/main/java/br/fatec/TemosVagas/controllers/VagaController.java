package br.fatec.TemosVagas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.fatec.TemosVagas.dtos.vaga.VagaDTO;
import br.fatec.TemosVagas.dtos.vaga.VagaResponseDTO;
import br.fatec.TemosVagas.entities.Vaga;
import br.fatec.TemosVagas.services.VagaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/vaga")
public class VagaController {
    
    @Autowired
    VagaService vagaService;

    @PostMapping("/cadastrar")
    public @ResponseBody ResponseEntity<VagaResponseDTO> cadastrar(@RequestBody @Valid VagaDTO dto) {
        Vaga vaga = vagaService.cadastrar(VagaDTO.toVaga(dto));
        return ResponseEntity.ok().body(VagaResponseDTO.valueOf(vaga));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VagaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid VagaDTO dto) {
        Vaga vaga = vagaService.atualizar(id, VagaDTO.toVaga(dto));
        return ResponseEntity.ok().body(VagaResponseDTO.valueOf(vaga));
    }
}
