package com.hotelmaster.hotelmaster.service.chambre;

import com.hotelmaster.hotelmaster.entities.Chambre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IServiceChambre {
    void addChambre(Chambre chambre, MultipartFile photo) throws IOException;
    void updateChambre(Chambre chambre, MultipartFile photo) throws IOException;
    Chambre getChambre(Long id);
    List<Chambre> getAllChambres();
    Page<Chambre> getAllChambresFilter(String motCle, Pageable pageable);
    Page<Chambre> getAllChambresFilterByHotel(String motCle, Long hotelId, Pageable pageable);
    List<Chambre> getChambresByHotel(Long hotelId);
    List<Chambre> getChambresDisponibles(LocalDate arrivee, LocalDate depart);
    void deleteChambre(Long id);
}
