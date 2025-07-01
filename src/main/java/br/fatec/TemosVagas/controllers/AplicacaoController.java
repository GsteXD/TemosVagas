package br.fatec.TemosVagas.controllers;

import br.fatec.TemosVagas.dtos.aplicacao.AplicacaoDTO;
import br.fatec.TemosVagas.dtos.aplicacao.AplicacaoReponseDTO;
import br.fatec.TemosVagas.entities.Aplicacao;
import br.fatec.TemosVagas.entities.enums.StatusAplicacao;
import br.fatec.TemosVagas.services.AplicacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aplicacao")
public class AplicacaoController {

    @Autowired
    private AplicacaoService aplicacaoService;

    //candidato
    @PostMapping("/candidatar")
    public @ResponseBody ResponseEntity<AplicacaoReponseDTO> candidatarVaga(@RequestBody @Valid AplicacaoDTO dto) {
            Aplicacao aplicacao = aplicacaoService.aplicarVaga(dto.vagaId(), dto.informacaoAdicionais());
            return ResponseEntity.ok().body(AplicacaoDTO.valueOf(aplicacao));
    }

    //candidato
    @GetMapping("/minhasCandidaturas")
    public @ResponseBody ResponseEntity<List<AplicacaoReponseDTO>> minhasAplicacoes() {
        List<Aplicacao> aplicacoes = aplicacaoService.listarAplicacoesCandidato();

        List<AplicacaoReponseDTO> response = aplicacoes.stream()
                .map(AplicacaoDTO::valueOf)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    //empresa: verifica todas as aplicações feitas para a vaga.
    @GetMapping("/vaga/{vagaId}")
    public @ResponseBody ResponseEntity<List<AplicacaoReponseDTO>> aplicacoesPorVaga(@PathVariable Long vagaId) {
        List<Aplicacao> aplicacoes = aplicacaoService.listarAplicacoesVaga(vagaId);

        List<AplicacaoReponseDTO> response = aplicacoes.stream()
                .map(AplicacaoDTO::valueOf)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    //empresa:Muda o status de uma aplicação.
    @PutMapping("/status/{aplicacaoId}/{status}")
    public @ResponseBody ResponseEntity<AplicacaoReponseDTO> atualizarStatus(
            @PathVariable Long aplicacaoId,
            @PathVariable StatusAplicacao status) {
        Aplicacao aplicacao = aplicacaoService.atualizarStatusAplicacao(aplicacaoId, status);
        return ResponseEntity.ok().body(AplicacaoDTO.valueOf(aplicacao));
    }
}
