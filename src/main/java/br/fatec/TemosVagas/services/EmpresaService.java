package br.fatec.TemosVagas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.fatec.TemosVagas.entities.Empresa;
import br.fatec.TemosVagas.repositories.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;

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

}
