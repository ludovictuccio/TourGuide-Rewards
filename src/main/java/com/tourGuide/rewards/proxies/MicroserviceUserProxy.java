package com.tourGuide.rewards.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tourGuide.rewards.domain.dto.UserRewardsDto;

@FeignClient(value = "microservice-users", url = "localhost:9001/user")
public interface MicroserviceUserProxy {

    @GetMapping("/getUserRewardsDto/{userName}")
    public UserRewardsDto getUserRewardsDto(
            @PathVariable("userName") String userName);

}
