package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import br.fatec.TemosVagas.repositories.candidato.CurriculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Curriculo listarCurriculo(Long id_curriculo) {
        if (id_curriculo != null && id_curriculo > 0) {
            return curriculoRepository.findById(id_curriculo).orElseThrow(() -> new EntityNotFoundException("Currículo não encontrado."));
        }
        throw new EntityNotFoundException("ID não fornecido ou inválido.");
    }

}
