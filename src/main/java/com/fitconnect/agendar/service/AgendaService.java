package com.fitconnect.agendar.service;

import com.fitconnect.agendar.model.Agenda;
import com.fitconnect.agendar.model.AgendaEstado;
import com.fitconnect.agendar.repository.AgendaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    private boolean hayConflicto(LocalDate fecha, LocalTime horaInicioNueva, Integer idExcluir) {
        LocalTime horaFinNueva = horaInicioNueva.plusHours(1);
        List<Agenda> agendasDelDia = agendaRepository.findByFecha(fecha);

        for (Agenda agenda : agendasDelDia) {
            if (idExcluir != null && agenda.getId().equals(idExcluir)) continue;
            if (agenda.getEstado() == AgendaEstado.COMPLETADA) continue;

            LocalTime inicio = agenda.getHora();
            LocalTime fin = inicio.plusHours(1);

            boolean traslape = horaInicioNueva.isBefore(fin) && inicio.isBefore(horaFinNueva);
            if (traslape) return true;
        }
        return false;
    }

    public Agenda crearAgenda(Agenda nuevaAgenda) {
        if (hayConflicto(nuevaAgenda.getFecha(), nuevaAgenda.getHora(), null)) {
            throw new IllegalArgumentException("Ya existe una cita en ese horario");
        }
        nuevaAgenda.setEstado(AgendaEstado.AGENDADA);
        return agendaRepository.save(nuevaAgenda);
    }

    public List<Agenda> listarTodas() {
        return agendaRepository.findAll();
    }

    public Optional<Agenda> buscarPorId(Integer id) {
        return agendaRepository.findById(id);
    }

    public Agenda actualizarAgenda(Integer id, Agenda agendaActualizada) {
        Agenda existente = agendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agenda no encontrada con id " + id));

        if (hayConflicto(agendaActualizada.getFecha(), agendaActualizada.getHora(), id)) {
            throw new IllegalArgumentException("Ya existe una cita en ese horario");
        }

        existente.setNombreCliente(agendaActualizada.getNombreCliente());
        existente.setRutCliente(agendaActualizada.getRutCliente());
        existente.setIdServicio(agendaActualizada.getIdServicio());
        existente.setFecha(agendaActualizada.getFecha());
        existente.setHora(agendaActualizada.getHora());
        existente.setEmailCliente(agendaActualizada.getEmailCliente());

        return agendaRepository.save(existente);
    }

    public void eliminarAgenda(Integer id) {
        if (!agendaRepository.existsById(id)) {
            throw new IllegalArgumentException("Agenda no encontrada con id " + id);
        }
        agendaRepository.deleteById(id);
    }

    // Actualizar estado cada minuto
    @Scheduled(cron = "0 * * * * *") // cada minuto
    public void actualizarEstadosDeAgenda() {
        List<Agenda> todas = agendaRepository.findAll();
        LocalDateTime ahora = LocalDateTime.now();

        for (Agenda agenda : todas) {
            if (agenda.getEstado() == AgendaEstado.COMPLETADA) continue;

            LocalDateTime inicio = LocalDateTime.of(agenda.getFecha(), agenda.getHora());
            LocalDateTime fin = inicio.plusHours(1);

            if (ahora.isAfter(fin)) {
                agenda.setEstado(AgendaEstado.COMPLETADA);
            } else if (!ahora.isBefore(inicio) && ahora.isBefore(fin)) {
                agenda.setEstado(AgendaEstado.EN_CURSO);
            } else {
                agenda.setEstado(AgendaEstado.AGENDADA);
            }
            agendaRepository.save(agenda);
        }
    }
}
