package com.cgipraktika.reserveerimine.service;

import com.cgipraktika.reserveerimine.dto.BookingRequestDTO;
import com.cgipraktika.reserveerimine.model.Booking;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.repository.BookingRepository;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(long id) {
        return bookingRepository.findById(id);
    }

    public Booking addNewBooking(BookingRequestDTO booking) {
        if (booking.tableId() == null) {
            throw new IllegalArgumentException("Table ID can't be null!" + booking);
        }

        TableEntity table = tableRepository.findById(booking.tableId())
                .orElseThrow(() -> new RuntimeException("Table not found!"));

        Booking tableBooking = Booking.builder()
                .table(table)
                .customerName(booking.customerName())
                .guestCount(booking.guestCount())
                .startTime(booking.startTime())
                .endTime(booking.endTime())
                .build();
        
        return bookingRepository.save(tableBooking);
    }
}