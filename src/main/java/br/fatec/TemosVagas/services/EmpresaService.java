package br.fatec.TemosVagas.services;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.fatec.TemosVagas.entities.Empresa;
import br.fatec.TemosVagas.repositories.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EmpresaService {
    
    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    public Empresa cadastrar(Empresa empresa) {
        if (empresa != null) {
            empresa.setSenha(encoder.encode(empresa.getSenha()));
            return empresaRepository.save(empresa);
        }
        return null;
    }

    public Empresa findById(Long id) {
        if (id != null && id > 0) {
            return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
        }
        throw new EntityNotFoundException("ID incorreto ou não especificado.");
    }

    public Empresa carregarEmpresaLogada() {
        Empresa empresa = (Empresa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return empresaRepository.findById(empresa.getId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    @Transactional
    public Empresa atualizar(Empresa empresa) {
        if (empresa != null) {
            Empresa empresaLogada = (Empresa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            //Captura de qualquer classe que for encontrada em empresa, incluindo superclasses.
            //Isso é feito pois o stream só acessa os campos da classe diretamente, sem considerar 
            //os campos herdados de superclasses.
            Class<?> classe = empresa.getClass();
            while (classe != null && classe != Object.class) {
                Arrays.stream(classe.getDeclaredFields())
                    .filter(field -> 
                        !Modifier.isStatic(field.getModifiers()) && 
                        !Modifier.isFinal(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(empresa);
                            if (value != null && !Objects.equals(value, field.get(empresaLogada))) {
                                field.set(empresaLogada, value);
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Erro ao acessar o campo: " + field.getName(), e);
                        }
                });
                //Ao final do Array, capturamos o tipo de classe para parar a execução
                classe = classe.getSuperclass();
            }

            return empresaRepository.save(empresaLogada);
        }
        return null;
    }

}
