package ru.nipineft.appointmentbookingsystem.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nipineft.appointmentbookingsystem.dto.AppointmentRequest;
import ru.nipineft.appointmentbookingsystem.dto.AppointmentResponse;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        // 1. Ищем или создаем клиента по номеру телефона (он у вас UNIQUE)
        Long clientId = getOrCreateClient(request.clientName(), request.clientPhoneNumber(), request.clientEmail());

        // 2. Вставляем запись в appointments. По умолчанию статус 'NEW' проставит сама БД
        String insertAppointmentSql = "INSERT INTO appointments (client_id, master_id, service_id, start_time) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertAppointmentSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, clientId);
            ps.setLong(2, request.masterId());
            ps.setLong(3, request.serviceId());
            ps.setTimestamp(4, Timestamp.valueOf(request.startTime()));
            return ps;
        }, keyHolder);

        Long appointmentId = ((Number) keyHolder.getKeys().get("id")).longValue();

        return new AppointmentResponse(
                appointmentId,
                clientId,
                request.masterId(),
                request.serviceId(),
                request.startTime(),
                "NEW"
        );
    }

    private Long getOrCreateClient(String name, String phoneNumber, String email) {
        String selectSql = "SELECT id FROM clients WHERE phone_number = ?";
        try {
            return jdbcTemplate.queryForObject(selectSql, Long.class, phoneNumber);
        } catch (EmptyResultDataAccessException e) {
            // Если клиента нет, создаем его
            String insertClientSql = "INSERT INTO clients (name, phone_number, email) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertClientSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, phoneNumber);
                ps.setString(3, email);
                return ps;
            }, keyHolder);

            return ((Number) keyHolder.getKeys().get("id")).longValue();
        }
    }
}
