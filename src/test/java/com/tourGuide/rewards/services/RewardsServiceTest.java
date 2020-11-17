package com.tourGuide.rewards.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import rewardCentral.RewardCentral;

@SpringBootTest
public class RewardsServiceTest {

    @Autowired
    public IRewardsService rewardsService;

    @MockBean
    private RewardCentral rewardCentral;

    private UUID attractionUUID;
    private UUID userUUID;

    @BeforeEach
    public void setUpPerTest() {
        attractionUUID = UUID.randomUUID();
        userUUID = UUID.randomUUID();
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
