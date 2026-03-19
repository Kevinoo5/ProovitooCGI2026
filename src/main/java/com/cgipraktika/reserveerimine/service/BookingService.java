package com.cgipraktika.reserveerimine.service;

import com.cgipraktika.reserveerimine.dto.BookingRequestDTO;
import com.cgipraktika.reserveerimine.model.Booking;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.repository.BookingRepository;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking addNewBooking(BookingRequestDTO booking) {
        if (booking.getTableId() == null) {
            throw new IllegalArgumentException("Table ID can't be null!" + booking);
        }

        TableEntity table = tableRepository.findById(booking.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found!"));

        Booking tableBooking = Booking.builder()
                .table(table)
                .customerName(booking.getCustomerName())
                .guestCount(booking.getGuestCount())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .build();
        
        return bookingRepository.save(tableBooking);
    }
}