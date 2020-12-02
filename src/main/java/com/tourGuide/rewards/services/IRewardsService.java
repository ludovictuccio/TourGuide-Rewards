package com.tourGuide.rewards.services;

import java.util.UUID;

import com.tourGuide.rewards.domain.dto.UserRewardsDto;

public interface IRewardsService {

    int getAttractionRewards(final UUID attractionId, final UUID userId);

    UserRewardsDto calculateRewards(final String userName);
}
