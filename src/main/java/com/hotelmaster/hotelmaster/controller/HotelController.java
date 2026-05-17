package com.hotelmaster.hotelmaster.controller;

import com.hotelmaster.hotelmaster.entities.Hotel;
import com.hotelmaster.hotelmaster.service.alert.AlertService;
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
@RequestMapping("/hotels")
public class HotelController {

    private final IServiceHotel serviceHotel;
    private final AlertService alertService;

    @GetMapping
    public String listHotels(Model model,
                             @RequestParam(name = "mc", defaultValue = "") String mc,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<Hotel> pageHotels = serviceHotel.getAllHotelsFilter(mc, PageRequest.of(page, size));
        model.addAttribute("hotelsPage", pageHotels);
        model.addAttribute("hotels", pageHotels.getContent());
        model.addAttribute("mc", mc);
        model.addAttribute("pages", new int[pageHotels.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "hotels/list";
    }

    @GetMapping("/nouveau")
    public String formAjout(Model model) {
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("modeEdition", false);
        return "hotels/form";
    }

    @PostMapping("/save")
    public String saveHotel(@Valid @ModelAttribute("hotel") Hotel hotel,
                            BindingResult bindingResult,
                            @RequestParam("photoFile") MultipartFile photoFile,
                            Model model, RedirectAttributes ra) throws Exception {
        if (bindingResult.hasErrors()) { model.addAttribute("modeEdition", false); return "hotels/form"; }
        serviceHotel.addHotel(hotel, photoFile);
        alertService.success(ra, "L'hôtel « " + hotel.getNom() + " » a été ajouté avec succès !");
        return "redirect:/hotels";
    }

    @GetMapping("/modifier/{id}")
    public String formModifier(@PathVariable Long id, Model model) {
        model.addAttribute("hotel", serviceHotel.getHotel(id));
        model.addAttribute("modeEdition", true);
        return "hotels/form";
    }

    @PostMapping("/update")
    public String updateHotel(@Valid @ModelAttribute("hotel") Hotel hotel,
                              BindingResult bindingResult,
                              @RequestParam("photoFile") MultipartFile photoFile,
                              Model model, RedirectAttributes ra) throws Exception {
        if (bindingResult.hasErrors()) { model.addAttribute("modeEdition", true); return "hotels/form"; }
        serviceHotel.updateHotel(hotel, photoFile);
        alertService.success(ra, "L'hôtel « " + hotel.getNom() + " » a été mis à jour.");
        return "redirect:/hotels";
    }

    @GetMapping("/supprimer/{id}")
    public String deleteHotel(@PathVariable Long id, RedirectAttributes ra) {
        Hotel h = serviceHotel.getHotel(id);
        serviceHotel.deleteHotel(id);
        alertService.warning(ra, "L'hôtel « " + h.getNom() + " » a été supprimé.");
        return "redirect:/hotels";
    }

    @GetMapping("/detail/{id}")
    public String detailHotel(@PathVariable Long id, Model model) {
        model.addAttribute("hotel", serviceHotel.getHotel(id));
        return "hotels/detail";
    }
}
