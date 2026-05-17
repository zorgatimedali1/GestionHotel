package com.hotelmaster.hotelmaster.service.hotel;

import com.hotelmaster.hotelmaster.entities.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IServiceHotel {
    void addHotel(Hotel hotel, MultipartFile photo) throws IOException;
    void updateHotel(Hotel hotel, MultipartFile photo) throws IOException;
    Hotel getHotel(Long id);
    List<Hotel> getAllHotels();
    Page<Hotel> getAllHotelsFilter(String motCle, Pageable pageable);
    void deleteHotel(Long id);
}
