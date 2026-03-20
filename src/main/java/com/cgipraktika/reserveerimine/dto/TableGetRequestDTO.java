package com.cgipraktika.reserveerimine.dto;

import com.cgipraktika.reserveerimine.model.TableFeature;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

public record TableGetRequestDTO(
    int guests,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startTime,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime endTime,
    Set<TableFeature> preferredFeatures
) {
    public TableGetRequestDTO {
        if (preferredFeatures == null) {
            preferredFeatures = Set.of();
        }
    }
}