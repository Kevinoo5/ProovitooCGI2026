package com.cgipraktika.reserveerimine.controller;

import com.cgipraktika.reserveerimine.dto.SearchResponse;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.model.TableFeature;
import com.cgipraktika.reserveerimine.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/tables")
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
    public ResponseEntity<SearchResponse> getOpenTables(@RequestParam int guests, @RequestParam(required=false) Set<TableFeature> prefs) {
        List<TableEntity> tables = tableService.getAllTables();
        Set<TableFeature> safePrefs = (prefs == null) ? Set.of() : prefs;

        return ResponseEntity.ok(tableService.getRecommendedTables(tables, guests, safePrefs));
    }
}
