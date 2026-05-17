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
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le numéro de chambre est obligatoire")
    private String numero;

    @NotBlank(message = "Le type de chambre est obligatoire")
    private String type;

    @NotNull(message = "Le prix par nuit est obligatoire")
    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private Double prixParNuit;

    private Boolean disponible;

    private String photo;

    /** Relation N:1 — plusieurs chambres appartiennent à un hôtel */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    /**
     * Relation 1:1 inverse — côté non-owning
     * mappedBy pointe vers le champ "chambre" de DetailsChambre
     */
    @OneToOne(mappedBy = "chambre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DetailsChambre detailsChambre;

    /** Relation N:N — une chambre peut avoir plusieurs réservations */
    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}
