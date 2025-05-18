package com.fitconnect.agendar.service;

import com.fitconnect.agendar.model.Agenda;
import com.fitconnect.agendar.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    // Validar traslapes
    private boolean hayConflicto(LocalDate fecha, LocalTime horaInicioNueva, Integer idExcluir) {
        List<Agenda> agendasDelDia = agendaRepository.findByFecha(fecha);
        LocalTime horaFinNueva = horaInicioNueva.plusHours(1);

        for (Agenda agendaExistente : agendasDelDia) {
            // Si es actualización, excluir el mismo registro
            if (idExcluir != null && agendaExistente.getId().equals(idExcluir)) {
                continue;
            }

            LocalTime horaInicioExistente = agendaExistente.getHora();
            LocalTime horaFinExistente = horaInicioExistente.plusHours(1);

            boolean seTraslapan = horaInicioNueva.isBefore(horaFinExistente) && horaInicioExistente.isBefore(horaFinNueva);

            if (seTraslapan) {
                return true;  // hay conflicto
            }
        }
        return false; // no hay conflicto
    }

    // Crear nueva agenda
    public Agenda crearAgenda(Agenda nuevaAgenda) {
        if (hayConflicto(nuevaAgenda.getFecha(), nuevaAgenda.getHora(), null)) {
            throw new IllegalArgumentException("Ya existe una cita en ese horario");
        }
        return agendaRepository.save(nuevaAgenda);
    }

    // Obtener todas las agendas
    public List<Agenda> listarTodas() {
        return agendaRepository.findAll();
    }

    // Buscar agenda por id
    public Optional<Agenda> buscarPorId(Integer id) {
        return agendaRepository.findById(id);
    }

    // Actualizar agenda
    public Agenda actualizarAgenda(Integer id, Agenda agendaActualizada) {
        Optional<Agenda> agendaExistenteOpt = agendaRepository.findById(id);
        if (agendaExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Agenda no encontrada con id " + id);
        }

        if (hayConflicto(agendaActualizada.getFecha(), agendaActualizada.getHora(), id)) {
            throw new IllegalArgumentException("Ya existe una cita en ese horario");
        }

        Agenda agendaExistente = agendaExistenteOpt.get();

        // Actualizar campos (puedes usar setters o builder)
        agendaExistente.setNombreCliente(agendaActualizada.getNombreCliente());
        agendaExistente.setRutCliente(agendaActualizada.getRutCliente());
        agendaExistente.setIdServicio(agendaActualizada.getIdServicio());
        agendaExistente.setFecha(agendaActualizada.getFecha());
        agendaExistente.setHora(agendaActualizada.getHora());
        agendaExistente.setEmailCliente(agendaActualizada.getEmailCliente());

        return agendaRepository.save(agendaExistente);
    }

    // Eliminar agenda por id
    public void eliminarAgenda(Integer id) {
        if (!agendaRepository.existsById(id)) {
            throw new IllegalArgumentException("Agenda no encontrada con id " + id);
        }
        agendaRepository.deleteById(id);
    }
}
