package br.fatec.TemosVagas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

    //TODO: melhorar a autenticação de rotas

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // Controle da autorização de requests, por enquanto básico, só para testes
                .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers(HttpMethod.POST,"/candidato/cadastrar").permitAll()
                            .anyRequest().authenticated()
                )
                .cors(cors -> cors.configurationSource(request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.addAllowedOrigin("*");
                        config.addAllowedHeader("*");
                        config.addAllowedMethod("*");
                        return config;
                    })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults()); //Habilita a autenticação básica

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
