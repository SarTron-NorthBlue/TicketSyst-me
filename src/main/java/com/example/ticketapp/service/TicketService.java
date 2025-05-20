package com.example.ticketapp.service;

import com.example.ticketapp.model.Ticket;
import com.example.ticketapp.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByStatus(Ticket.Status status) {
        return ticketRepository.findByStatus(status);
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
