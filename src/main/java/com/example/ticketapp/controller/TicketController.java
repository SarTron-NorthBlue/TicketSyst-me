package com.example.ticketapp.controller;

import com.example.ticketapp.model.Ticket;
import com.example.ticketapp.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "tickets/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "tickets/new";
    }

    @PostMapping
    public String createTicket(@ModelAttribute Ticket ticket) {
        ticketService.saveTicket(ticket);
        return "redirect:/tickets";
    }
}
