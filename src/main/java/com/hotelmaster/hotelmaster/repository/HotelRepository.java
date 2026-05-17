package com.hotelmaster.hotelmaster.repository;

import com.hotelmaster.hotelmaster.entities.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT h FROM Hotel h WHERE h.nom LIKE %:mc% OR h.ville LIKE %:mc%")
    Page<Hotel> rechercheParMotCle(@Param("mc") String motCle, Pageable pageable);

    @Query("SELECT COUNT(h) FROM Hotel h")
    Long countTotalHotels();

    @Query("SELECT SUM(SIZE(h.chambres)) FROM Hotel h")
    Long countTotalChambres();
}
