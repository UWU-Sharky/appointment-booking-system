package ru.nipineft.appointmentbookingsystem.dto;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long clientId,
        Long masterId,
        Long serviceId,
        LocalDateTime startTime,
        String status
) {}