package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.entities.candidato.Formacao;
import br.fatec.TemosVagas.repositories.candidato.CurriculoRepository;
import br.fatec.TemosVagas.repositories.candidato.FormacaoRepository;
import jakarta.persistence.EntityNotFoundException;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormacaoService {

    @Autowired
    FormacaoRepository formacaoRepository;

    @Autowired
    CurriculoRepository curriculoRepository;

    @Transactional
    public Formacao cadastrar(Formacao formacao) {
        if (formacao != null) {
            Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Curriculo curriculo = curriculoRepository.findByCandidatoId(candidato.getId()).orElseThrow(() -> new EntityNotFoundException("Curriculo nao encontrado."));

            formacao.setCurriculo(curriculo);

            // Adiciona essa formação na listagem da entidade currículo
            curriculo.getListaFormacao().add(formacao);

            return formacaoRepository.save(formacao);
        }
       throw new EntityNotFoundException("Dados da formação não encontrados no corpo da requisição.");
    }

    @Transactional
    public void deletar(Long id) {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Curriculo curriculo = curriculoRepository.findByCandidatoId(candidato.getId())
            .orElseThrow(() -> new EntityNotFoundException("Currículo não encontrado."));
        if (id != null && candidato != null) {
            Formacao formacao = formacaoRepository.findByIdAndCurriculoId(id, curriculo.getId())
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada para este usuário."));

            formacao.setCurriculo(null);

            curriculoRepository.findByCandidatoId(candidato.getId()).ifPresent(
                    c -> c.getListaFormacao().remove(formacao)
            );

            formacaoRepository.delete(formacao);
        } else {
            throw new EntityNotFoundException("ID da formação não fornecido.");
        }
        
    }

    @Transactional(readOnly = true)
    public List<Formacao> listarFormacoes() {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (candidato != null) {
            Curriculo curriculo = curriculoRepository.findByCandidatoId(candidato.getId()).orElseThrow(() -> new EntityNotFoundException("Currículo não encontrado."));

            return curriculo.getListaFormacao();
        }
        throw new EntityNotFoundException("ID não fornecido ou inválido.");
    }

    @Transactional
    public Formacao atualizar(Formacao formacao, Long id) {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (formacao != null) {
            Curriculo curriculo = curriculoRepository.findByCandidatoId(candidato.getId())
                .orElseThrow(() -> new EntityNotFoundException("Currículo não encontrado."));
            Formacao formacaoExistente = formacaoRepository.findByIdAndCurriculoId(id, curriculo.getId())
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada para este usuário."));

            Arrays.stream(Formacao.class.getDeclaredFields())
                .filter(field ->
                    !Modifier.isStatic(field.getModifiers()) &&
                    !Modifier.isFinal(field.getModifiers()))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(formacao);
                        if (value != null && !value.equals(field.get(formacaoExistente))) {
                            field.set(formacaoExistente, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Erro ao acessar o campo: " + field.getName(), e);
                    }
                });

            return formacaoRepository.save(formacaoExistente);
        }
        return null;

    }

}
