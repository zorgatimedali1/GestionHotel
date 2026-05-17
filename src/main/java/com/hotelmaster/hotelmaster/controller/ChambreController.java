package com.hotelmaster.hotelmaster.controller;

import com.hotelmaster.hotelmaster.entities.Chambre;
import com.hotelmaster.hotelmaster.entities.DetailsChambre;
import com.hotelmaster.hotelmaster.repository.DetailsChambreRepository;
import com.hotelmaster.hotelmaster.service.alert.AlertService;
import com.hotelmaster.hotelmaster.service.chambre.IServiceChambre;
import com.hotelmaster.hotelmaster.service.hotel.IServiceHotel;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/chambres")
public class ChambreController {

    private final IServiceChambre serviceChambre;
    private final IServiceHotel serviceHotel;
    private final DetailsChambreRepository detailsChambreRepository;
    private final AlertService alertService;

    // ── Liste avec filtre hôtel ────────────────────────────────────────────────
    @GetMapping
    public String listChambres(Model model,
                               @RequestParam(name = "mc", defaultValue = "") String mc,
                               @RequestParam(name = "hotelId", required = false) Long hotelId,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "5") int size) {

        Page<Chambre> p;
        // ✅ FIX 4 : si un hôtel est sélectionné, filtrer par hôtel
        if (hotelId != null) {
            p = serviceChambre.getAllChambresFilterByHotel(mc, hotelId, PageRequest.of(page, size));
        } else {
            p = serviceChambre.getAllChambresFilter(mc, PageRequest.of(page, size));
        }

        model.addAttribute("chambresPage", p);
        model.addAttribute("chambres", p.getContent());
        model.addAttribute("mc", mc);
        model.addAttribute("hotelId", hotelId);
        model.addAttribute("pages", new int[p.getTotalPages()]);
        model.addAttribute("currentPage", page);
        // Liste des hôtels pour le dropdown de filtre
        model.addAttribute("hotelsFiltre", serviceHotel.getAllHotels());
        return "chambres/list";
    }

    @GetMapping("/nouveau")
    public String formAjout(Model model) {
        model.addAttribute("chambre", new Chambre());
        model.addAttribute("hotels", serviceHotel.getAllHotels());
        model.addAttribute("modeEdition", false);
        return "chambres/form";
    }

    @PostMapping("/save")
    public String saveChambre(@Valid @ModelAttribute("chambre") Chambre chambre,
                              BindingResult bindingResult,
                              @RequestParam("photoFile") MultipartFile photoFile,
                              Model model, RedirectAttributes ra) throws Exception {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hotels", serviceHotel.getAllHotels());
            model.addAttribute("modeEdition", false);
            return "chambres/form";
        }
        serviceChambre.addChambre(chambre, photoFile);
        alertService.success(ra, "Chambre N°" + chambre.getNumero() + " ajoutée avec succès !");
        return "redirect:/chambres";
    }

    @GetMapping("/modifier/{id}")
    public String formModifier(@PathVariable Long id, Model model) {
        model.addAttribute("chambre", serviceChambre.getChambre(id));
        model.addAttribute("hotels", serviceHotel.getAllHotels());
        model.addAttribute("modeEdition", true);
        return "chambres/form";
    }

    @PostMapping("/update")
    public String updateChambre(@Valid @ModelAttribute("chambre") Chambre chambre,
                                BindingResult bindingResult,
                                @RequestParam("photoFile") MultipartFile photoFile,
                                Model model, RedirectAttributes ra) throws Exception {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hotels", serviceHotel.getAllHotels());
            model.addAttribute("modeEdition", true);
            return "chambres/form";
        }
        serviceChambre.updateChambre(chambre, photoFile);
        alertService.success(ra, "Chambre N°" + chambre.getNumero() + " mise à jour.");
        return "redirect:/chambres";
    }

    @GetMapping("/supprimer/{id}")
    public String deleteChambre(@PathVariable Long id, RedirectAttributes ra) {
        Chambre c = serviceChambre.getChambre(id);
        serviceChambre.deleteChambre(id);
        alertService.warning(ra, "Chambre N°" + c.getNumero() + " supprimée.");
        return "redirect:/chambres";
    }

    @GetMapping("/details/{chambreId}")
    public String formDetails(@PathVariable Long chambreId, Model model) {
        Chambre chambre = serviceChambre.getChambre(chambreId);
        DetailsChambre details = detailsChambreRepository
                .findByChambreId(chambreId)
                .orElse(DetailsChambre.builder().chambre(chambre).build());
        model.addAttribute("details", details);
        model.addAttribute("chambre", chambre);
        return "chambres/details";
    }

    @PostMapping("/details/save")
    public String saveDetails(@Valid @ModelAttribute("details") DetailsChambre details,
                              BindingResult bindingResult,
                              Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("chambre", details.getChambre());
            return "chambres/details";
        }
        detailsChambreRepository.save(details);
        alertService.success(ra, "Détails de la chambre enregistrés avec succès.");
        return "redirect:/chambres";
    }
}
