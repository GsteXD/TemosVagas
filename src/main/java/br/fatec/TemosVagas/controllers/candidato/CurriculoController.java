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

    @PostMapping("/cadastrar")
    public @ResponseBody ResponseEntity<CurriculoDTO> cadastrar(@RequestBody CurriculoDTO curriculoDTO) {
        Curriculo curriculo = curriculoService.cadastrar(CurriculoDTO.toCurriculo(curriculoDTO));
        return ResponseEntity.ok().body(CurriculoDTO.valueOf(curriculo));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCurriculo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().body("Arquivo inválido. Envie um PDF.");
        }
        curriculoService.uploadCurriculo(file);

        return ResponseEntity.ok().body("Arquivo enviado com sucesso!");
    }
    
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadCurriculo() {
        byte[] pdf = curriculoService.downloadCurriculo();

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=curriculo.pdf")
            .header("Content-Type", "application/pdf")
            .body(pdf);
    }

    // Mostra todas as informações do currículo
    @GetMapping("/listar")
    public @ResponseBody ResponseEntity<CurriculoDTO> listarCurriculo() {
        Curriculo curriculo = curriculoService.listarCurriculo();
        return ResponseEntity.ok().body(CurriculoDTO.valueOf(curriculo));
    }
}
