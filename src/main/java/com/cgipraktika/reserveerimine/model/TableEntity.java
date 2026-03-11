package com.cgipraktika.reserveerimine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TableEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int seats;
    private String zone;
    private boolean hasWindow;
    private boolean isPrivate;
    private boolean isAccessible;
    private boolean nearKidsZone;

    protected TableEntity() {}

    public TableEntity(int seats, String zone, boolean hasWindow, boolean isPrivate, boolean isAccessible, boolean nearKidsZone) {
        this.seats = seats;
        this.zone = zone;
        this.hasWindow = hasWindow;
        this.isPrivate = isPrivate;
        this.isAccessible = isAccessible;
        this.nearKidsZone = nearKidsZone;
    }
}
