package ru.nipineft.appointmentbookingsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nipineft.appointmentbookingsystem.repository.AppointmentRepository;
import ru.nipineft.appointmentbookingsystem.dto.AppointmentRequest;
import ru.nipineft.appointmentbookingsystem.dto.AppointmentResponse;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;

    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        try {
            AppointmentResponse response = appointmentRepository.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Если сработает ваш триггер trg_appointment_profile или ошибка мапинга, вернется 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Не удалось создать запись: " + e.getMessage());
        }
    }
}
