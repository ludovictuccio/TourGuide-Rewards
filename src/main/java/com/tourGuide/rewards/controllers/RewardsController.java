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

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rewards")
public class RewardsController {

    @Autowired
    public IRewardsService rewardsService;

    @ApiOperation(value = "GET Attraction rewards", notes = "Need pathvariable attractionId & pathvariable userId - Return 200 OK or 404 Not Found -  Method used to return the integer of attraction rewards for an user.", response = Integer.class)
    @GetMapping("/getAttractionRewards/{attractionId}/{userId}")
    public int getAttractionRewards(
            @PathVariable("attractionId") final UUID attractionId,
            @PathVariable("userId") final UUID userId) {
        return rewardsService.getAttractionRewards(attractionId, userId);
    }

    @ApiOperation(value = "POST Calculate rewards", notes = "Need body UserRewardsDto - Return 200 OK - Method used to update user's rewards points, adding rewards if attraction was visited.", response = UserReward.class)
    @PostMapping("/calculateRewards")
    public List<UserReward> calculateRewards(
            @RequestBody final UserRewardsDto user) {
        return rewardsService.calculateRewards(user);
    }

}
