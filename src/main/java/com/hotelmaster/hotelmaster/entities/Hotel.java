package com.hotelmaster.hotelmaster.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'hôtel est obligatoire")
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    @NotNull(message = "Le nombre d'étoiles est obligatoire")
    @Min(value = 1, message = "Le nombre d'étoiles doit être au moins 1")
    private Integer etoiles;

    private String telephone;

    private String email;

    /** Image principale de l'hôtel (nom du fichier stocké) */
    private String photo;

    /** Relation 1:N — un hôtel possède plusieurs chambres */
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chambre> chambres;
}
