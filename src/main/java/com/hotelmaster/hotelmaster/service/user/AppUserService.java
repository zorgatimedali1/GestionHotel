package com.hotelmaster.hotelmaster.service.user;

import com.hotelmaster.hotelmaster.entities.AppUser;
import com.hotelmaster.hotelmaster.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserService implements IAppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createReceptionist(String username, String password, String nomComplet) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Le nom d'utilisateur « " + username + " » est déjà pris.");
        }
        AppUser user = AppUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_RECEPTIONIST")
                .nomComplet(nomComplet)
                .actif(true)
                .build();
        userRepository.save(user);
    }

    @Override
    public void updateReceptionist(Long id, String username, String nomComplet, boolean actif) {
        AppUser user = getUser(id);
        // Vérifier unicité seulement si le username change
        if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new RuntimeException("Le nom d'utilisateur « " + username + " » est déjà pris.");
        }
        user.setUsername(username);
        user.setNomComplet(nomComplet);
        user.setActif(actif);
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        AppUser user = getUser(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public AppUser getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : id=" + id));
    }

    @Override
    public List<AppUser> getAllReceptionists() {
        return userRepository.findByRole("ROLE_RECEPTIONIST");
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
