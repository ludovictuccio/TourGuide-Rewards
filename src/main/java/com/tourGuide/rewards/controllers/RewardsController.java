package com.tourGuide.rewards.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourGuide.rewards.domain.UserReward;
import com.tourGuide.rewards.domain.dto.UserRewardsDto;
import com.tourGuide.rewards.services.IRewardsService;

@RestController
@RequestMapping("/rewards")
public class RewardsController {

    @Autowired
    public IRewardsService rewardsService;

    @GetMapping("/getAttractionRewards/{attractionId}/{userId}")
    public int getAttractionRewards(
            @PathVariable("attractionId") final UUID attractionId,
            @PathVariable("userId") final UUID userId) {
        return rewardsService.getAttractionRewards(attractionId, userId);
    }

    @PostMapping("/calculateRewards")
    public List<UserReward> calculateRewards(
            @RequestBody final UserRewardsDto user) {
        return rewardsService.calculateRewards(user);
    }

}
