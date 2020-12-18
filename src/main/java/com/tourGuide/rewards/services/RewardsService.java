package com.tourGuide.rewards.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourGuide.rewards.domain.UserReward;
import com.tourGuide.rewards.domain.VisitedLocation;
import com.tourGuide.rewards.domain.dto.AttractionDto;
import com.tourGuide.rewards.domain.dto.UserRewardsDto;
import com.tourGuide.rewards.proxies.MicroserviceGpsProxy;
import com.tourGuide.rewards.util.DistanceCalculator;

import rewardCentral.RewardCentral;

@Service
public class RewardsService implements IRewardsService {

    @Autowired
    private RewardCentral rewardCentral;

    @Autowired
    private MicroserviceGpsProxy microserviceGpsProxy;

    @Autowired
    private DistanceCalculator distanceCalculator;

    /**
     * {@inheritDoc}
     */
    public int getAttractionRewards(final UUID attractionId,
            final UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

    /**
     * {@inheritDoc}
     */
    public List<UserReward> calculateRewards(final UserRewardsDto user) {
        CopyOnWriteArrayList<AttractionDto> allAttractionsDtoCopy = new CopyOnWriteArrayList<>();
        allAttractionsDtoCopy.addAll(microserviceGpsProxy.getAllAttractions());

        CopyOnWriteArrayList<VisitedLocation> allUserLocations = new CopyOnWriteArrayList<>();
        allUserLocations.addAll(user.getVisitedLocations());

        allUserLocations.stream()
                .forEach(visitedLocation -> allAttractionsDtoCopy.stream()
                        .filter(attraction -> distanceCalculator
                                .isNearAttraction(visitedLocation, attraction))
                        .forEach(attraction -> {
                            if (user.getUserRewards().stream()
                                    .filter(r -> r.attraction
                                            .getAttractionName()
                                            .equals(attraction
                                                    .getAttractionName()))
                                    .count() == 0) {
                                user.addUserReward(new UserReward(
                                        visitedLocation, attraction,
                                        getAttractionRewards(
                                                attraction.getAttractionId(),
                                                user.getUserId())));
                            }
                        }));
        return user.getUserRewards();
    }

}
