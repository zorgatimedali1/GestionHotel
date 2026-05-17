package com.hotelmaster.hotelmaster.controller;

import com.hotelmaster.hotelmaster.service.chambre.IServiceChambre;
import com.hotelmaster.hotelmaster.service.reservation.IServiceReservation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/receptionist")
public class ReceptionistHomeController {

    private final IServiceChambre serviceChambre;
    private final IServiceReservation serviceReservation;

    /**
     * Page d'accueil du réceptionniste.
     * Affiche directement les chambres et réservations du jour (5 premières).
     */
    @GetMapping("/home")
    public String home(Model model) {
        // Les 5 premières chambres
        model.addAttribute("chambres",
                serviceChambre.getAllChambresFilter("", PageRequest.of(0, 5)).getContent());

        // Les 5 premières réservations
        model.addAttribute("reservations",
                serviceReservation.getAllReservationsFilter("", PageRequest.of(0, 5)).getContent());

        return "receptionist/home";
    }
}
