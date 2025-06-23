package br.fatec.TemosVagas.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.fatec.TemosVagas.entities.Status;
import br.fatec.TemosVagas.entities.Vaga;
import br.fatec.TemosVagas.entities.enums.TipoStatus;
import br.fatec.TemosVagas.repositories.StatusRepository;
import br.fatec.TemosVagas.repositories.VagaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class VagaService {
    
    @Autowired
    VagaRepository vagaRepository;

    @Autowired
    private StatusRepository statusRepository;

    public Vaga cadastrar(Vaga vaga) {
        if (vaga != null) {
            String tipo = vaga.getTipo() != null ? vaga.getTipo().toLowerCase() : "";

            switch (tipo) {
                case "estágio" -> {
                    if (vaga.getCursoConclusao() != null) {
                        throw new IllegalArgumentException(
                            "O ano de conclusão do curso não é pertinente para vagas de estágio."
                        );
                    }
                    if (vaga.getSemestre() == null || vaga.getCurso() == null) {
                        throw new IllegalArgumentException(
                            "Para vagas de estágio, é obrigatório informar o semestre e o curso."
                        );
                    }
                }
                case "trainee" -> {
                    if (vaga.getCursoConclusao() == null) {
                        throw new IllegalArgumentException(
                            "Para vagas de trainee, é obrigatório informar o ano de conclusão do curso."
                        );
                    }
                }
                default -> {
                    if (vaga.getDescricao() == null || vaga.getDescricao().isBlank()) {
                        throw new IllegalArgumentException(
                            "Para esta vaga, é obrigatório informar os requisitos mínimos e desejáveis na descrição."
                        );
                    }
                }
            }

            Status status = statusRepository.findById(TipoStatus.ABERTO)
                .orElseThrow(() -> new EntityNotFoundException("Status 'ABERTO' não encontrado."));

            vaga.setDataPub(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            vaga.setStatus(status);
            return vagaRepository.save(vaga);
        }
        return null;
    }

}
