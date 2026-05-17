package com.hotelmaster.hotelmaster.controller;

import com.hotelmaster.hotelmaster.entities.ServiceExtra;
import com.hotelmaster.hotelmaster.service.alert.AlertService;
import com.hotelmaster.hotelmaster.service.serviceextra.IServiceExtra;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/services-extra")
public class ServiceExtraController {

    private final IServiceExtra serviceExtra;
    private final AlertService alertService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("services", serviceExtra.getAllServicesExtra());
        return "services/list";
    }

    @GetMapping("/nouveau")
    public String formAjout(Model model) {
        model.addAttribute("service", new ServiceExtra());
        model.addAttribute("modeEdition", false);
        return "services/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("service") ServiceExtra service,
                       BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) return "services/form";
        serviceExtra.addServiceExtra(service);
        alertService.success(ra, "Service « " + service.getNom() + " » ajouté avec succès !");
        return "redirect:/services-extra";
    }

    @GetMapping("/modifier/{id}")
    public String formModifier(@PathVariable Long id, Model model) {
        model.addAttribute("service", serviceExtra.getServiceExtra(id));
        model.addAttribute("modeEdition", true);
        return "services/form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("service") ServiceExtra service,
                         BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) return "services/form";
        serviceExtra.updateServiceExtra(service);
        alertService.success(ra, "Service « " + service.getNom() + " » mis à jour.");
        return "redirect:/services-extra";
    }

    @GetMapping("/supprimer/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        ServiceExtra s = serviceExtra.getServiceExtra(id);
        serviceExtra.deleteServiceExtra(id);
        alertService.warning(ra, "Service « " + s.getNom() + " » supprimé.");
        return "redirect:/services-extra";
    }
}
