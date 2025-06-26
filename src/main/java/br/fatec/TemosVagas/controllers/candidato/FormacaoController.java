package br.fatec.TemosVagas.controllers.candidato;

import br.fatec.TemosVagas.dtos.candidato.FormacaoDTO;
import br.fatec.TemosVagas.entities.candidato.Formacao;
import br.fatec.TemosVagas.services.candidato.FormacaoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/candidato/formacao")
public class FormacaoController {

    @Autowired
    private FormacaoService formacaoService;

    @PostMapping("/cadastrar")
    public @ResponseBody ResponseEntity<FormacaoDTO> cadastrar(@RequestBody FormacaoDTO formacaoDTO) {
        Formacao formacao = formacaoService.cadastrar(FormacaoDTO.toFormacao(formacaoDTO));
        return ResponseEntity.ok().body(FormacaoDTO.valueOf(formacao));
    }

    @PutMapping("/atualizar/{id}")
    public @ResponseBody ResponseEntity<FormacaoDTO> atualizar(@RequestBody FormacaoDTO formacaoDTO, @PathVariable Long id) {
        Formacao formacao = formacaoService.atualizar(FormacaoDTO.toFormacao(formacaoDTO), id);
        return ResponseEntity.ok().body(FormacaoDTO.valueOf(formacao));
    }

    @DeleteMapping("/deletar/{id_formacao}")
    public ResponseEntity<String> deletar(@PathVariable Long id_formacao) {
        formacaoService.deletar(id_formacao); // Função tipo void, nada é retornado
        return ResponseEntity.ok().body("Formação deletada com sucesso!");
    }

    // Lista todos as formações a partir de um id de currículo
    @GetMapping("/listar")
    public @ResponseBody ResponseEntity<List<FormacaoDTO>> listarFormacoes() {
        List<Formacao> formacoes = formacaoService.listarFormacoes();
        return ResponseEntity.ok().body(FormacaoDTO.valueAll(formacoes));
    }

    
}
