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
public class ServiceExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du service est obligatoire")
    private String nom;

    @NotNull(message = "Le prix du service est obligatoire")
    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private Double prix;

    private String description;

    private String icone;

    /** Relation N:N inverse */
    @ManyToMany(mappedBy = "servicesExtra", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}
