package com.tourGuide.rewards.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rewardCentral.RewardCentral;

@Service
public class RewardsService implements IRewardsService {

    @Autowired
    private RewardCentral rewardCentral;

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

//    public List<UserReward> getUserRewards(User user) {
//        return user.getUserRewards();
//    }
}
