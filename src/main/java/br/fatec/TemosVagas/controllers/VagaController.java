package br.fatec.TemosVagas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.fatec.TemosVagas.dtos.vaga.VagaDTO;
import br.fatec.TemosVagas.dtos.vaga.VagaResponseDTO;
import br.fatec.TemosVagas.entities.Vaga;
import br.fatec.TemosVagas.entities.enums.TipoStatus;
import br.fatec.TemosVagas.services.VagaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;


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
    public @ResponseBody ResponseEntity<VagaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid VagaDTO dto) {
        Vaga vaga = vagaService.atualizar(id, VagaDTO.toVaga(dto));
        return ResponseEntity.ok().body(VagaResponseDTO.valueOf(vaga));
    }

    @PutMapping("/status/{id}")
    public @ResponseBody ResponseEntity<String> controlarStatus(@PathVariable Long id, @RequestParam String status) {
        vagaService.controlarStatus(id, TipoStatus.valueOf(status));
        return ResponseEntity.ok().body("Status da vaga atualizado com sucesso.");
    }

    @GetMapping("/find/{id}")
    public @ResponseBody ResponseEntity<VagaResponseDTO> findById(@PathVariable Long id) {
        Vaga vaga = vagaService.findById(id);
        return ResponseEntity.ok().body(VagaResponseDTO.valueOf(vaga));
    }

    //Vagas disponiveis.
    @GetMapping("/listar")
    public @ResponseBody ResponseEntity<List<VagaResponseDTO>> listarVagasAbertas() {
        List<Vaga> vagas = vagaService.listarVagasAbertas();
        List<VagaResponseDTO> response = vagas.stream()
                        .map(VagaResponseDTO::valueOf)
                        .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }
}
