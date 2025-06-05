package br.fatec.TemosVagas.controllers.candidato;

import br.fatec.TemosVagas.dtos.CurriculoDTO;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.services.candidato.CurriculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curriculo")
public class CurriculoController {

    @Autowired
    CurriculoService curriculoService;

    @PostMapping("/cadastrar/{id_usuario}")
    public @ResponseBody ResponseEntity<CurriculoDTO> cadastrar(@RequestBody CurriculoDTO curriculoDTO, @PathVariable Long id_usuario) {
        Curriculo curriculo = curriculoService.cadastrar(CurriculoDTO.toCurriculo(curriculoDTO), id_usuario);
        return ResponseEntity.ok().body(CurriculoDTO.valueOf(curriculo));
    }

    // Mostra todas as informações do currículo
    @GetMapping("/listar/{id_curriculo}")
    public @ResponseBody ResponseEntity<CurriculoDTO> listarCurriculo(@PathVariable Long id_curriculo) {
        Curriculo curriculo = curriculoService.listarCurriculo(id_curriculo);
        return ResponseEntity.ok().body(CurriculoDTO.valueOf(curriculo));
    }
}
