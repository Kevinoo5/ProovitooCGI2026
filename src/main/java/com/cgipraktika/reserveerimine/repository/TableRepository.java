package com.cgipraktika.reserveerimine.repository;

import com.cgipraktika.reserveerimine.model.TableEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends CrudRepository<TableEntity, Long> {

    List<TableEntity> findByZone(String zone);

    List<TableEntity> findByOccupiedFalseAndSeatsGreaterThanEqual(int seatsIsGreaterThan);
}

