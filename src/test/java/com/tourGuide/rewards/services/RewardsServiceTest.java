package com.tourGuide.rewards.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tourGuide.rewards.domain.Location;
import com.tourGuide.rewards.domain.UserReward;
import com.tourGuide.rewards.domain.VisitedLocation;
import com.tourGuide.rewards.domain.dto.AttractionDto;
import com.tourGuide.rewards.domain.dto.UserRewardsDto;
import com.tourGuide.rewards.proxies.MicroserviceGpsProxy;
import com.tourGuide.rewards.util.DistanceCalculator;

import rewardCentral.RewardCentral;

@SpringBootTest
public class RewardsServiceTest {

    @Autowired
    public IRewardsService rewardsService;

    @MockBean
    private DistanceCalculator distanceCalculator;

    @MockBean
    private RewardCentral rewardCentral;

    @MockBean
    private MicroserviceGpsProxy microserviceGpsProxy;

    private List<VisitedLocation> visitedLocationsList;
    private List<UserReward> userRewardsList;
    private List<AttractionDto> attractionsDtoList;

    private UUID attractionUUID;
    private UUID userUUID;

    private VisitedLocation visitedLocation;

    private Location location;

    private AttractionDto attractionDto;

    @BeforeEach
    public void setUpPerTest() {
        attractionUUID = UUID.randomUUID();
        userUUID = UUID.randomUUID();
        location = new Location(48.858331, 2.294481);

        visitedLocationsList = new ArrayList<>();
        visitedLocation = new VisitedLocation(UUID.randomUUID(), location,
                new Date());
        visitedLocationsList.add(visitedLocation);

        userRewardsList = new ArrayList<>();

        attractionsDtoList = new ArrayList<>();
        attractionDto = new AttractionDto("Tour Eiffel", location, "Paris",
                "France", UUID.randomUUID());
        attractionsDtoList.add(attractionDto);
    }

    @Test
    @Tag("CalculateRewards")
    @DisplayName("Calculate Rewards - Ok")
    public void givenUserWithVisitedLocation_whencalculateRewardsWithAttractionInTheSameLocation_thenReturnUserRewardsAdded() {
        // GIVEN
        UserRewardsDto user = new UserRewardsDto(UUID.randomUUID(),
                visitedLocationsList, userRewardsList);

        when(microserviceGpsProxy.getAllAttractions())
                .thenReturn(attractionsDtoList);
        when(distanceCalculator.isNearAttraction(visitedLocation,
                attractionDto)).thenReturn(true);

        assertThat(user.getUserRewards().size()).isEqualTo(0);
        // WHEN
        rewardsService.calculateRewards(user);

        // THEN
        assertThat(user.getUserRewards().size()).isEqualTo(1);
    }

    @Test
    @Tag("CalculateRewards")
    @DisplayName("Calculate Rewards - Ok - Differents attractions")
    public void givenTwoVisitedLocationsOnDifferentLocationAttractions_whenCalculate_thenReturnTwoRewards() {
        // GIVEN
        UserRewardsDto user = new UserRewardsDto(UUID.randomUUID(),
                visitedLocationsList, userRewardsList);

        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),
                new Location(48.858331, 2.294481), new Date());
        AttractionDto attraction = new AttractionDto("Tour Eiffel",
                new Location(48.858331, 2.294481), "Paris", "France",
                UUID.randomUUID());

        VisitedLocation visitedLocation2 = new VisitedLocation(user.getUserId(),
                new Location(43.295364, 5.37439), new Date());
        AttractionDto attraction2 = new AttractionDto("Vieux-Port de Marseille",
                new Location(43.295364, 5.37439), "Marseille", "France",
                UUID.randomUUID());

        UserReward userReward = new UserReward(visitedLocation, attraction);
        userReward.setRewardPoints(100);
        user.addUserReward(userReward);

        UserReward userReward2 = new UserReward(visitedLocation2, attraction2);
        userReward2.setRewardPoints(80);
        user.addUserReward(userReward2);

        when(distanceCalculator.isNearAttraction(visitedLocation, attraction))
                .thenReturn(true);
        when(distanceCalculator.isNearAttraction(visitedLocation2, attraction2))
                .thenReturn(true);

        // WHEN
        rewardsService.calculateRewards(user);

        // THEN
        assertThat(user.getUserRewards().size()).isEqualTo(2);
        assertThat(user.getUserRewards().get(0).getRewardPoints())
                .isEqualTo(100);
        assertThat(user.getUserRewards().get(1).getRewardPoints())
                .isEqualTo(80);
    }

    @Test
    @Tag("CalculateRewards")
    @DisplayName("Calculate Rewards - Error - isNearAttraction false")
    public void givenUserWithVisitedLocation_whencalculateRewardsWithFarAttractionLocation_thenReturnUserRewardsNotAdded() {
        // GIVEN
        UserRewardsDto user = new UserRewardsDto(UUID.randomUUID(),
                visitedLocationsList, userRewardsList);

        // set attraction too far (user in Paris, attraction Marseille)
        attractionsDtoList = new ArrayList<>();
        attractionDto = new AttractionDto("Vieux-Port de Marseille",
                new Location(43.295364, 5.37439), "Marseille", "France",
                UUID.randomUUID());
        attractionsDtoList.add(attractionDto);
        when(microserviceGpsProxy.getAllAttractions())
                .thenReturn(attractionsDtoList);

        assertThat(user.getUserRewards().size()).isEqualTo(0);
        // WHEN
        rewardsService.calculateRewards(user);

        // THEN
        assertThat(user.getUserRewards().size()).isEqualTo(0);// unchanged
    }

    @Test
    @Tag("GetAttractionRewards")
    @DisplayName("Get Attraction Rewards - Ok")
    public void givenCorrectValues_whenGetAttractionRewards_thenReturnValue() {
        // GIVEN
        when(rewardCentral.getAttractionRewardPoints(attractionUUID, userUUID))
                .thenReturn(100);

        // WHEN
        int result = rewardsService.getAttractionRewards(attractionUUID,
                userUUID);

        // THEN
        assertThat(result).isGreaterThan(0);
    }

    @Test
    @Tag("GetAttractionRewards")
    @DisplayName("Get Attraction Rewards - ERROR - Null attraction UUID")
    public void givenNullAttractionId_whenGetAttractionRewards_thenReturnZero() {
        // GIVEN
        attractionUUID = null;

        // WHEN
        int result = rewardsService.getAttractionRewards(attractionUUID,
                userUUID);

        // THEN
        assertThat(result).isEqualTo(0);
    }

    @Test
    @Tag("GetAttractionRewards")
    @DisplayName("Get Attraction Rewards - ERROR - Null user UUID")
    public void givenNullUserId_whenGetAttractionRewards_thenReturnZero() {
        // GIVEN
        userUUID = null;

        // WHEN
        int result = rewardsService.getAttractionRewards(attractionUUID,
                userUUID);

        // THEN
        assertThat(result).isEqualTo(0);
    }

}
