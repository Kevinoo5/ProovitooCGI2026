package com.cgipraktika.reserveerimine.config;

import com.cgipraktika.reserveerimine.model.Booking;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.model.TableFeature;
import com.cgipraktika.reserveerimine.repository.BookingRepository;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TableRepository tableRepository;
    private final BookingRepository bookingRepository;

    @Override
    public void run(String... args) {
        TableEntity t1 = createTable(2, "Window", Set.of(TableFeature.WINDOW, TableFeature.PRIVATE));
        TableEntity t2 = createTable(4, "Center", Set.of(TableFeature.KIDS_ZONE));
        TableEntity t3 = createTable(2, "Window", Set.of(TableFeature.WINDOW));
        TableEntity t4 = createTable(6, "Main Hall", Set.of(TableFeature.ACCESSIBLE));
        TableEntity t5 = createTable(4, "Center", Set.of(TableFeature.PRIVATE));

        tableRepository.saveAll(List.of(t1, t2, t3, t4, t5));

        Booking b1 = Booking.builder()
                .table(t1)
                .customerName("Jane Doe")
                .guestCount(2)
                .startTime(LocalDateTime.now().withHour(18).withMinute(0))
                .endTime(LocalDateTime.now().withHour(20).withMinute(0))
                .build();

        Booking b2 = Booking.builder()
                .table(t4)
                .customerName("John Smith")
                .guestCount(5)
                .startTime(LocalDateTime.now().withHour(19).withMinute(0))
                .endTime(LocalDateTime.now().withHour(21).withMinute(0))
                .build();

        bookingRepository.saveAll(List.of(b1, b2));
    }

    private TableEntity createTable(int seats, String zone, Set<TableFeature> features) {
        return TableEntity.builder()
                .seats(seats)
                .zone(zone)
                .features(features)
                .build();
    }
}