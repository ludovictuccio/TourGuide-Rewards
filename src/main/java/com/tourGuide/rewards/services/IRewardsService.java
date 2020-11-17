package com.tourGuide.rewards.services;

import java.util.UUID;

public interface IRewardsService {

    int getAttractionRewards(final UUID attractionId, final UUID userId);
}
