package com.example.ticketapp.repository;

import com.example.ticketapp.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(Ticket.Status status);
    List<Ticket> findByCreatedBy_Id(Long userId);
}
