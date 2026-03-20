package com.cgipraktika.reserveerimine.dto;

import java.time.LocalDateTime;

public record BookingRequestDTO (
    Long tableId,
    String customerName,
    LocalDateTime startTime,
    LocalDateTime endTime,
    int guestCount
) {}