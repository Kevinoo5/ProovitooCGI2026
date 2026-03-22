package com.cgipraktika.reserveerimine.service;

import com.cgipraktika.reserveerimine.dto.SearchResponseDTO;
import com.cgipraktika.reserveerimine.dto.TableGetRequestDTO;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.model.TableFeature;
import com.cgipraktika.reserveerimine.repository.BookingRepository;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    private final BookingRepository bookingRepository;

    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    public int getTableScore(TableEntity table, int guests, Set<TableFeature> features) {
        Set<TableFeature> safePrefs = Objects.requireNonNullElse(features, Collections.emptySet());
        Set<TableFeature> tableFeatures = Objects.requireNonNullElse(table.getFeatures(), Collections.emptySet());

        int score = 0;
        if (table.getSeats() == guests) {
            score += 50;
        } else if (table.getSeats() > guests) {
            score += 50 - (table.getSeats() - guests) * 2;
        } else {
            score += 50 - (guests - table.getSeats()) * 2;
        }

        long matchingFilters = tableFeatures.stream().filter(safePrefs::contains).count();
        score += (int) (matchingFilters * 5);

        return score;
    }

    public SearchResponseDTO searchAvailableTables(TableGetRequestDTO tableGetRequestDTO) {
        int bestTable = 0;
        long bestTableId = 0;

        List<TableEntity> allTables = tableRepository.findAll();
        Set<Long> occupiedIds = new HashSet<>(bookingRepository.findOccupiedTableIds(tableGetRequestDTO.startTime(), tableGetRequestDTO.endTime()));

        for (TableEntity table : allTables) {
            boolean isOccupied = occupiedIds.contains(table.getId());
            boolean enoughSeats = table.getSeats() >= tableGetRequestDTO.guests();

            if (!isOccupied && enoughSeats) {
                int currentScore = getTableScore(table, tableGetRequestDTO.guests(), tableGetRequestDTO.preferredFeatures());
                long currentId = table.getId();
                if (currentScore > bestTable) {
                    bestTable = currentScore;
                    bestTableId = currentId;
                }
            }
        }

        return new SearchResponseDTO(allTables, occupiedIds, bestTableId);
    }
}
