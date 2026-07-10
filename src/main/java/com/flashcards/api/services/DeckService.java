package com.flashcards.api.services;

import com.flashcards.api.dtos.request.DeckRequestDTO;
import com.flashcards.api.entities.Deck;
import com.flashcards.api.entities.User;
import com.flashcards.api.repositories.DeckRepository;
import com.flashcards.api.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Transactional
    public Deck createDeck(DeckRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Deck deck = new Deck();
        deck.setName(dto.name());
        deck.setLanguage(dto.language());
        deck.setCategory(dto.category());
        deck.setUser(user);

        return deckRepository.save(deck);
    }

    @Transactional(readOnly = true)
    public List<Deck> listDecksByUser(UUID userId) {
        return deckRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Deck findDeckById(UUID id) {
        return deckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck não encontrado."));
    }

    @Transactional
    public void deleteDeck(UUID id) {
        if (!deckRepository.existsById(id)) {
            throw new RuntimeException("Não é possível deletar. Deck não encontrado.");
        }
        deckRepository.deleteById(id);
    }

    @Transactional
    public Deck toggleFavorite(UUID id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck não encontrado."));

        deck.setFavorite(!deck.getFavorite());

        return deckRepository.save(deck);
    }
}
