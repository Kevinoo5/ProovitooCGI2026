package com.cgipraktika.reserveerimine.controller;

import com.cgipraktika.reserveerimine.dto.BookingRequestDTO;
import com.cgipraktika.reserveerimine.model.Booking;
import com.cgipraktika.reserveerimine.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroller broneeringute haldamiseks
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    /**
     * Kõigi broneeringute tagastamine
     * @return Kõikide broneeringute list
     */
    @GetMapping({"", "/"})
    public ResponseEntity<List<Booking>> getBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    /**
     * Broneeringu tagastamine broneeringu ID kaudu
     * @param id Broneeringu ID
     * @return Broneering
     */
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Broneeringu lisamine
     * @param dto Broneeringu loomise päringu objekt
     * @return Uus broneering
     */
    @PostMapping({"", "/"})
    public ResponseEntity<Booking> addBooking(@RequestBody BookingRequestDTO dto) {
        Booking newBooking = bookingService.addNewBooking(dto);
        return ResponseEntity.ok(newBooking);
    }

}