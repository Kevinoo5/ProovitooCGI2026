package com.cgipraktika.reserveerimine.repository;
import com.cgipraktika.reserveerimine.model.Booking;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    @NullMarked
    @Override
    List<Booking> findAll();
}
