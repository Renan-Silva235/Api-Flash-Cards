package com.flashcards.api.services;

import com.flashcards.api.dtos.request.DeckRequestDTO;
import com.flashcards.api.entities.Deck;
import com.flashcards.api.entities.User;
import com.flashcards.api.exceptions.ForbiddenException;
import com.flashcards.api.exceptions.ResourceNotFoundException;
import com.flashcards.api.repositories.DeckRepository;
import com.flashcards.api.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Transactional
    public Deck createDeck(DeckRequestDTO dto) {
        // O dono é sempre o usuário do token; o userId do corpo é ignorado
        User user = currentUserService.getCurrentUser();

        Deck deck = new Deck();
        deck.setName(dto.name());
        deck.setLanguage(dto.language());
        deck.setCategory(dto.category());
        deck.setUser(user);

        return deckRepository.save(deck);
    }

    @Transactional(readOnly = true)
    public List<Deck> listDecksByUser(UUID userId) {
        if (!userId.equals(currentUserService.getCurrentUserId())) {
            throw new ForbiddenException("Acesso negado.");
        }
        return deckRepository.findByUserId(userId);
    }

    /**
     * Busca um deck do usuário logado. Se o deck não existir ou for de outro usuário,
     * responde "não encontrado" do mesmo jeito, para não revelar que ele existe.
     */
    @Transactional(readOnly = true)
    public Deck findOwnedDeck(UUID id) {
        return deckRepository.findByIdAndUserId(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Deck não encontrado."));
    }

    @Transactional(readOnly = true)
    public Deck findDeckById(UUID id) {
        return findOwnedDeck(id);
    }

    @Transactional
    public void deleteDeck(UUID id) {
        Deck deck = findOwnedDeck(id);
        deckRepository.delete(deck);
    }

    @Transactional
    public Deck toggleFavorite(UUID id) {
        Deck deck = findOwnedDeck(id);

        deck.setFavorite(!deck.getFavorite());

        return deckRepository.save(deck);
    }
}
