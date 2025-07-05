package br.fatec.TemosVagas.services;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.fatec.TemosVagas.entities.Empresa;
import br.fatec.TemosVagas.entities.Status;
import br.fatec.TemosVagas.entities.Vaga;
import br.fatec.TemosVagas.entities.enums.TipoStatus;
import br.fatec.TemosVagas.repositories.StatusRepository;
import br.fatec.TemosVagas.repositories.VagaRepository;
import br.fatec.TemosVagas.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Configuration
@EnableScheduling
public class VagaService {
    
    @Autowired
    VagaRepository vagaRepository;

    @Autowired
    private StatusRepository statusRepository;

    public Vaga cadastrar(Vaga vaga) {
        if (vaga != null) {
            Empresa empresa = (Empresa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            validarVaga(vaga);

            Status status = statusRepository.findById(TipoStatus.ABERTO)
                .orElseThrow(() -> new EntityNotFoundException("Status 'ABERTO' não encontrado."));

            vaga.setDataPub(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            vaga.setStatus(status);
            vaga.setEmpresa(empresa);
            return vagaRepository.save(vaga);
        }
        return null;
    }

    @Transactional
    public Vaga atualizar(Long id, Vaga vaga) {
        if (vaga != null) {
            validarVaga(vaga);

            Empresa empresa = (Empresa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Vaga vagaExistente = vagaRepository.findByIdAndEmpresaId(id, empresa.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada ou não pertencente à empresa."));
                
            //Verifica se a dataLimite foi prorrogada
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate novaDataLimite = LocalDate.parse(vaga.getDataLimite(), formatter);
            LocalDate dataLimiteAtual = LocalDate.parse(vagaExistente.getDataLimite(), formatter);
            LocalDate hoje = LocalDate.now();

            //Seta o status como PRORROGADO caso a condição seja verdadeira
            if (!novaDataLimite.equals(dataLimiteAtual) && novaDataLimite.isAfter(hoje)) {
                vagaExistente.setStatus(statusRepository.findById(TipoStatus.PRORROGADO)
                    .orElseThrow(() -> new EntityNotFoundException("Status 'PRORROGADO' não encontrado.")));
            }

            Arrays.stream(Vaga.class.getDeclaredFields())
                .filter(field -> 
                    !Modifier.isStatic(field.getModifiers()) && 
                    !Modifier.isFinal(field.getModifiers()))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(vaga);
                        if (value != null) {
                            field.set(vagaExistente, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Erro ao acessar o campo: " + field.getName(), e);
                    }
            });

            return vagaRepository.save(vagaExistente);
        }
        return null;
    }

    public Vaga findById(Long id) {
        if (id != null && id > 0) {
            return vagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada."));
        }
        throw new EntityNotFoundException("ID não especificado.");
    }

    //Validação da vaga a depender do tipo que for passado no corpo
    private void validarVaga(Vaga vaga) {
        Boolean dataValida = DateUtils.DataAtualOuFutura(vaga.getDataLimite());
        if (!dataValida) {
            throw new IllegalArgumentException("A data limite deve ser igual ou posterior à data atual.");
        }

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
    }

    @Transactional(readOnly = true)
    public List<Vaga> listarVagasAbertas() {
        return vagaRepository.findByStatusStatus(TipoStatus.ABERTO);
    }

    //Fecha automaticamente as vagas que estão com a data limite vencida
    // O fechamento ocorre diariamente à meia-noite
    @Scheduled(cron = "0 0 0 * * ?") 
    @Transactional
    protected void fecharVagasVencidas() {
        List<Vaga> vagasAbertas = vagaRepository.findByStatusStatus(TipoStatus.ABERTO);
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Vaga vaga : vagasAbertas) {
            LocalDate dataLimite = LocalDate.parse(vaga.getDataLimite(), formatter);
            if (!dataLimite.isAfter(hoje)) {
                vaga.setStatus(
                    statusRepository.findById(TipoStatus.FECHADO).orElseThrow(() ->
                        new EntityNotFoundException("Status 'FECHADO' não encontrado."))
                );
                vagaRepository.save(vaga);
            }
        }
    }

    //Caso a empresa necessite controlar o status da vaga manualmente
    public void controlarStatus(Long id, TipoStatus status) {
        if (id != null && id > 0) {
            Empresa empresa = (Empresa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Vaga vaga = vagaRepository.findByIdAndEmpresaId(id, empresa.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada ou não pertencente à empresa."));
            switch (status) {
                case ABERTO:
                    vaga.setStatus(statusRepository.findById(TipoStatus.ABERTO)
                        .orElseThrow(() -> new EntityNotFoundException("Status 'ABERTO' não encontrado.")));
                    break;
                case FECHADO:
                    vaga.setStatus(statusRepository.findById(TipoStatus.FECHADO)
                        .orElseThrow(() -> new EntityNotFoundException("Status 'FECHADO' não encontrado.")));
                    break;
                default:
                    throw new IllegalArgumentException("Status inválido. Defina o status como ABERTO ou FECHADO.");
            }
            vagaRepository.save(vaga);
        } else {
            throw new EntityNotFoundException("ID não especificado.");
        }
    }

}
