package com.tourGuide.rewards.proxies;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.tourGuide.rewards.domain.dto.AttractionDto;

@FeignClient(value = "microservice-gps", url = "${proxy.gps}")
public interface MicroserviceGpsProxy {

    @GetMapping("/getAllAttractions")
    public List<AttractionDto> getAllAttractions();

}
