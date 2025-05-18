package com.fitconnect.agendar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "agenda")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_cliente", nullable = false)
    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    @Column(name = "rut_cliente", length = 10, nullable = false)
    @NotBlank(message = "El RUT del cliente es obligatorio")
    @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "El RUT debe tener el formato correcto, por ejemplo: 12345678-9")
    private String rutCliente;

    @Column(name = "id_servicio", nullable = false)
    @NotNull(message = "El id del servicio es obligatorio")
    private Integer idServicio;

    @Column(name = "fecha", nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @Column(name = "email_cliente", nullable = false, length = 100)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String emailCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AgendaEstado estadoAgenda = AgendaEstado.AGENDADA;
}
