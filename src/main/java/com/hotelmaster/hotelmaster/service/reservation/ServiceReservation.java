package com.hotelmaster.hotelmaster.service.reservation;

import com.hotelmaster.hotelmaster.entities.Reservation;
import com.hotelmaster.hotelmaster.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceReservation implements IServiceReservation {

    private final ReservationRepository reservationRepository;

    @Override
    public void addReservation(Reservation reservation) {
        calculerMontantTotal(reservation);
        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Reservation reservation) {
        calculerMontantTotal(reservation);
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Réservation introuvable : id=" + id));
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Page<Reservation> getAllReservationsFilter(String motCle, Pageable pageable) {
        return reservationRepository.rechercheParMotCle(motCle, pageable);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public boolean isChambreDisponible(Long chambreId, LocalDate arrivee, LocalDate depart) {
        Long overlap = reservationRepository.countOverlap(chambreId, arrivee, depart);
        return overlap == null || overlap == 0;
    }

    @Override
    public boolean isChambreDisponiblePourEdition(Long chambreId, LocalDate arrivee, LocalDate depart, Long reservationId) {
        Long overlap = reservationRepository.countOverlapExcluding(chambreId, arrivee, depart, reservationId);
        return overlap == null || overlap == 0;
    }

    @Override
    public void calculerMontantTotal(Reservation reservation) {
        if (reservation.getChambre() == null
                || reservation.getDateArrivee() == null
                || reservation.getDateDepart() == null) {
            reservation.setMontantTotal(0.0);
            return;
        }

        long nbNuits = ChronoUnit.DAYS.between(
                reservation.getDateArrivee(), reservation.getDateDepart());

        // FIX: Handle null prixParNuit
        Double prixParNuit = reservation.getChambre().getPrixParNuit();
        double prix = (prixParNuit != null) ? prixParNuit : 0.0;

        double montant = nbNuits * prix;

        if (reservation.getServicesExtra() != null) {
            double extras = reservation.getServicesExtra().stream()
                    .mapToDouble(se -> se.getPrix())
                    .sum();
            montant += extras;
        }

        reservation.setMontantTotal(montant);
    }
}