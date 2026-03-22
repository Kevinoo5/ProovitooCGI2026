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

/**
 * Kontroller, mis haldab restoranilaudadega seotud API päringuid.
 */
@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    // Dependency Injection läbi konstruktori
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    /**
     * Tagastab kõik süsteemis olevad lauad ja nende staatuse.
     * @return Kõikide laudade info
     */
    @GetMapping()
    public ResponseEntity<List<TableEntity>> getTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    /**
     * Laua võimalike filtrite tagastamine
     * @return Kõik võimalikud filtri parameetrid
     */
    @GetMapping("/features")
    public List<Map<String, String>> getAllFeatures() {
        return Stream.of(TableFeature.values()).map(f -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", f.name());
            map.put("label", f.getLabel());
            return map;
        }).toList();
    }

    /**
     * Otsing ja skooride omistamine
     * * @param request Filtreerimise tingimused (aeg, inimeste arv, eelistused)
     * @return SearchResponseDTO, mis sisaldab vabu laudu ja esiletõstetud soovitust.
     */
    @GetMapping("/availableFor")
    public ResponseEntity<SearchResponseDTO> getOpenTables(@ModelAttribute TableGetRequestDTO request) {
        // Logika asub Service kihis, et hoida kontroller õhuke ja testitav
        SearchResponseDTO response = tableService.searchAvailableTables(request);

        return ResponseEntity.ok(response);
    }
}