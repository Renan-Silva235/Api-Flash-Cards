package com.flashcards.api.services;

import com.flashcards.api.dtos.response.ProfileResponseDTO;
import com.flashcards.api.entities.User;
import com.flashcards.api.repositories.DeckRepository;
import com.flashcards.api.repositories.FlashCardRepository;
import com.flashcards.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private FlashCardRepository flashCardRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfile(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        long totalDecks = deckRepository.countByUserId(userId);
        long favoriteDecks = deckRepository.countByUserIdAndFavoriteTrue(userId);
        long totalFlashcards = flashCardRepository.countByDeckUserId(userId);

        return new ProfileResponseDTO(
                user.getName(),
                user.getEmail(),
                totalDecks,
                totalFlashcards,
                favoriteDecks
        );
    }
}