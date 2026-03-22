package com.cgipraktika.reserveerimine.dto;

import com.cgipraktika.reserveerimine.model.TableFeature;
import java.util.Set;

public record TableResponseDTO(
        Long id,
        int seats,
        String zone,
        boolean occupied,
        boolean recommended,
        Set<TableFeature> features
) {}