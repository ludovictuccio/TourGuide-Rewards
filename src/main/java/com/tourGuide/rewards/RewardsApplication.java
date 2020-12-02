package com.tourGuide.rewards;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import rewardCentral.RewardCentral;

@EnableFeignClients
@SpringBootApplication
public class RewardsApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        SpringApplication.run(RewardsApplication.class, args);
    }

    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }

}
