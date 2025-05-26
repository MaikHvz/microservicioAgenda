package com.fitconnect.agendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendarApplication.class, args);
	}

}
//http://localhost:8080/api/agendas
//{
//  "nombreCliente": "Benjamin Tapion",
//  "rutCliente": "21345678-9",
//  "idServicio": 2,
//  "fecha": "2025-05-19",
//  "hora": "12:30",
//  "emailCliente": "benjamin.munoz@mail.com"
//}

//{
//  "nombreCliente": "agustin fernandez",
//  "rutCliente": "23345678-9",
//  "idServicio": 2,
//  "fecha": "2025-05-26",
//  "hora": "12:50",
//  "emailCliente": "agustin.fer@mail.com"
//}


