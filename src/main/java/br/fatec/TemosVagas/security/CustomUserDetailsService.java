package br.fatec.TemosVagas.security;

import br.fatec.TemosVagas.entities.Empresa;
import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.repositories.EmpresaRepository;
import br.fatec.TemosVagas.repositories.candidato.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Candidato> candidato = candidatoRepository.findByEmail(email);
        if(candidato.isPresent()) {
            return candidato.get();
        }
        Optional<Empresa> empresa = empresaRepository.findByEmail(email);
        if(empresa.isPresent()) {
            return empresa.get();
        }
        throw new UsernameNotFoundException("Usuario não encontrado "+ email);
    }
}
