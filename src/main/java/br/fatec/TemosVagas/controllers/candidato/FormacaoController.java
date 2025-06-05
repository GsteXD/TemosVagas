package br.fatec.TemosVagas.controllers.candidato;

import br.fatec.TemosVagas.dtos.FormacaoDTO;
import br.fatec.TemosVagas.entities.candidato.Formacao;
import br.fatec.TemosVagas.services.candidato.FormacaoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formacao")
public class FormacaoController {

    @Autowired
    private FormacaoService formacaoService;

    @PostMapping("/cadastrar/{id_curriculo}")
    public @ResponseBody ResponseEntity<FormacaoDTO> cadastrar(@RequestBody FormacaoDTO formacaoDTO, @PathVariable Long id_curriculo) {
        Formacao formacao = formacaoService.cadastrar(FormacaoDTO.toFormacao(formacaoDTO), id_curriculo);
        return ResponseEntity.ok().body(FormacaoDTO.valueOf(formacao));
    }

    @DeleteMapping("/deletar/{id_curriculo}/{id_formacao}")
    public ResponseEntity<String> deletar(@PathVariable Long id_formacao, @PathVariable Long id_curriculo) {
        formacaoService.deletar(id_formacao, id_curriculo); // Função tipo void, nada é retornado
        return ResponseEntity.ok().body("Formação deletada com sucesso!");
    }

    // Lista todos as formações a partir de um id de currículo
    @GetMapping("/listar/{id_curriculo}")
    public @ResponseBody ResponseEntity<List<FormacaoDTO>> listarFormacoes(@PathVariable Long id_curriculo) {
        List<Formacao> formacoes = formacaoService.listarFormacoes(id_curriculo);
        return ResponseEntity.ok().body(FormacaoDTO.valueAll(formacoes));
    }

    
}
