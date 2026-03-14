package com.cgipraktika.reserveerimine.controller;

import com.cgipraktika.reserveerimine.dto.SearchResponse;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import com.cgipraktika.reserveerimine.service.TableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/greeting")
    public String getGreetingData() {
        return "Welcome to the Restaurant!";
    }

    @GetMapping("/availableFor")
    public SearchResponse getOpenTables(@RequestParam int guests, @RequestParam(required=false) String[] prefs) {
        List<TableEntity> tables = tableService.getAllOpenTables(guests);

        Long suggestion = tables.isEmpty() ? null : tables.getFirst().getId();

        return new SearchResponse(tables, suggestion);
    }
}
