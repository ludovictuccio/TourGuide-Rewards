package com.tourGuide.rewards.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tourGuide.rewards.domain.UserReward;
import com.tourGuide.rewards.domain.VisitedLocation;
import com.tourGuide.rewards.domain.dto.UserRewardsDto;
import com.tourGuide.rewards.proxies.MicroserviceGpsProxy;
import com.tourGuide.rewards.proxies.MicroserviceUserProxy;
import com.tourGuide.rewards.services.IRewardsService;

import rewardCentral.RewardCentral;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RewardsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    public IRewardsService rewardsService;

    @MockBean
    private RewardCentral rewardCentral;

    @MockBean
    private MicroserviceUserProxy microserviceUserProxy;

    @MockBean
    private MicroserviceGpsProxy microserviceGpsProxy;

    private List<VisitedLocation> visitedLocationsList;
    private List<UserReward> userRewardsList;

    private static final String URI_GET_VALID_ATTRACTION_REWARDS = "/rewards/getAttractionRewards/3fe2b1fa-28f3-11eb-adc1-0242ac120002/45f41782-28f3-11eb-adc1-0242ac120002";
    private static final String URI_GET_INVALID_ATTRACTION_REWARDS_NO_ATTRACTION_ID = "/rewards/getAttractionRewards//45f41782-28f3-11eb-adc1-0242ac120002";
    private static final String URI_GET_INVALID_ATTRACTION_REWARDS_NO_USER_ID = "/rewards/getAttractionRewards/3fe2b1fa-28f3-11eb-adc1-0242ac120002/";
    private static final String URI_GET_CALCULATE_REWARDS = "/rewards/calculateRewards/internalUser1";

    @BeforeEach
    public void setUpPerTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        visitedLocationsList = new ArrayList<>();
        visitedLocationsList.clear();
        userRewardsList = new ArrayList<>();
        userRewardsList.clear();
    }

    @Test
    @Tag("CalculateRewards")
    @DisplayName("Calculate Rewards - OK")
    public void givenValidUsername_whenCalculateRewards_thenReturnOk()
            throws Exception {
        List<VisitedLocation> visitedLocationsList = new ArrayList<>();
        List<UserReward> userRewardsList = new ArrayList<>();

        when(microserviceUserProxy.getUserRewardsDto("internalUser1"))
                .thenReturn(
                        new UserRewardsDto(UUID.randomUUID(), "internalUser1",
                                visitedLocationsList, userRewardsList));
        this.mockMvc
                .perform(get(URI_GET_CALCULATE_REWARDS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("GetAttractionRewards")
    @DisplayName("Get Attraction Rewards - OK")
    public void givenValidAttractionAndUserId_whenGetAttractionRewards_thenReturnOk()
            throws Exception {
        this.mockMvc
                .perform(get(URI_GET_VALID_ATTRACTION_REWARDS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("GetAttractionRewards")
    @DisplayName("Get Attraction Rewards - ERROR - User UUID null")
    public void givenNullUserId_whenGetAttractionRewards_thenReturnNotFound()
            throws Exception {
        this.mockMvc
                .perform(get(URI_GET_INVALID_ATTRACTION_REWARDS_NO_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @Tag("GetAttractionRewards")
    @DisplayName("Get Attraction Rewards - ERROR - Attraction UUID null")
    public void givenNullAttractionId_whenGetAttractionRewards_thenReturnNotFound()
            throws Exception {
        this.mockMvc
                .perform(
                        get(URI_GET_INVALID_ATTRACTION_REWARDS_NO_ATTRACTION_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound()).andReturn();
    }

}
