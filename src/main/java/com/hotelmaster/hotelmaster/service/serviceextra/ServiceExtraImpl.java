package com.hotelmaster.hotelmaster.service.serviceextra;

import com.hotelmaster.hotelmaster.entities.ServiceExtra;
import com.hotelmaster.hotelmaster.repository.ServiceExtraRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceExtraImpl implements IServiceExtra {

    private final ServiceExtraRepository serviceExtraRepository;

    @Override
    public void addServiceExtra(ServiceExtra serviceExtra) {
        serviceExtraRepository.save(serviceExtra);
    }

    @Override
    public void updateServiceExtra(ServiceExtra serviceExtra) {
        serviceExtraRepository.save(serviceExtra);
    }

    @Override
    public ServiceExtra getServiceExtra(Long id) {
        return serviceExtraRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Service extra introuvable : id=" + id));
    }

    @Override
    public List<ServiceExtra> getAllServicesExtra() {
        return serviceExtraRepository.findAll();
    }

    @Override
    public void deleteServiceExtra(Long id) {
        serviceExtraRepository.deleteById(id);
    }
}
