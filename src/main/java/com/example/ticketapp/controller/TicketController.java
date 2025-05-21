package com.example.ticketapp.controller;

import com.example.ticketapp.model.Ticket;
import com.example.ticketapp.service.TicketService;
import com.example.ticketapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import com.example.ticketapp.model.User;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "tickets/list";
    }

    @GetMapping("/new")
    public String createForm(Model model, Principal principal) {
        Ticket ticket = new Ticket();
        com.example.ticketapp.model.User user = userService.loadUserByUsername(principal.getName());
        ticket.setCreatedBy(user);
        model.addAttribute("ticket", ticket);
        return "tickets/new";
    }

    @PostMapping
    public String createTicket(@ModelAttribute Ticket ticket, Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        ticket.setCreatedBy(user);
        ticketService.saveTicket(ticket);
        return "redirect:/dashboard/" + user.getCompanySlug();
    }

    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        try {
            Optional<Ticket> ticketOpt = ticketService.findById(id);
            if (ticketOpt.isEmpty()) {
                model.addAttribute("error", "Le ticket #" + id + " n'existe pas");
                return "error";
            }
            
            Ticket ticket = ticketOpt.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("statuses", Ticket.Status.values());
            return "tickets/view";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur technique: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/update-status")
    public String updateStatus(@PathVariable Long id, @RequestParam Ticket.Status status) {
        ticketService.updateTicketStatus(id, status);
        return "redirect:/tickets/" + id;
    }

    @GetMapping("/edit/{id}")
    public String editTicketForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "tickets/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTicket(@PathVariable Long id, @ModelAttribute Ticket ticket) {
        ticket.setId(id);
        ticketService.saveTicket(ticket);
        return "redirect:/tickets/" + id;
    }

    @GetMapping("/{id}/debug")
    @ResponseBody
    public ResponseEntity<String> debugTicket(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            return ResponseEntity.ok("Ticket OK - ID: " + ticket.getId() + 
                                   ", CreatedBy: " + (ticket.getCreatedBy() != null ? ticket.getCreatedBy().getId() : "null"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/debug/all-tickets")
    @ResponseBody
    public ResponseEntity<String> debugAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.ok("Aucun ticket en base de données");
        }
        
        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : tickets) {
            sb.append("Ticket ID: ").append(ticket.getId())
              .append(", Titre: ").append(ticket.getTitle())
              .append(", CreatedBy: ").append(ticket.getCreatedBy() != null ? ticket.getCreatedBy().getId() : "null")
              .append("<br>");
        }
        return ResponseEntity.ok(sb.toString());
    }

    @GetMapping("/debug/create-test-ticket")
    @ResponseBody
    public ResponseEntity<String> createTestTicket(Principal principal) {
        try {
            Ticket ticket = new Ticket();
            ticket.setTitle("Ticket de test");
            ticket.setDescription("Ceci est un ticket de test");
            ticket.setClientName("Client Test");
            ticket.setClientEmail("test@example.com");
            ticket.setCategory(Ticket.Category.TECHNICAL);
            ticket.setPriority(Ticket.Priority.MEDIUM);
            
            com.example.ticketapp.model.User user = userService.loadUserByUsername(principal.getName());
            ticket.setCreatedBy(user);
            
            ticketService.saveTicket(ticket);
            return ResponseEntity.ok("Ticket de test créé avec ID: " + ticket.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }
}