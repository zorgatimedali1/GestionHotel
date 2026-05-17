package com.hotelmaster.hotelmaster.service.chambre;

import com.hotelmaster.hotelmaster.entities.Chambre;
import com.hotelmaster.hotelmaster.repository.ChambreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceChambre implements IServiceChambre {

    private final ChambreRepository chambreRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ServiceChambre(ChambreRepository chambreRepository) {
        this.chambreRepository = chambreRepository;
    }

    @Override
    public void addChambre(Chambre chambre, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            chambre.setPhoto(savePhoto(photo));
        }
        chambreRepository.save(chambre);
    }

    @Override
    public void updateChambre(Chambre chambre, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            chambre.setPhoto(savePhoto(photo));
        } else {
            chambreRepository.findById(chambre.getId()).ifPresent(existing -> {
                if (chambre.getPhoto() == null || chambre.getPhoto().isBlank()) {
                    chambre.setPhoto(existing.getPhoto());
                }
            });
        }
        chambreRepository.save(chambre);
    }

    @Override
    public Chambre getChambre(Long id) {
        return chambreRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Chambre introuvable : id=" + id));
    }

    @Override
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    @Override
    public Page<Chambre> getAllChambresFilter(String motCle, Pageable pageable) {
        return chambreRepository.rechercheParMotCle(motCle, pageable);
    }

    @Override
    public Page<Chambre> getAllChambresFilterByHotel(String motCle, Long hotelId, Pageable pageable) {
        return chambreRepository.rechercheParMotCleEtHotel(motCle, hotelId, pageable);
    }

    @Override
    public List<Chambre> getChambresByHotel(Long hotelId) {
        return chambreRepository.findByHotelId(hotelId);
    }

    @Override
    public List<Chambre> getChambresDisponibles(LocalDate arrivee, LocalDate depart) {
        return chambreRepository.findDisponiblesPourDates(arrivee, depart);
    }

    @Override
    public void deleteChambre(Long id) {
        chambreRepository.deleteById(id);
    }

    private String savePhoto(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf('.'))
                : ".jpg";
        String fileName = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
        return fileName;
    }
}
