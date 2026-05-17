package com.hotelmaster.hotelmaster.service.dashboard;

import com.hotelmaster.hotelmaster.entities.Chambre;
import com.hotelmaster.hotelmaster.entities.DashboardStatsDTO;
import com.hotelmaster.hotelmaster.entities.Hotel;
import com.hotelmaster.hotelmaster.entities.Reservation;
import com.hotelmaster.hotelmaster.entities.ServiceExtra;
import com.hotelmaster.hotelmaster.repository.ChambreRepository;
import com.hotelmaster.hotelmaster.repository.HotelRepository;
import com.hotelmaster.hotelmaster.repository.ReservationRepository;
import com.hotelmaster.hotelmaster.repository.ServiceExtraRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ServiceDashboard {

    private final HotelRepository hotelRepository;
    private final ChambreRepository chambreRepository;
    private final ReservationRepository reservationRepository;
    private final ServiceExtraRepository serviceExtraRepository;

    public DashboardStatsDTO getStats() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<Chambre> chambres = chambreRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAll();
        List<ServiceExtra> servicesExtra = serviceExtraRepository.findAll();

        long totalHotels = hotels.size();
        long totalChambres = chambres.size();
        LocalDate today = LocalDate.now();

        Set<Long> occupeesIds = reservations.stream()
                .filter(r -> "CONFIRMÉE".equals(r.getStatut())
                        && r.getDateDepart() != null
                        && r.getDateDepart().isAfter(today))
                .map(r -> r.getChambre() != null ? r.getChambre().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        long occupees = occupeesIds.size();
        long disponibles = totalChambres - occupees;

        double tauxOccupation = totalChambres > 0
                ? Math.round(occupees * 1000.0 / totalChambres) / 10.0
                : 0.0;

        double prixMoyenChambre = chambres.stream()
                .filter(c -> c.getPrixParNuit() != null)
                .mapToDouble(Chambre::getPrixParNuit)
                .average()
                .orElse(0.0);

        long totalReservations = reservations.size();

        long confirmees = reservations.stream()
                .filter(r -> "CONFIRMÉE".equals(r.getStatut()))
                .count();

        long enAttente = reservations.stream()
                .filter(r -> "EN_ATTENTE".equals(r.getStatut()))
                .count();

        long annulees = reservations.stream()
                .filter(r -> "ANNULÉE".equals(r.getStatut()))
                .count();

        double chiffreAffaires = reservations.stream()
                .filter(r -> "CONFIRMÉE".equals(r.getStatut()) && r.getMontantTotal() != null)
                .mapToDouble(Reservation::getMontantTotal)
                .sum();

        double chiffreAffairesPotentiel = reservations.stream()
                .filter(r -> "EN_ATTENTE".equals(r.getStatut()) && r.getMontantTotal() != null)
                .mapToDouble(Reservation::getMontantTotal)
                .sum();

        long totalServicesExtra = servicesExtra.size();

        return new DashboardStatsDTO(
                totalHotels, totalChambres, disponibles, occupees,
                tauxOccupation, prixMoyenChambre,
                totalReservations, confirmees, enAttente, annulees,
                chiffreAffaires, chiffreAffairesPotentiel, totalServicesExtra
        );
    }
}
