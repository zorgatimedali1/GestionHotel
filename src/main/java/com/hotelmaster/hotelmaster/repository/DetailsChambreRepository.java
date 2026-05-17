package com.hotelmaster.hotelmaster.repository;

import com.hotelmaster.hotelmaster.entities.DetailsChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailsChambreRepository extends JpaRepository<DetailsChambre, Long> {

    Optional<DetailsChambre> findByChambreId(Long chambreId);
}
