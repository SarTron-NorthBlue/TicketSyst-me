package com.example.ticketapp.controller;

import com.example.ticketapp.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final TicketService ticketService;

    public DashboardController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "dashboard";
    }
}
