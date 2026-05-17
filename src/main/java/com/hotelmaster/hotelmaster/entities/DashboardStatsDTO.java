package com.hotelmaster.hotelmaster.entities;

import lombok.*;

@Getter @AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalHotels;
    private Long totalChambres;
    private Long chambresDisponibles;
    private Long chambresOccupees;
    private Double tauxOccupation;
    private Double prixMoyenChambre;
    private Long totalReservations;
    private Long reservationsConfirmees;
    private Long reservationsEnAttente;
    private Long reservationsAnnulees;
    private Double chiffreAffaires;
    private Double chiffreAffairesPotentiel;
    private Long totalServicesExtra;
}
