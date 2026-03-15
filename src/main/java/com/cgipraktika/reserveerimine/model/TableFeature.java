package com.cgipraktika.reserveerimine.model;

import lombok.Getter;

@Getter
public enum TableFeature {
    WINDOW("Window Seat"),
    PRIVATE("Quiet Corner"),
    ACCESSIBLE("Accessible"),
    KIDS_ZONE("Kids Zone"),
    TERRACE("Terrace"),
    SILENT("Silent Area");

    private final String label;

    TableFeature(String label) {
        this.label = label;
    }

}