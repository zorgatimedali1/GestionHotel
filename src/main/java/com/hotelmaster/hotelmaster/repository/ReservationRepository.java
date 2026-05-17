package com.hotelmaster.hotelmaster.repository;

import com.hotelmaster.hotelmaster.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.nomClient LIKE %:mc% OR r.prenomClient LIKE %:mc% OR r.emailClient LIKE %:mc%")
    Page<Reservation> rechercheParMotCle(@Param("mc") String motCle, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.statut = :statut")
    Long countByStatut(@Param("statut") String statut);

    @Query("""
    SELECT COUNT(r) FROM Reservation r
    WHERE r.chambre.id = :chambreId
      AND r.statut IN ('EN_ATTENTE', 'CONFIRMÉE')
      AND r.dateArrivee < :dateDepart
      AND r.dateDepart > :dateArrivee
    """)
    Long countOverlap(@Param("chambreId") Long chambreId,
                      @Param("dateArrivee") LocalDate arrivee,
                      @Param("dateDepart") LocalDate depart);

    @Query("""
    SELECT COUNT(r) FROM Reservation r
    WHERE r.chambre.id = :chambreId
      AND r.id != :reservationId
      AND r.statut IN ('EN_ATTENTE', 'CONFIRMÉE')
      AND r.dateArrivee < :dateDepart
      AND r.dateDepart > :dateArrivee
    """)
    Long countOverlapExcluding(@Param("chambreId") Long chambreId,
                               @Param("dateArrivee") LocalDate arrivee,
                               @Param("dateDepart") LocalDate depart,
                               @Param("reservationId") Long reservationId);

    @Query("SELECT COALESCE(SUM(r.montantTotal),0) FROM Reservation r WHERE r.statut = 'CONFIRMÉE'")
    Double sumMontantConfirme();

    @Query("SELECT COALESCE(SUM(r.montantTotal),0) FROM Reservation r WHERE r.statut = 'EN_ATTENTE'")
    Double sumMontantEnAttente();
    @Query("""
    SELECT COUNT(DISTINCT r.chambre.id)
    FROM Reservation r
    WHERE r.statut = 'CONFIRMÉE'
      AND r.dateDepart > :today
    """)
    Long countChambresOccupeesAujourdhui(@Param("today") LocalDate today);

    @Query("""
    SELECT DISTINCT r.chambre.id
    FROM Reservation r
    WHERE r.statut = 'CONFIRMÉE'
      AND r.dateDepart > :today
    """)
    List<Long> findChambresOccupeesIdsAujourdhui(@Param("today") LocalDate today);
}
