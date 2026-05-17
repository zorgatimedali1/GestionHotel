package com.hotelmaster.hotelmaster.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailsChambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La superficie est obligatoire")
    @Min(value = 5, message = "La superficie doit être d'au moins 5 m²")
    private Double superficie;

    private Boolean climatisation;

    private Boolean balcon;

    private Boolean vueMer;

    private Boolean wifiInclus;

    private Integer nombreLits;

    private String typeVue;

    private String descriptionEquipements;

    /**
     * Relation 1:1 — owning side (porte la clé étrangère)
     */
    @OneToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;
}
