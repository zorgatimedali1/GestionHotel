package com.hotelmaster.hotelmaster.service.alert;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Service
public class AlertService {

    public void success(RedirectAttributes ra, String message) {
        ra.addFlashAttribute("alertType", "success");
        ra.addFlashAttribute("alertIcon", "fa-check-circle");
        ra.addFlashAttribute("alertMsg", message);
    }

    public void error(RedirectAttributes ra, String message) {
        ra.addFlashAttribute("alertType", "danger");
        ra.addFlashAttribute("alertIcon", "fa-times-circle");
        ra.addFlashAttribute("alertMsg", message);
    }

    public void warning(RedirectAttributes ra, String message) {
        ra.addFlashAttribute("alertType", "warning");
        ra.addFlashAttribute("alertIcon", "fa-exclamation-triangle");
        ra.addFlashAttribute("alertMsg", message);
    }

    public void info(RedirectAttributes ra, String message) {
        ra.addFlashAttribute("alertType", "info");
        ra.addFlashAttribute("alertIcon", "fa-info-circle");
        ra.addFlashAttribute("alertMsg", message);
    }
}
