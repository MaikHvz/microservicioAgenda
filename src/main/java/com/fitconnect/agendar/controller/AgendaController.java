package com.fitconnect.agendar.controller;

import com.fitconnect.agendar.model.Agenda;
import com.fitconnect.agendar.service.AgendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    // Crear una nueva agenda
    @PostMapping
    public ResponseEntity<?> crearAgenda(@RequestBody Agenda agenda) {
        try {
            Agenda agendaCreada = agendaService.crearAgenda(agenda);
            return ResponseEntity.ok(agendaCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Listar todas las agendas
    @GetMapping
    public ResponseEntity<List<Agenda>> listarTodas() {
        List<Agenda> agendas = agendaService.listarTodas();
        return ResponseEntity.ok(agendas);
    }

    // Buscar agenda por id
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Optional<Agenda> agenda = agendaService.buscarPorId(id);
        return agenda.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar agenda por id
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAgenda(@PathVariable Integer id, @RequestBody Agenda agenda) {
        try {
            Agenda agendaActualizada = agendaService.actualizarAgenda(id, agenda);
            return ResponseEntity.ok(agendaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar agenda por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAgenda(@PathVariable Integer id) {
        try {
            agendaService.eliminarAgenda(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
