package com.flashcards.api.services;

import com.flashcards.api.dtos.request.CreateFlashCardRequest;
import com.flashcards.api.entities.Deck;
import com.flashcards.api.entities.FlashCard;
import com.flashcards.api.repositories.DeckRepository;
import com.flashcards.api.repositories.FlashCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FlashCardService {

    @Autowired
    private FlashCardRepository flashCardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Transactional
    public FlashCard create(CreateFlashCardRequest dto) {
        Deck deck = deckRepository.findById(dto.deckId())
                .orElseThrow(() -> new RuntimeException("Deck não encontrado."));

        FlashCard card = new FlashCard();
        updateCardFields(card, dto);
        card.setDeck(deck);

        return flashCardRepository.save(card);
    }

    @Transactional
    public FlashCard update(UUID id, CreateFlashCardRequest dto) {
        FlashCard card = flashCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard não encontrado."));

        updateCardFields(card, dto);
        return flashCardRepository.save(card);
    }

    @Transactional(readOnly = true)
    public List<FlashCard> listByDeck(UUID deckId) {
        return flashCardRepository.findByDeckId(deckId);
    }

    @Transactional(readOnly = true)
    public List<FlashCard> search(String term) {
        return flashCardRepository.searchCards(term);
    }

    @Transactional
    public void delete(UUID id) {
        if (!flashCardRepository.existsById(id)) {
            throw new RuntimeException("Flashcard não encontrado.");
        }
        flashCardRepository.deleteById(id);
    }

    private void updateCardFields(FlashCard card, CreateFlashCardRequest dto) {
        card.setWord(dto.word());
        card.setTranslation(dto.translation());
        card.setPast(dto.past());
        card.setPresent(dto.present());
        card.setFuture(dto.future());
        card.setExamplePhrase1(dto.examplePhrase1());
        card.setExamplePhrase2(dto.examplePhrase2());
        card.setExamplePhrase3(dto.examplePhrase3());
        card.setDifficulty(dto.difficulty());
        card.setFavorite(dto.favorite() != null ? dto.favorite() : false);
    }
}
