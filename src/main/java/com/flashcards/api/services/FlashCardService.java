package com.flashcards.api.services;

import com.flashcards.api.security.CurrentUserService;
import com.flashcards.api.exceptions.ResourceNotFoundException;
import com.flashcards.api.dtos.request.CreateFlashCardRequest;
import com.flashcards.api.entities.Deck;
import com.flashcards.api.entities.FlashCard;
import com.flashcards.api.enums.CardStatus;
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
    private DeckService deckService;

    @Autowired
    private CurrentUserService currentUserService;

    /**
     * Busca um card de um deck do usuário logado. Card inexistente ou de outro
     * usuário respondem igual ("não encontrado"), para não revelar que ele existe.
     */
    @Transactional(readOnly = true)
    public FlashCard findOwnedCard(UUID id) {
        return flashCardRepository.findByIdAndDeckUserId(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard não encontrado."));
    }

    @Transactional
    public FlashCard create(CreateFlashCardRequest dto) {
        // Só deixa criar card em deck do próprio usuário
        Deck deck = deckService.findOwnedDeck(dto.deckId());

        FlashCard card = new FlashCard();
        updateCardFields(card, dto);
        card.setDeck(deck);

        return flashCardRepository.save(card);
    }

    @Transactional
    public FlashCard update(UUID id, CreateFlashCardRequest dto) {
        FlashCard card = findOwnedCard(id);

        updateCardFields(card, dto);
        return flashCardRepository.save(card);
    }

    @Transactional(readOnly = true)
    public List<FlashCard> listByDeck(UUID deckId) {
        deckService.findOwnedDeck(deckId);

        return flashCardRepository.findByDeckIdAndStatusIn(
                deckId,
                List.of(
                        CardStatus.LEARNING,
                        CardStatus.REVIEWED
                )
        );
    }

    @Transactional(readOnly = true)
    public List<FlashCard> search(String term) {
        return flashCardRepository.searchCardsByUser(term, currentUserService.getCurrentUserId());
    }

    @Transactional
    public void delete(UUID id) {
        FlashCard card = findOwnedCard(id);
        flashCardRepository.delete(card);
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
