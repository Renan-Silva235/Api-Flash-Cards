package com.flashcards.api.controllers;

import com.flashcards.api.dtos.response.StatisticsResponseDTO;
import com.flashcards.api.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public StatisticsResponseDTO getStatistics(
            @RequestParam(required = false) String language
    ) {
        return statisticsService.getStatistics(language);
    }
}