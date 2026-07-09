package com.flashcards.api.services;

import com.flashcards.api.dtos.response.StatisticsResponseDTO;
import com.flashcards.api.enums.DifficultyLevel;
import com.flashcards.api.repositories.FlashCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flashcards.api.entities.User;
import com.flashcards.api.security.userDetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class StatisticsService {

    @Autowired
    private FlashCardRepository flashCardRepository;

    public StatisticsResponseDTO getStatistics(String language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        long totalCards;
        long easy;
        long medium;
        long hard;

        if (language == null || language.isBlank() || language.equalsIgnoreCase("all")) {

            totalCards = flashCardRepository.countByDeckUserId(user.getId());
            easy = flashCardRepository.countByDeckUserIdAndDifficulty(user.getId(), DifficultyLevel.EASY);
            medium = flashCardRepository.countByDeckUserIdAndDifficulty(user.getId(), DifficultyLevel.MEDIUM);
            hard = flashCardRepository.countByDeckUserIdAndDifficulty(user.getId(), DifficultyLevel.HARD);

        } else {

            totalCards = flashCardRepository.countByDeckUserIdAndDeckLanguage(user.getId(), language);
            easy = flashCardRepository.countByDeckUserIdAndDeckLanguageAndDifficulty(user.getId(), language, DifficultyLevel.EASY);
            medium = flashCardRepository.countByDeckUserIdAndDeckLanguageAndDifficulty(user.getId(), language, DifficultyLevel.MEDIUM);
            hard = flashCardRepository.countByDeckUserIdAndDeckLanguageAndDifficulty(user.getId(), language, DifficultyLevel.HARD);

        }

        return new StatisticsResponseDTO(
                totalCards,
                easy,
                medium,
                hard
        );
    }
}