package com.example.ticketapp.controller;

import com.example.ticketapp.model.Ticket;
import com.example.ticketapp.service.TicketService;
import com.example.ticketapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import org.springframework.web.multipart.MultipartFile;

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
    public String createTicket(@ModelAttribute Ticket ticket, 
                             @RequestParam(required = false) MultipartFile file,
                             Principal principal) {
        // Associer l'utilisateur connecté
        com.example.ticketapp.model.User user = userService.loadUserByUsername(principal.getName());
        ticket.setCreatedBy(user);
        
        // Gérer le fichier uploadé si présent
        if (file != null && !file.isEmpty()) {
            // Ici vous pouvez sauvegarder le fichier et stocker le chemin dans le ticket
            // Par exemple :
            // String filePath = fileStorageService.store(file);
            // ticket.setAttachmentPath(filePath);
        }
        
        ticketService.saveTicket(ticket);
        return "redirect:/dashboard";
    }
}