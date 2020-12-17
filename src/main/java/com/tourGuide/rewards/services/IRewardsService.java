package com.tourGuide.rewards.services;

import java.util.List;
import java.util.UUID;

import com.tourGuide.rewards.domain.UserReward;
import com.tourGuide.rewards.domain.dto.UserRewardsDto;

public interface IRewardsService {

    int getAttractionRewards(final UUID attractionId, final UUID userId);

    List<UserReward> calculateRewards(final UserRewardsDto user);

}
