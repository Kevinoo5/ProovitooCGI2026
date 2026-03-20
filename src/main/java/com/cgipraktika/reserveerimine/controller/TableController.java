package com.cgipraktika.reserveerimine.controller;

import com.cgipraktika.reserveerimine.dto.SearchResponseDTO;
import com.cgipraktika.reserveerimine.dto.TableGetRequestDTO;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.model.TableFeature;
import com.cgipraktika.reserveerimine.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping()
    public ResponseEntity<List<TableEntity>> getTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/features")
    public List<Map<String, String>> getAllFeatures() {
        return Stream.of(TableFeature.values()).map(f -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", f.name());
            map.put("label", f.getLabel());
            return map;
        }).toList();
    }

    @GetMapping("/availableFor")
    public ResponseEntity<SearchResponseDTO> getOpenTables(@ModelAttribute TableGetRequestDTO request) {
        SearchResponseDTO response = tableService.searchAvailableTables(request);

        return ResponseEntity.ok(response);
    }
}
