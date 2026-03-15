package com.cgipraktika.reserveerimine.repository;

import com.cgipraktika.reserveerimine.model.TableEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends CrudRepository<TableEntity, Long> {

    @NullMarked
    @Override
    List<TableEntity> findAll();

    List<TableEntity> findByOccupiedFalseAndSeatsGreaterThanEqual(int seatsIsGreaterThan);
}

