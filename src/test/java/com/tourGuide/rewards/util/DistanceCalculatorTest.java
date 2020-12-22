package com.tourGuide.rewards.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tourGuide.rewards.domain.Location;
import com.tourGuide.rewards.domain.VisitedLocation;
import com.tourGuide.rewards.domain.dto.AttractionDto;

@SpringBootTest
public class DistanceCalculatorTest {

    @Autowired
    private DistanceCalculator distanceCalculator;

    private Location tourEiffelLocation;
    private Location disneylandParisLocation;

    private VisitedLocation visitedLocation;
    private AttractionDto attractionDto;

    @BeforeEach
    public void setUpPerTest() {
        tourEiffelLocation = new Location(48.858331, 2.294481);
        disneylandParisLocation = new Location(48.872448, 2.776794);
    }

    @Test
    @Tag("isWithinAttractionProximity")
    @DisplayName("isWithinAttractionProximity - True")
    public void aaa() {
        // GIVEN
        attractionDto = new AttractionDto("Tour Eiffel", tourEiffelLocation,
                "Paris", "France", UUID.randomUUID());

        // WHEN
        boolean result = distanceCalculator
                .isWithinAttractionProximity(attractionDto, tourEiffelLocation);

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    @Tag("isWithinAttractionProximity")
    @DisplayName("isWithinAttractionProximity - False")
    public void aaaa() {
        // GIVEN
        attractionDto = new AttractionDto("Tour Eiffel",
                new Location(8.858331, 2.294481), "Paris", "France",
                UUID.randomUUID());

        // WHEN
        boolean result = distanceCalculator
                .isWithinAttractionProximity(attractionDto, tourEiffelLocation);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    @Tag("isNearAttraction")
    @DisplayName("Is Near Attraction - True")
    public void givenVisitedLocationNear_whenBoolean_thenReturnTrue() {
        // GIVEN
        visitedLocation = new VisitedLocation(UUID.randomUUID(),
                tourEiffelLocation, new Date());
        attractionDto = new AttractionDto("Tour Eiffel", tourEiffelLocation,
                "Paris", "France", UUID.randomUUID());

        // WHEN
        boolean result = distanceCalculator.isNearAttraction(visitedLocation,
                attractionDto);

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    @Tag("isNearAttraction")
    @DisplayName("Is Near Attraction - False")
    public void givenVisitedLocationFar_whenBoolean_thenReturnFalse() {
        // GIVEN
        visitedLocation = new VisitedLocation(UUID.randomUUID(),
                new Location(8.858331, 2.294481), new Date());
        attractionDto = new AttractionDto("Tour Eiffel", tourEiffelLocation,
                "Paris", "France", UUID.randomUUID());

        // WHEN
        boolean result = distanceCalculator.isNearAttraction(visitedLocation,
                attractionDto);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    @Tag("getDistanceInMiles")
    @DisplayName("Get distance in Miles - OK")
    public void givenTwoValidLocation_whenGetDistance_thenReturnCorrectDouble() {
        // GIVEN
        // WHEN
        double result = distanceCalculator.getDistanceInMiles(
                tourEiffelLocation, disneylandParisLocation);

        // THEN
        assertThat(result).isEqualTo(21.928803618826255d);
    }

}
