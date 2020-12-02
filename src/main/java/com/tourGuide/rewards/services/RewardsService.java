package com.tourGuide.rewards.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourGuide.rewards.domain.UserReward;
import com.tourGuide.rewards.domain.VisitedLocation;
import com.tourGuide.rewards.domain.dto.AttractionDto;
import com.tourGuide.rewards.domain.dto.UserRewardsDto;
import com.tourGuide.rewards.proxies.MicroserviceGpsProxy;
import com.tourGuide.rewards.proxies.MicroserviceUserProxy;
import com.tourGuide.rewards.util.DistanceCalculator;

import rewardCentral.RewardCentral;

@Service
public class RewardsService implements IRewardsService {

    @Autowired
    private RewardCentral rewardCentral;

    @Autowired
    private MicroserviceGpsProxy microserviceGpsProxy;

    @Autowired
    private MicroserviceUserProxy microserviceUserProxy;

    @Autowired
    private DistanceCalculator distanceCalculator;

    /**
     * Method used to return the integer of attraction rewards for an user.
     *
     * @param UUID attractionId
     * @param UUID userId
     * @return int attraction rewards
     */
    public int getAttractionRewards(final UUID attractionId,
            final UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

    /**
     * Method used to update user's rewards points, adding rewards if attraction
     * was visited.
     *
     * @param userName
     */
    public UserRewardsDto calculateRewards(final String userName) {

        UserRewardsDto user = microserviceUserProxy.getUserRewardsDto(userName);

        for (VisitedLocation visitedLocation : user.getVisitedLocations()) {

            for (AttractionDto attractionDto : microserviceGpsProxy
                    .getAllAttractions()) {

                if (user.getUserRewards().stream()
                        .filter(r -> r.attraction.getAttractionName()
                                .equals(attractionDto.getAttractionName()))
                        .count() == 0) {

                    if (distanceCalculator.isNearAttraction(visitedLocation,
                            attractionDto)) {
                        user.addUserReward(
                                new UserReward(visitedLocation, attractionDto,
                                        getAttractionRewards(
                                                attractionDto.getAttractionId(),
                                                user.getUserId())));
                    }
                }
            }
        }
        return user;
    }

}
