package com.cgipraktika.reserveerimine.service;

import com.cgipraktika.reserveerimine.dto.SearchResponse;
import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.model.TableFeature;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;

    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    public List<TableEntity> getAllOpenTables(int peopleCount) {
        return tableRepository.findByOccupiedFalseAndSeatsGreaterThanEqual(peopleCount);
    }

    public int getTableScore(TableEntity table, int guests, Set<TableFeature> features) {
        int score = 0;
        if (table.getSeats() == guests) {
            score += 50;
        } else if (table.getSeats() > guests) {
            score += 50 - (table.getSeats() - guests) * 2;
        }

        long matchingFilters = table.getFeatures().stream().filter(features::contains).count();
        score += (int) (matchingFilters * 5);

        return score;
    }

    public SearchResponse getRecommendedTables(List<TableEntity> tables, int guests, Set<TableFeature> prefs) {
        int bestTable = 0;
        long bestTableId = 0;

        for (TableEntity table : tables) {
            int currentScore = getTableScore(table, guests, prefs);
            long currentId = table.getId();
            if (currentScore > bestTable) {
                bestTable = currentScore;
                bestTableId = currentId;
            }
        }

        return new SearchResponse(tables, bestTableId);
    }
}
