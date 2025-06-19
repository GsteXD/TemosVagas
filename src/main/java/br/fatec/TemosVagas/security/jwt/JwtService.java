package br.fatec.TemosVagas.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String chavePrivada;


    public String gerarToken(UserDetails usuario) {
        try {
            return Jwts.builder()
                    .setIssuer("temos-vagas-api")
                    .setSubject(usuario.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(gerarDataValidade()))
                    .signWith(Keys.hmacShaKeyFor(chavePrivada.getBytes()), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar token", e);
        }
    }

    public String validarToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(chavePrivada.getBytes()))
                    .requireIssuer("temos-vagas-api")
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean tokenValido(String token, UserDetails usuario) {
        String email = validarToken(token);
        return email != null && !email.isEmpty() && email.equals(usuario.getUsername());
    }

    private Instant gerarDataValidade() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
