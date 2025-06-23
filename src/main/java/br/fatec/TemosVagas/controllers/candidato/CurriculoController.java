package br.fatec.TemosVagas.controllers.candidato;

import br.fatec.TemosVagas.dtos.candidato.CurriculoDTO;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.services.candidato.CurriculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/candidato/curriculo")
public class CurriculoController {

    @Autowired
    CurriculoService curriculoService;

    @PostMapping("/cadastrar/{id_usuario}")
    public @ResponseBody ResponseEntity<CurriculoDTO> cadastrar(@RequestBody CurriculoDTO curriculoDTO, @PathVariable Long id_usuario) {
        Curriculo curriculo = curriculoService.cadastrar(CurriculoDTO.toCurriculo(curriculoDTO), id_usuario);
        return ResponseEntity.ok().body(CurriculoDTO.valueOf(curriculo));
    }

    @PostMapping(value = "/upload/{id_curriculo}", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCurriculo(@RequestParam("file") MultipartFile file, @PathVariable Long id_curriculo) {
        if (file.isEmpty() || !"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().body("Arquivo inválido. Envie um PDF.");
        }
        curriculoService.uploadCurriculo(id_curriculo, file);

        return ResponseEntity.ok().body("Arquivo enviado com sucesso!");
    }
    
    @GetMapping("/download/{id_curriculo}")
    public ResponseEntity<byte[]> downloadCurriculo(@PathVariable Long id_curriculo) {
        byte[] pdf = curriculoService.downloadCurriculo(id_curriculo);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=curriculo.pdf")
            .header("Content-Type", "application/pdf")
            .body(pdf);
    }

    // Mostra todas as informações do currículo
    @GetMapping("/listar/{id_curriculo}")
    public @ResponseBody ResponseEntity<CurriculoDTO> listarCurriculo(@PathVariable Long id_curriculo) {
        Curriculo curriculo = curriculoService.listarCurriculo(id_curriculo);
        return ResponseEntity.ok().body(CurriculoDTO.valueOf(curriculo));
    }
}
