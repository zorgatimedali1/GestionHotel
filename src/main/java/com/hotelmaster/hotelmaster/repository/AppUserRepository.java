package com.hotelmaster.hotelmaster.repository;

import com.hotelmaster.hotelmaster.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    /** Récupère tous les réceptionnistes (pour la liste admin) */
    List<AppUser> findByRole(String role);
}
