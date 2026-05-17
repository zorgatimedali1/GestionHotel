package com.hotelmaster.hotelmaster.controller;

import com.hotelmaster.hotelmaster.service.dashboard.ServiceDashboard;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class DashboardController {

    private final ServiceDashboard serviceDashboard;

    /**
     * Dashboard réservé ADMIN (sécurisé dans SecurityConfig).
     * Le réceptionniste est redirigé vers /receptionist/home au login.
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("stats", serviceDashboard.getStats());
        return "dashboard";
    }
}
