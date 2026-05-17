package com.hotelmaster.hotelmaster.security;

import com.hotelmaster.hotelmaster.entities.AppUser;
import com.hotelmaster.hotelmaster.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Créer admin si absent
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .nomComplet("Administrateur Principal")
                    .actif(true)
                    .build());
            System.out.println("✅ Compte ADMIN créé : admin / admin123");
        }

        // Créer un réceptionniste de démo si absent
        if (!userRepository.existsByUsername("receptionist")) {
            userRepository.save(AppUser.builder()
                    .username("receptionist")
                    .password(passwordEncoder.encode("recep123"))
                    .role("ROLE_RECEPTIONIST")
                    .nomComplet("Réceptionniste Démo")
                    .actif(true)
                    .build());
            System.out.println("✅ Compte RECEPTIONIST créé : receptionist / recep123");
        }
    }
}
