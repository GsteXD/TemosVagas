package br.fatec.TemosVagas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class InMemoryUserConfig {

    // Tá hard coded, depois eu vejo como colocar no banco
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
        UserDetails user = User.withUsername("admin")
                .password(encoder.encode("admin"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
