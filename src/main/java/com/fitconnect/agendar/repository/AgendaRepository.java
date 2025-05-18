package com.fitconnect.agendar.repository;

import com.fitconnect.agendar.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {
    List<Agenda> findByFecha(LocalDate fecha);
}
