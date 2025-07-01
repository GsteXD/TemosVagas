package br.fatec.TemosVagas.services;

import br.fatec.TemosVagas.entities.Aplicacao;
import br.fatec.TemosVagas.entities.Vaga;
import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.entities.candidato.Formacao;
import br.fatec.TemosVagas.entities.enums.StatusAplicacao;
import br.fatec.TemosVagas.repositories.AplicacaoRepository;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import br.fatec.TemosVagas.repositories.candidato.CurriculoRepository;
import br.fatec.TemosVagas.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.List;

@Service
public class AplicacaoService {

    @Autowired
    private AplicacaoRepository aplicacaoRepository;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private CurriculoRepository curriculoRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Aplicacao aplicarVaga(Long vagaId, String informacoesAdicionais) {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Vaga vaga = vagaService.findById(vagaId);
        //verifica se o candidato ja aplicou para esta vaga.
        Optional<Aplicacao> aplicacaoExistente = aplicacaoRepository.findByCandidato_IdAndVaga_Id(candidato.getId(), vagaId);
        if(aplicacaoExistente.isPresent()) {
            throw new IllegalStateException("Você já se candidatou para esta vaga.");
        }

        validarElegibilidadeCandidato(candidato, vaga);

        Aplicacao aplicacao = new Aplicacao();
        aplicacao.setCandidato(candidato);
        aplicacao.setVaga(vaga);
        aplicacao.setInformacoesAdicionais(informacoesAdicionais);

        return aplicacaoRepository.save(aplicacao);
    }



    //direciona qual a validação a depender do tipo de vaga.
    private void validarElegibilidadeCandidato(Candidato candidato, Vaga vaga) {
        String tipoVaga = vaga.getTipo().toLowerCase();

        boolean temCurriculo = curriculoRepository.existsByCandidato_Id(candidato.getId());
        if(!temCurriculo) {
            throw new IllegalStateException("É necessario ter um curriculo cadastrado.");
        }

        Optional<Curriculo> curricoloOpt = curriculoRepository.findByCandidatoId(candidato.getId());

        switch (tipoVaga) {
            case "estágio" -> validarElegibilidadeEstagio(candidato, vaga, curricoloOpt);
            case "trainee" -> validarElegibilidadeTrainee(candidato, vaga, curricoloOpt);
            default -> validarEligibilidadeGeral(candidato, vaga, curricoloOpt);
        }
    }

    private void validarElegibilidadeEstagio(Candidato candidato, Vaga vaga, Optional<Curriculo> curriculoOpt) {
        if (curriculoOpt.isEmpty()) {
            throw new IllegalStateException("É necessario ter um curriculo cadastrado.");
        }
        Curriculo curriculo = curriculoOpt.get();
        List<Formacao> formacoes = curriculo.getListaFormacao();

        if(formacoes.isEmpty()) {
            throw new IllegalStateException("É necessario ter formações cadastrada no curriculo para vagas de estágio.");
        }
        //verifica se o candidato esta em uma graduação,
        boolean graduando = formacoes.stream()
                .anyMatch(formacao -> {
                    String tipoDiploma = formacao.getTipoDiploma().toLowerCase();
                    return ((tipoDiploma.contains("graduação")
                            || tipoDiploma.contains("bacharelado")
                            || tipoDiploma.contains("tecnólogo")
                            || tipoDiploma.contains("licenciatura"))
                            && DateUtils.ehMesAtualOuPosterior(formacao.getDataFim()));
                });
        if (!graduando) {
            throw new IllegalStateException("É preciso estar fazendo uma graduação para se candidatar a vagas de estágio.");
        }

        // verificar se o curso do candidato é compativel com a vaga
        if(vaga.getCurso() != null && !vaga.getCurso().isBlank()) {
            boolean cursoCompativel = formacoes.stream()
                    .anyMatch(formacao -> {
                        String cursoFormacao = formacao.getCurso().toLowerCase();
                        String cursoVaga = vaga.getCurso().toLowerCase();
                        return cursoFormacao.contains(cursoVaga) || cursoVaga.contains(cursoFormacao);
                    });

            if (!cursoCompativel) {
                throw new IllegalStateException("O curso não é compativel com o exigido pela vaga.");
            }
            //chama o metodo que sera responsavel por ver se o semestre do candidato é compativel com o requerido.
            validarSemestre(formacoes, vaga);
        }
    }

    private void validarElegibilidadeTrainee(Candidato candidato, Vaga vaga, Optional<Curriculo> curriculoOpt) {
        if (curriculoOpt.isEmpty()) {
            throw new IllegalStateException("É necessario ter um curriculo cadastrado.");
        }
        Curriculo curriculo = curriculoOpt.get();
        List<Formacao> formacoes = curriculo.getListaFormacao();

        if(formacoes.isEmpty()) {
            throw new IllegalStateException("É necessario ter formações cadastrada no curriculo para vagas de trainee.");
        }
        //verifica se o candidato ja se formou.
        boolean graduacaoConcluida = formacoes.stream()
                .anyMatch(formacao -> {
                    String tipoDiploma = formacao.getTipoDiploma().toLowerCase();
                    return ((tipoDiploma.contains("graduação")
                            || tipoDiploma.contains("bacharelado")
                            || tipoDiploma.contains("tecnólogo")
                            || tipoDiploma.contains("licenciatura"))
                            && DateUtils.ehMesAnterior(formacao.getDataFim()));
                });
        if (!graduacaoConcluida) {
            throw new IllegalStateException("É preciso ter concluido uma graduação para se candidatar a vagas de trainee.");
        }

        // verificar se a graduação foi recente
        if(vaga.getCursoConclusao() != null) {
            int anoConclusaoVaga = Integer.parseInt(vaga.getCursoConclusao());//converte o valor de string para int.
            boolean conclusaoRecente = formacoes.stream()
                    .filter(formacao -> DateUtils.ehMesAnterior(formacao.getDataFim()))//mantém somente as graduações ja concluidas.
                    .anyMatch(formacao -> {
                        Integer anoConclusaoCandidato = DateUtils.extrairAno(formacao.getDataFim());//pega somente o ano da conclusão.
                        return anoConclusaoCandidato != null && anoConclusaoCandidato >= anoConclusaoVaga;
                    });

            if(!conclusaoRecente) {
                throw new IllegalStateException("Seu ano de formação n atende o requisitado para a vaga.");
            }
        }
    }
    //validação generica para vagas que n são estágio e nem trainee.
    private void validarEligibilidadeGeral(Candidato candidato, Vaga vaga, Optional<Curriculo> curriculoOpt) {
        if (curriculoOpt.isEmpty()) {
            throw new IllegalStateException("É necessario ter um curriculo cadastrado.");
        }
    }



    private void validarSemestre(List<Formacao> formacoes, Vaga vaga) {
        if(vaga.getSemestre() == null || vaga.getSemestre().isBlank()) return;//não faz nada se a vaga n exige semestre.

        try {
            //extrai o numero do semestre da string e remova os caracteres que não forem números.
            int semestreRequirido = Integer.parseInt(vaga.getSemestre().replaceAll("\\D", ""));
            //filtra as formações em andamento e verifica se alguma delas tem o semestre compativel.
            boolean semestreCompativel = formacoes.stream()
                    .filter(f -> DateUtils.ehMesAtualOuPosterior(f.getDataFim()))
                    .anyMatch(f-> {int semestreCandidato = calcularSemestre(f.getDataInicio());
                        return semestreCandidato >= semestreRequirido;
                    });

            if(!semestreCompativel) {
                throw new IllegalStateException("É necessario estar no " + vaga.getSemestre() + ".");
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Formato de semestre inválido na vaga: " + vaga.getSemestre());
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao validar semestre: " + e.getMessage());
        }
    }
    //faz o calculo do semestre do candidato.
    private int calcularSemestre(String dataInicioString) {
        YearMonth dataInicio = DateUtils.converterParaAnoMes(dataInicioString);
        if(dataInicio == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR"));
            dataInicio = YearMonth.parse(dataInicioString, formatter);
        }
        //chronoUnit calcula a diferenca em meses entre as duas datas.
        long mesesCurso = dataInicio.until(YearMonth.now(), ChronoUnit.MONTHS);
        //vai dividir por 6 e arredondar para cima usando o ceil arredonda para cima o resultado
        return (int) Math.ceil((double) mesesCurso / 6);
    }



    //lista as aplicações do candidato;
    @Transactional(readOnly = true)
    public List<Aplicacao> listarAplicacoesCandidato() {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return aplicacaoRepository.findByCandidato_Id(candidato.getId());
    }
    //lista todas as aplicações que foram feitas para uma vaga.
    @Transactional(readOnly = true)
    public List<Aplicacao> listarAplicacoesVaga(Long vagaId) {
        return aplicacaoRepository.findByVaga_Id(vagaId);
    }
    //atualiza o status de uma aplicação.


    //TODO: ao mudar o status da aplicação para aprovado, mudar o status da vaga para fechado?
    //TODO: ao mudar o status da aplicação para aprovado, mudar o status da vaga para fechado?
    @Transactional
    public Aplicacao atualizarStatusAplicacao(Long aplicacaoId, StatusAplicacao novoStatus) {
        Aplicacao aplicacao = aplicacaoRepository.findById(aplicacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Aplicação não encontrada."));

        if(aplicacao.getStatusAplicacao() != novoStatus){
            StatusAplicacao statusAnterior = aplicacao.getStatusAplicacao();
            aplicacao.setStatusAplicacao(novoStatus);
            Aplicacao aplicacaoAtualizada = aplicacaoRepository.save(aplicacao);

            notificarMudancaStatus(aplicacaoAtualizada);

            return aplicacaoAtualizada;
        }
        return aplicacao;
    }

    /*envia o email após acontecer alguma mudança no status da aplicação, deixei fora do método de atualizar
    * por medo de dar algum conflito com o transactional caso o email falhe.         */
    @Async
    public void notificarMudancaStatus(Aplicacao aplicacao) {
        try {
            Candidato candidato = aplicacao.getCandidato();
            Vaga vaga = aplicacao.getVaga();

            emailService.enviarEmailAttStatus(candidato.getEmail(), candidato.getNome(),
                    vaga.getTitulo(), vaga.getEmpresa().getNome(), aplicacao.getStatusAplicacao());
        }catch (Exception e) {
            throw new RuntimeException("Falha ao enviar email de atualização de status", e);
        }
    }

}
