package com.cgipraktika.reserveerimine.dto;

import com.cgipraktika.reserveerimine.model.TableEntity;
import java.util.List;
import java.util.Set;

public record SearchResponseDTO(
        List<TableEntity> allTables,
        Set<Long> occupiedTableIds,
        Long suggestedTableId
) {}