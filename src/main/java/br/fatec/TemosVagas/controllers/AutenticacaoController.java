package br.fatec.TemosVagas.controllers;

import br.fatec.TemosVagas.dtos.autenticacao.LoginRequest;
import br.fatec.TemosVagas.dtos.autenticacao.LoginResponse;
import br.fatec.TemosVagas.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/autenticar")
public class AutenticacaoController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.autenticar(loginRequest));
    }
}
