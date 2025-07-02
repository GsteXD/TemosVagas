package br.fatec.TemosVagas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.fatec.TemosVagas.dtos.vaga.VagaRelatorioDTO;
import br.fatec.TemosVagas.services.RelatorioService;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {
    
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/vagasEmpresa")
    public @ResponseBody ResponseEntity<List<VagaRelatorioDTO>> gerarRelatorioVagasEmpresa() {
        List<VagaRelatorioDTO> relatorio = relatorioService.relatorioVagaEmpresa();
        return ResponseEntity.ok().body(relatorio);
    }

    @GetMapping("/vagasGrupo")
    public @ResponseBody ResponseEntity<List<VagaRelatorioDTO>> gerarRelatorioVagasGrupo() {
        List<VagaRelatorioDTO> relatorio = relatorioService.relatorioVagaGrupo();
        return ResponseEntity.ok().body(relatorio);
    }

}
