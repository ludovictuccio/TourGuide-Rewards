package com.tourGuide.rewards.services;

import java.util.List;
import java.util.UUID;

import com.tourGuide.rewards.domain.UserReward;
import com.tourGuide.rewards.domain.dto.UserRewardsDto;

public interface IRewardsService {

    /**
     * Method used to return the integer of attraction rewards for an user.
     *
     * @param UUID attractionId
     * @param UUID userId
     * @return int attraction rewards
     */
    int getAttractionRewards(final UUID attractionId, final UUID userId);

    /**
     * Method used to update user's rewards points, adding rewards if attraction
     * was visited.
     *
     * @param UserRewardsDto user
     * @return user Rewards list
     */
    List<UserReward> calculateRewards(final UserRewardsDto user);

}
