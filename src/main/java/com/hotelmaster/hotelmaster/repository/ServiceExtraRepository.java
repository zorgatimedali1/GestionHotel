package com.hotelmaster.hotelmaster.repository;

import com.hotelmaster.hotelmaster.entities.ServiceExtra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceExtraRepository extends JpaRepository<ServiceExtra, Long> {
}
