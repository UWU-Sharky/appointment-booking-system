package ru.nipineft.appointmentbookingsystem.dto;


import java.time.LocalDateTime;

public record AppointmentRequest(
        String clientName,
        String clientPhoneNumber,
        String clientEmail,
        Long masterId,
        Long serviceId,
        LocalDateTime startTime
) {}