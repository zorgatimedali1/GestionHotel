package com.hotelmaster.hotelmaster.controller;

import com.hotelmaster.hotelmaster.service.alert.AlertService;
import com.hotelmaster.hotelmaster.service.user.IAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final IAppUserService userService;
    private final AlertService alertService;

    // ── Liste des réceptionnistes ──────────────────────────────────────────────
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("receptionists", userService.getAllReceptionists());
        return "users/list";
    }

    // ── Formulaire création ────────────────────────────────────────────────────
    @GetMapping("/nouveau")
    public String formAjout() {
        return "users/form-create";
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String nomComplet,
                           RedirectAttributes ra) {
        try {
            userService.createReceptionist(username, password, nomComplet);
            alertService.success(ra, "Compte réceptionniste « " + username + " » créé avec succès !");
        } catch (RuntimeException e) {
            alertService.error(ra, e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // ── Formulaire modification ────────────────────────────────────────────────
    @GetMapping("/modifier/{id}")
    public String formModifier(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "users/form-edit";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String username,
                             @RequestParam String nomComplet,
                             @RequestParam(defaultValue = "false") boolean actif,
                             RedirectAttributes ra) {
        try {
            userService.updateReceptionist(id, username, nomComplet, actif);
            alertService.success(ra, "Compte « " + username + " » mis à jour.");
        } catch (RuntimeException e) {
            alertService.error(ra, e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // ── Changement de mot de passe ─────────────────────────────────────────────
    @PostMapping("/password/{id}")
    public String changePassword(@PathVariable Long id,
                                 @RequestParam String newPassword,
                                 RedirectAttributes ra) {
        userService.changePassword(id, newPassword);
        alertService.success(ra, "Mot de passe modifié avec succès.");
        return "redirect:/admin/users";
    }

    // ── Suppression ────────────────────────────────────────────────────────────
    @GetMapping("/supprimer/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        String username = userService.getUser(id).getUsername();
        userService.deleteUser(id);
        alertService.warning(ra, "Compte « " + username + " » supprimé.");
        return "redirect:/admin/users";
    }
}
