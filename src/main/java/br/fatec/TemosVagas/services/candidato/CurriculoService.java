package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.repositories.candidato.CurriculoRepository;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CurriculoService {

    @Autowired
    private CurriculoRepository curriculoRepository;

    //A busca de informações desse arquivo é baseado no contexto do usuário logado

    @Transactional(readOnly = true)
    public Curriculo cadastrar(Curriculo curriculo) {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        boolean curriculoExistente = curriculoRepository.findByCandidatoId(candidato.getId()).isPresent();
        if (curriculoExistente) {
            throw new IllegalArgumentException("Candidato já possui um currículo cadastrado.");
        }

        curriculo.setCandidato(candidato);

        return curriculoRepository.save(curriculo);
    }

    @Transactional(readOnly = true)
    public void uploadCurriculo(MultipartFile file) {
        Curriculo curriculo = listarCurriculo();

        try {
            if (!"application/pdf".equals(file.getContentType())) {
                throw new IllegalArgumentException("Arquivo inválido. Envie um PDF.");
            }

            curriculo.setArquivoPdf(file.getBytes());
            curriculoRepository.save(curriculo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo PDF:", e);
        }
    }

    @Transactional(readOnly = true)
    public byte[] downloadCurriculo() {
        Curriculo curriculo = listarCurriculo();
        byte[] pdf = curriculo.getArquivoPdf();

        System.out.println(pdf);

        if (pdf == null || pdf.length == 0) {
            throw new EntityNotFoundException("PDF não encontrado.");
        }

        return pdf;
    }

    @Transactional(readOnly = true)
    public Curriculo listarCurriculo() {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return curriculoRepository.findByCandidatoId(candidato.getId())
                .orElseThrow(() -> new EntityNotFoundException("Currículo não encontrado para este candidato. Tente cadastrar primeiro."));

    }

}
