package com.hotelmaster.hotelmaster.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * ✅ Méthode 2 — JPA UserDetailsService
     * Spring Boot auto-détecte UserDetailsServiceImpl (@Service implémentant UserDetailsService)
     * Plus besoin de déclarer le bean UserDetailsService ici.
     * Le seul bean nécessaire est PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/uploads/**", "/webjars/**").permitAll()
                .requestMatchers("/login", "/login?error", "/login?logout").permitAll()

                // ── ADMIN uniquement ──────────────────────────────────────────
                .requestMatchers("/hotels/supprimer/**",
                                 "/chambres/supprimer/**",
                                 "/reservations/supprimer/**",
                                 "/services-extra/supprimer/**").hasRole("ADMIN")
                .requestMatchers("/hotels/nouveau", "/hotels/save",
                                 "/hotels/modifier/**", "/hotels/update").hasRole("ADMIN")
                // Gestion des comptes réceptionnistes
                .requestMatchers("/admin/users/**").hasRole("ADMIN")
                // Dashboard complet → ADMIN seulement (RECEPTIONIST a sa propre page d'accueil)
                .requestMatchers("/dashboard").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                // ✅ Redirection différenciée par rôle
                .successHandler((req, res, auth) -> {
                    boolean isAdmin = auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    res.sendRedirect(isAdmin ? "/dashboard" : "/receptionist/home");
                })
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/acces-refuse")
            );

        return http.build();
    }
}
