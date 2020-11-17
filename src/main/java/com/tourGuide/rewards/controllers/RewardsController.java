package com.tourGuide.rewards.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
