package com.cgipraktika.reserveerimine.controller;

import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TableController {
    private final TableRepository tableRepository;

    public TableController(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @GetMapping("/greeting")
    public String getGreetingData() {
        return "Welcome to the Restaurant!";
    }

    @GetMapping("/availableFor{guests}")
    public List<TableEntity> getOpenTables(@PathVariable int guests) {
        return tableRepository.findByOccupiedFalseAndSeatsGreaterThanEqual(guests);
    }
}
