package br.fatec.TemosVagas.security.jwt;

import br.fatec.TemosVagas.dtos.autenticacao.LoginRequest;
import br.fatec.TemosVagas.dtos.autenticacao.LoginResponse;
import br.fatec.TemosVagas.entities.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public LoginResponse autenticar(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.senha()
                )
        );

        var usuario = (Usuario) userDetailsService.loadUserByUsername(loginRequest.email());
        var token = jwtService.gerarToken(usuario);

        return new LoginResponse(
                token,
                usuario.getNome(),
                usuario.getRole().name()
        );
    }
}
