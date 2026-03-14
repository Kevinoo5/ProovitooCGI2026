package com.cgipraktika.reserveerimine.service;

import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;

    public List<TableEntity> getAllOpenTables(int peopleCount) {
        return tableRepository.findByOccupiedFalseAndSeatsGreaterThanEqual(peopleCount);
    }
}
