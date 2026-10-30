package ru.nipineft.appointmentbookingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AppointmentBookingSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AppointmentBookingSystemApplication.class, args);
    }

}
