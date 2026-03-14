package com.cgipraktika.reserveerimine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int seats;
    private String zone;

    @Builder.Default
    private boolean occupied = false;

    @Builder.Default
    private boolean hasWindow = false;

    @Builder.Default
    private boolean isPrivate = false;

    @Builder.Default
    private boolean isAccessible = false;

    @Builder.Default
    private boolean nearKidsZone = false;
}
