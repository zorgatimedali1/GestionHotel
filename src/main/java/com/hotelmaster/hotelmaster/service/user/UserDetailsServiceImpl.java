package com.hotelmaster.hotelmaster.service.user;

import com.hotelmaster.hotelmaster.entities.AppUser;
import com.hotelmaster.hotelmaster.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable : " + username));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                // getRole() retourne "ROLE_ADMIN" → Spring extrait "ADMIN"
                .roles(user.getRole().replace("ROLE_", ""))
                .disabled(!user.isActif())
                .build();
    }
}
