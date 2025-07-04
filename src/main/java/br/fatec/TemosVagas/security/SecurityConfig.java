package br.fatec.TemosVagas.security;

import br.fatec.TemosVagas.security.jwt.UserAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserAuthenticationFilter userAuthenticationFilter) throws Exception {
        http //Filtro de autenticação de rotas
                .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers(HttpMethod.POST,"/candidato/cadastrar").permitAll()
                            .requestMatchers(HttpMethod.POST, "/empresa/cadastrar").permitAll()
                            .requestMatchers(HttpMethod.POST,"/autenticar/login").permitAll()
                            .requestMatchers("/empresa/**").hasRole("EMPRESA")
                            .requestMatchers("/vaga/cadastrar", "/vaga/atualizar", "/vaga/status").hasRole("EMPRESA")
                            .requestMatchers("/relatorio/**").hasRole("EMPRESA")
                            .requestMatchers(HttpMethod.GET,"/vaga/listar").hasRole("CANDIDATO")
                            .requestMatchers("/candidato/**").hasRole("CANDIDATO")
                            //rotas de aplicação.
                            .requestMatchers(HttpMethod.POST, "/aplicacao/candidatar").hasRole("CANDIDATO")
                            .requestMatchers(HttpMethod.GET,"/aplicacao/minhasCandidaturas").hasRole("CANDIDATO")
                            .requestMatchers(HttpMethod.GET,"/aplicacao/vaga/**").hasRole("EMPRESA")
                            .requestMatchers(HttpMethod.PUT,"/aplicacao/status/**").hasRole("EMPRESA")
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
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
