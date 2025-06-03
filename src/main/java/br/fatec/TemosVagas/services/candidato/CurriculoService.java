package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.entities.candidato.Formacao;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import br.fatec.TemosVagas.repositories.candidato.CurriculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<Formacao> listarFormacoes(Long id) {
        if (id != null && id > 0) {
            Curriculo curriculo = curriculoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Currículo não encontrado."));

            return curriculo.getListaFormacao();
        }
        throw new EntityNotFoundException("ID não fornecido ou inválido.");
    }

}
