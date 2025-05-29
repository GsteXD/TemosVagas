package br.fatec.TemosVagas.services;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidatoService {

    @Autowired
    CandidatoRepository candidatoRepository;

    public Candidato cadastrar(Candidato candidato) {
        if (candidato != null) {
            return candidatoRepository.save(candidato);
        }
        return null;
    }

    public Candidato findById(Long id) {
        if (id != null && id > 0) {
            return candidatoRepository.findById(id).orElse(null);
        }
        return null;
    }

    public Candidato login(String email, String senha) {
        if (email != null && senha != null) {
            //TODO: Colocar um serviço de autenticação aqui
            return candidatoRepository.findByEmail(email).orElse(null);
        }
        return null;
    }

}
