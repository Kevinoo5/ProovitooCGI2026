package com.cgipraktika.reserveerimine.dto;

import com.cgipraktika.reserveerimine.model.TableEntity;
import java.util.List;

public record SearchResponse(
        List<TableEntity> allTables,
        Long suggestedTableId
) {}