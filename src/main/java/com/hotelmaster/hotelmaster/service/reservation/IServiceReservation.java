package com.hotelmaster.hotelmaster.service.reservation;

import com.hotelmaster.hotelmaster.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IServiceReservation {
    void addReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    Reservation getReservation(Long id);
    List<Reservation> getAllReservations();
    Page<Reservation> getAllReservationsFilter(String motCle, Pageable pageable);
    void deleteReservation(Long id);
    void calculerMontantTotal(Reservation reservation);
    boolean isChambreDisponible(Long chambreId, LocalDate arrivee, LocalDate depart);
    boolean isChambreDisponiblePourEdition(Long chambreId, LocalDate arrivee, LocalDate depart, Long reservationId);
}
