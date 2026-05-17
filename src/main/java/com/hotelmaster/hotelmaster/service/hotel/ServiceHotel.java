package com.hotelmaster.hotelmaster.service.hotel;

import com.hotelmaster.hotelmaster.entities.Hotel;
import com.hotelmaster.hotelmaster.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceHotel implements IServiceHotel {

    private final HotelRepository hotelRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ServiceHotel(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void addHotel(Hotel hotel, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            hotel.setPhoto(savePhoto(photo));
        }
        hotelRepository.save(hotel);
    }

    @Override
    public void updateHotel(Hotel hotel, MultipartFile photo) throws IOException {
        // Keep existing photo if no new file uploaded
        if (photo != null && !photo.isEmpty()) {
            hotel.setPhoto(savePhoto(photo));
        } else {
            hotelRepository.findById(hotel.getId()).ifPresent(existing -> {
                if (hotel.getPhoto() == null || hotel.getPhoto().isBlank()) {
                    hotel.setPhoto(existing.getPhoto());
                }
            });
        }
        hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotel(Long id) {
        return hotelRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Hôtel introuvable : id=" + id));
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Page<Hotel> getAllHotelsFilter(String motCle, Pageable pageable) {
        return hotelRepository.rechercheParMotCle(motCle, pageable);
    }

    @Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    private String savePhoto(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Sanitize filename: keep only the original extension, use UUID as name
        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf('.'))
                : ".jpg";
        String fileName = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
        return fileName;
    }
}
