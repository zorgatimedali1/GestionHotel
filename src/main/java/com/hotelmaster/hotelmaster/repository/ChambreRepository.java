package com.hotelmaster.hotelmaster.repository;

import com.hotelmaster.hotelmaster.entities.Chambre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {

    @Query("SELECT c FROM Chambre c WHERE c.numero LIKE %:mc% OR c.type LIKE %:mc%")
    Page<Chambre> rechercheParMotCle(@Param("mc") String motCle, Pageable pageable);

    @Query("SELECT c FROM Chambre c WHERE c.hotel.id = :hotelId AND (c.numero LIKE %:mc% OR c.type LIKE %:mc%)")
    Page<Chambre> rechercheParMotCleEtHotel(@Param("mc") String motCle, @Param("hotelId") Long hotelId, Pageable pageable);

    @Query("SELECT c FROM Chambre c WHERE c.hotel.id = :hotelId")
    List<Chambre> findByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT COUNT(c) FROM Chambre c WHERE c.disponible = true")
    Long countChambresDisponibles();

    @Query("SELECT COUNT(c) FROM Chambre c WHERE c.disponible = false")
    Long countChambresOccupees();

    @Query("SELECT AVG(c.prixParNuit) FROM Chambre c")
    Double avgPrixParNuit();

    @Query("""
    SELECT c FROM Chambre c WHERE c.id NOT IN (
        SELECT DISTINCT r.chambre.id FROM Reservation r
        WHERE r.statut IN ('EN_ATTENTE', 'CONFIRMÉE')
        AND r.dateArrivee < :dateDepart
        AND r.dateDepart > :dateArrivee
    )
    """)
    List<Chambre> findDisponiblesPourDates(@Param("dateArrivee") LocalDate arrivee,
                                           @Param("dateDepart") LocalDate depart);
    @Modifying
    @Transactional
    @Query("UPDATE Chambre c SET c.disponible = true")
    void resetAllToDisponible();

    @Modifying
    @Transactional
    @Query("UPDATE Chambre c SET c.disponible = false WHERE c.id IN :ids")
    void markOccupeesById(@Param("ids") List<Long> ids);
}
