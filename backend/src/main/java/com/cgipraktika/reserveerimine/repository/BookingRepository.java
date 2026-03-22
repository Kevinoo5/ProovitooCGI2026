package com.cgipraktika.reserveerimine.repository;
import com.cgipraktika.reserveerimine.model.Booking;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    @NullMarked
    @Override
    List<Booking> findAll();

    @Query("SELECT b.table.id FROM Booking b WHERE :start < b.endTime AND :end > b.startTime")
    List<Long> findOccupiedTableIds(LocalDateTime start, LocalDateTime end);
}
