package br.fatec.TemosVagas.services.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import br.fatec.TemosVagas.services.EmailService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class  CandidatoService {

    @Autowired
    CandidatoRepository candidatoRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    EmailService emailService;

    public Candidato cadastrar(Candidato candidato) {
        if (candidato != null) {
            candidato.setSenha(encoder.encode(candidato.getSenha()));

            try {
                emailService.enviarEmailCadastro(candidato.getEmail(), candidato.getNome());
            }catch (Exception e) {
                System.err.println("Falha ao enviar email" + e.getMessage());
            }

            return candidatoRepository.save(candidato);
        }
        return null;
    }

    //Mantendo por questões de teste
    public Candidato findById(Long id) {
        if (id != null && id > 0) {
            return candidatoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        }
        throw new EntityNotFoundException("ID não especificado.");
    }

    //Simula o carregamento de dados do usuário logado através do contexto do token
    public Candidato carregarUsuarioLogado() {
        Candidato candidato = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return candidatoRepository.findById(candidato.getId())
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado."));
    }

    @Transactional
    public Candidato atualizar(Candidato candidato) {
        if (candidato != null) {
            Candidato candidatoLogado = (Candidato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            //Captura de qualquer classe que for encontrada em candidato, incluindo superclasses.
            //Isso é feito pois o stream só acessa os campos da classe diretamente, sem considerar 
            //os campos herdados de superclasses.
            Class<?> classe = candidato.getClass();
            while (classe != null && classe != Object.class) {
                Arrays.stream(classe.getDeclaredFields())
                    .filter(field -> 
                        !Modifier.isStatic(field.getModifiers()) && 
                        !Modifier.isFinal(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(candidato);
                            if (value != null && !Objects.equals(value, field.get(candidatoLogado))) {
                                field.set(candidatoLogado, value);
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Erro ao acessar o campo: " + field.getName(), e);
                        }
                });
                //Ao final do Array, capturamos o tipo de classe para parar a execução
                classe = classe.getSuperclass();
            }

            return candidatoRepository.save(candidatoLogado);
        }
        return null;
    }

}
