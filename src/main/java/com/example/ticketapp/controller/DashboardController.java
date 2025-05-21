package com.example.ticketapp.controller;

import com.example.ticketapp.service.TicketService;
import com.example.ticketapp.service.UserService;
import com.example.ticketapp.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final TicketService ticketService;
    private final UserService userService;

    public DashboardController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/{companySlug}")
    public String companyDashboard(@PathVariable String companySlug, 
                                 Principal principal,
                                 Model model) {
        User user = userService.loadUserByUsername(principal.getName());
        
        if (!user.getCompanySlug().equals(companySlug)) {
            return "redirect:/access-denied";
        }
        
        model.addAttribute("tickets", ticketService.getTicketsByCompany(companySlug));
        return "dashboard";
    }

    @GetMapping
    public String redirectToCompanyDashboard(Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        return "redirect:/dashboard/" + user.getCompanySlug();
    }
}
