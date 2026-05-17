package com.hotelmaster.hotelmaster.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du client est obligatoire")
    private String nomClient;

    @NotBlank(message = "Le prénom du client est obligatoire")
    private String prenomClient;

    @NotBlank(message = "L'email du client est obligatoire")
    private String emailClient;

    @NotNull(message = "La date d'arrivée est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateArrivee;

    @NotNull(message = "La date de départ est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDepart;

    /** ex: "CONFIRMÉE", "EN_ATTENTE", "ANNULÉE" */
    private String statut;

    private Double montantTotal;

    /** Relation N:1 — une réservation concerne une chambre */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    /**
     * Relation N:N — owning side
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reservation_service_extra",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "service_extra_id")
    )
    private List<ServiceExtra> servicesExtra;
}
