package com.hotelmaster.hotelmaster.controller;

import com.hotelmaster.hotelmaster.entities.Reservation;
import com.hotelmaster.hotelmaster.entities.ServiceExtra;
import com.hotelmaster.hotelmaster.repository.ServiceExtraRepository;
import com.hotelmaster.hotelmaster.service.alert.AlertService;
import com.hotelmaster.hotelmaster.service.chambre.IServiceChambre;
import com.hotelmaster.hotelmaster.service.reservation.IServiceReservation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final IServiceReservation serviceReservation;
    private final IServiceChambre serviceChambre;
    private final ServiceExtraRepository serviceExtraRepository;
    private final AlertService alertService;

    @GetMapping
    public String listReservations(Model model,
                                   @RequestParam(name="mc",defaultValue="") String mc,
                                   @RequestParam(name="page",defaultValue="0") int page,
                                   @RequestParam(name="size",defaultValue="5") int size) {
        Page<Reservation> p = serviceReservation.getAllReservationsFilter(mc, PageRequest.of(page, size));
        model.addAttribute("reservationsPage", p);
        model.addAttribute("reservations", p.getContent());
        model.addAttribute("mc", mc);
        model.addAttribute("pages", new int[p.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "reservations/list";
    }

    @GetMapping("/nouveau")
    public String formAjout(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("chambres", serviceChambre.getAllChambres());
        model.addAttribute("servicesExtra", serviceExtraRepository.findAll());
        model.addAttribute("statuts", List.of("EN_ATTENTE","CONFIRMÉE","ANNULÉE"));
        model.addAttribute("modeEdition", false);
        return "reservations/form";
    }

    @PostMapping("/save")
    public String saveReservation(@Valid @ModelAttribute("reservation") Reservation reservation,
                                  BindingResult bindingResult,
                                  @RequestParam(value="servicesExtraIds", required=false) List<Long> ids,
                                  Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("chambres", serviceChambre.getAllChambres());
            model.addAttribute("servicesExtra", serviceExtraRepository.findAll());
            model.addAttribute("statuts", List.of("EN_ATTENTE","CONFIRMÉE","ANNULÉE"));
            model.addAttribute("modeEdition", false);
            return "reservations/form";
        }
        if (reservation.getChambre() != null && reservation.getChambre().getId() != null) {
            reservation.setChambre(serviceChambre.getChambre(reservation.getChambre().getId()));
        }
        if (reservation.getChambre() != null && reservation.getDateArrivee() != null && reservation.getDateDepart() != null) {
            if (!serviceReservation.isChambreDisponible(reservation.getChambre().getId(), reservation.getDateArrivee(), reservation.getDateDepart())) {
                bindingResult.rejectValue("chambre.id", "error", "Cette chambre est déjà réservée pour ces dates.");
                model.addAttribute("chambres", serviceChambre.getChambresDisponibles(reservation.getDateArrivee(), reservation.getDateDepart()));
                model.addAttribute("servicesExtra", serviceExtraRepository.findAll());
                model.addAttribute("statuts", List.of("EN_ATTENTE","CONFIRMÉE","ANNULÉE"));
                model.addAttribute("modeEdition", false);
                return "reservations/form";
            }
        }
        attachServices(reservation, ids);
        serviceReservation.addReservation(reservation);
        alertService.success(ra, "Réservation de " + reservation.getPrenomClient() + " " + reservation.getNomClient() + " créée !");
        return "redirect:/reservations";
    }

    @GetMapping("/modifier/{id}")
    public String formModifier(@PathVariable Long id, Model model) {
        model.addAttribute("reservation", serviceReservation.getReservation(id));
        model.addAttribute("chambres", serviceChambre.getAllChambres());
        model.addAttribute("servicesExtra", serviceExtraRepository.findAll());
        model.addAttribute("statuts", List.of("EN_ATTENTE","CONFIRMÉE","ANNULÉE"));
        model.addAttribute("modeEdition", true);
        return "reservations/form";
    }

    @PostMapping("/update")
    public String updateReservation(@Valid @ModelAttribute("reservation") Reservation reservation,
                                    BindingResult bindingResult,
                                    @RequestParam(value="servicesExtraIds", required=false) List<Long> ids,
                                    Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("chambres", serviceChambre.getAllChambres());
            model.addAttribute("servicesExtra", serviceExtraRepository.findAll());
            model.addAttribute("statuts", List.of("EN_ATTENTE","CONFIRMÉE","ANNULÉE"));
            model.addAttribute("modeEdition", true);
            return "reservations/form";
        }
        if (reservation.getChambre() != null && reservation.getChambre().getId() != null) {
            reservation.setChambre(serviceChambre.getChambre(reservation.getChambre().getId()));
        }
        if (reservation.getChambre() != null && reservation.getDateArrivee() != null && reservation.getDateDepart() != null) {
            if (!serviceReservation.isChambreDisponiblePourEdition(reservation.getChambre().getId(), reservation.getDateArrivee(), reservation.getDateDepart(), reservation.getId())) {
                bindingResult.rejectValue("chambre.id", "error", "Cette chambre est déjà réservée pour ces dates.");
                model.addAttribute("chambres", serviceChambre.getChambresDisponibles(reservation.getDateArrivee(), reservation.getDateDepart()));
                model.addAttribute("servicesExtra", serviceExtraRepository.findAll());
                model.addAttribute("statuts", List.of("EN_ATTENTE","CONFIRMÉE","ANNULÉE"));
                model.addAttribute("modeEdition", true);
                return "reservations/form";
            }
        }
        attachServices(reservation, ids);
        serviceReservation.updateReservation(reservation);
        alertService.success(ra, "Réservation mise à jour avec succès.");
        return "redirect:/reservations";
    }

    @GetMapping("/supprimer/{id}")
    public String deleteReservation(@PathVariable Long id, RedirectAttributes ra) {
        Reservation r = serviceReservation.getReservation(id);
        serviceReservation.deleteReservation(id);
        alertService.warning(ra, "Réservation de " + r.getPrenomClient() + " " + r.getNomClient() + " supprimée.");
        return "redirect:/reservations";
    }

    private void attachServices(Reservation r, List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            List<ServiceExtra> services = serviceExtraRepository.findAllById(ids);
            r.setServicesExtra(services);
        } else {
            r.setServicesExtra(List.of());
        }
    }
}
