package com.hotelmaster.hotelmaster.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Entre 3 et 50 caractères")
    @Column(unique = true, nullable = false)
    private String username;

    /** Stocké en BCrypt — jamais en clair */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    /** Permet de désactiver un compte sans le supprimer */
    @Builder.Default
    private boolean actif = true;

    /** Prénom / nom pour affichage — optionnel */
    private String nomComplet;
}
