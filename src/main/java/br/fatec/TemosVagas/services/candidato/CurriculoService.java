package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import br.fatec.TemosVagas.repositories.candidato.CurriculoRepository;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CurriculoService {

    @Autowired
    private CurriculoRepository curriculoRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    public Curriculo cadastrar(Curriculo curriculo, Long id_candidato) {
        Candidato candidato = candidatoRepository.findById(id_candidato).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        curriculo.setCandidato(candidato);

        return curriculoRepository.save(curriculo);
    }

    public void uploadCurriculo(Long id_curriculo, MultipartFile file) {
        Curriculo curriculo = listarCurriculo(id_curriculo);

        try {
            curriculo.setArquivoPdf(file.getBytes());
            curriculoRepository.save(curriculo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo PDF:", e);
        }
    }

    public byte[] downloadCurriculo(Long id_curriculo) {
        Curriculo curriculo = listarCurriculo(id_curriculo);
        byte[] pdf = curriculo.getArquivoPdf();

        if (pdf == null || pdf.length == 0) {
            throw new EntityNotFoundException("PDF não encontrado.");
        }

        return pdf;
    }

    public Curriculo listarCurriculo(Long id_curriculo) {
        if (id_curriculo != null && id_curriculo > 0) {
            return curriculoRepository.findById(id_curriculo).orElseThrow(() -> new EntityNotFoundException("Currículo não encontrado."));
        }
        throw new EntityNotFoundException("ID não fornecido ou inválido.");
    }

}
