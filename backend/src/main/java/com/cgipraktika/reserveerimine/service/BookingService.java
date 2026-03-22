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

/**
 * Teenuskiht, mis tegeleb broneeringute haldamise ja salvestamisega.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;

    /**
     * Leiab kõik süsteemi salvestatud broneeringud.
     * @return Broneeringute nimekiri
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Otsib konkreetset broneeringut ID järgi.
     * @param id Broneeringu identifikaator
     * @return Leitud broneering või tühi vastus
     */
    public Optional<Booking> getBookingById(long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Loob ja salvestab uue broneeringu süsteemi.
     * Kontrollib laua olemasolu ja seostab selle broneeringuga.
     * @param booking Broneeringu andmed (klient, aeg, laua ID)
     * @return Salvestatud broneeringu objekt
     * @throws IllegalArgumentException Kui laua ID puudub
     * @throws RuntimeException Kui antud ID-ga lauda ei leita
     */
    public Booking addNewBooking(BookingRequestDTO booking) {
        if (booking.tableId() == null) {
            throw new IllegalArgumentException("Table ID can't be null!" + booking);
        }

        // Kontrollime, kas laud on andmebaasis olemas
        TableEntity table = tableRepository.findById(booking.tableId())
                .orElseThrow(() -> new RuntimeException("Table not found!"));

        // Ehitame broneeringu objekti kasutades Builderit ning salvestame selle
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