package com.hotelmaster.hotelmaster.service.serviceextra;

import com.hotelmaster.hotelmaster.entities.ServiceExtra;

import java.util.List;

public interface IServiceExtra {
    void addServiceExtra(ServiceExtra serviceExtra);
    void updateServiceExtra(ServiceExtra serviceExtra);
    ServiceExtra getServiceExtra(Long id);
    List<ServiceExtra> getAllServicesExtra();
    void deleteServiceExtra(Long id);
}
