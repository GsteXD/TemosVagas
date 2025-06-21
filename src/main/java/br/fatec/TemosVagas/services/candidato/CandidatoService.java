package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import br.fatec.TemosVagas.security.jwt.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class  CandidatoService {

    @Autowired
    CandidatoRepository candidatoRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    public Candidato cadastrar(Candidato candidato) {
        if (candidato != null) {
            candidato.setSenha(encoder.encode(candidato.getSenha()));
            return candidatoRepository.save(candidato);
        }
        return null;
    }

    public Candidato findById(Long id) {
        if (id != null && id > 0) {
            return candidatoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        }
        throw new EntityNotFoundException("ID não especificado.");
    }


}
