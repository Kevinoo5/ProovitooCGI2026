package com.cgipraktika.reserveerimine.controller;

import com.cgipraktika.reserveerimine.dto.SearchResponse;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.model.TableFeature;
import com.cgipraktika.reserveerimine.service.TableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/availableFor")
    public SearchResponse getOpenTables(@RequestParam int guests, @RequestParam(required=false) Set<TableFeature> prefs) {
        List<TableEntity> tables = tableService.getAllOpenTables(guests);
        Set<TableFeature> safePrefs = (prefs == null) ? Set.of() : prefs;

        return tableService.getRecommendedTables(tables, guests, safePrefs);
    }
}
