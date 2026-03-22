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

/**
 * Teenuskiht, mis tegeleb laudade leidmise ja haldamisega
 */
@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    private final BookingRepository bookingRepository;

    /**
     * Tagastab kõik süsteemis olevad lauad.
     * @return Kõikide laudade nimekiri
     */
    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    /**
     * Arvutab lauale skoori vastavalt selle sobivusele seltskonna suuruse ja eelistustega.
     * @param table Kontrollitav laud
     * @param guests Inimeste arv seltskonnas
     * @param features Kliendi poolt soovitud lisaparameetrid
     * @return Laua skoor (Mida kõrgem seda parem)
     */
    public int getTableScore(TableEntity table, int guests, Set<TableFeature> features) {
        Set<TableFeature> safePrefs = Objects.requireNonNullElse(features, Collections.emptySet());
        Set<TableFeature> tableFeatures = Objects.requireNonNullElse(table.getFeatures(), Collections.emptySet());

        int score = 0;

        // Skoorimine vastavalt istekohtadele (täpselt sobiv laud saab max punktid)
        if (table.getSeats() == guests) {
            score += 50;
        } else if (table.getSeats() > guests) {
            // Karistuspunktid iga tühja koha eest
            score += 50 - (table.getSeats() - guests) * 2;
        } else {
            // Laud on liiga väike (Seda ei peaks juhtuma, aga igaks juhuks on ka see kontroll olemas)
            score += 50 - (guests - table.getSeats()) * 2;
        }

        // Skoorimine vastavalt kliendi eelistustele (Iga sobiv filter annab 5 lisapunkti)
        long matchingFilters = tableFeatures.stream().filter(safePrefs::contains).count();
        score += (int) (matchingFilters * 5);

        return score;
    }

    /**
     * Otsib vabu laudu valitud ajavahemikus ja leiab neist kõige sobivama.
     * @param tableGetRequestDTO Otsingu kriteeriumid (aeg, külalised, eelistused)
     * @return SearchResponseDTO, mis sisaldab kõiki laudu, hõivatud laudade ID-sid ja parimat soovitust.
     */
    public SearchResponseDTO searchAvailableTables(TableGetRequestDTO tableGetRequestDTO) {
        int bestTableScore = 0;
        long bestTableId = 0;

        List<TableEntity> allTables = tableRepository.findAll();

        // Leiame andmebaasist kõik lauad, millel on valitud ajal juba broneering olemas
        Set<Long> occupiedIds = new HashSet<>(bookingRepository.findOccupiedTableIds(
                tableGetRequestDTO.startTime(),
                tableGetRequestDTO.endTime()
        ));

        // Kontrollime üle kõik lauad, et leida parim
        for (TableEntity table : allTables) {
            boolean isOccupied = occupiedIds.contains(table.getId());
            boolean enoughSeats = table.getSeats() >= tableGetRequestDTO.guests();

            // Kui laud on vaba ja sinna mahub seltskond ära, omistame lauale skoori
            if (!isOccupied && enoughSeats) {
                int currentScore = getTableScore(table, tableGetRequestDTO.guests(), tableGetRequestDTO.preferredFeatures());

                // Salvestame parima skooriga laua soovituseks
                if (currentScore > bestTableScore) {
                    bestTableScore = currentScore;
                    bestTableId = table.getId();
                }
            }
        }

        return new SearchResponseDTO(allTables, occupiedIds, bestTableId);
    }
}