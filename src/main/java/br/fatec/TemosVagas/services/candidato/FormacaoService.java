package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Curriculo;
import br.fatec.TemosVagas.entities.candidato.Formacao;
import br.fatec.TemosVagas.repositories.candidato.CurriculoRepository;
import br.fatec.TemosVagas.repositories.candidato.FormacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormacaoService {

    @Autowired
    FormacaoRepository formacaoRepository;

    @Autowired
    CurriculoRepository curriculoRepository;

    @Transactional
    public Formacao cadastrar(Formacao formacao, Long id_curriculo) {
        if (formacao != null && id_curriculo != null) {
            Curriculo curriculo = curriculoRepository.findById(id_curriculo).orElseThrow(() -> new EntityNotFoundException("Curriculo nao encontrado."));

            formacao.setCurriculo(curriculo);

            // Adiciona essa formação na listagem da entidade currículo
            curriculo.getListaFormacao().add(formacao);

            return formacaoRepository.save(formacao);
        }
       throw new EntityNotFoundException("Dados da formação não encontrados no corpo da requisição.");
    }

    @Transactional
    public void deletar(Long id, Long id_curriculo) {
        if (id != null && id_curriculo != null) {
            Formacao formacao = formacaoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Formação nao encontrada."));

            formacao.setCurriculo(null);

            curriculoRepository.findById(id_curriculo).ifPresent(
                    curriculo -> curriculo.getListaFormacao().remove(formacao)
            );

            formacaoRepository.delete(formacao);
        }
        throw new EntityNotFoundException("IDs não fornecidos.");
    }

}
