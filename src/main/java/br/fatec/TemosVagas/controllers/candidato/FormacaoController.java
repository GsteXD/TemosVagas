package br.fatec.TemosVagas.controllers.candidato;

import br.fatec.TemosVagas.dtos.FormacaoDTO;
import br.fatec.TemosVagas.entities.candidato.Formacao;
import br.fatec.TemosVagas.services.candidato.FormacaoService;
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


}
