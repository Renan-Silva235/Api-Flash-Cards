package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.DeckRequestDTO;
import com.flashcards.api.dtos.response.DeckResponseDTO;
import com.flashcards.api.entities.Deck;
import com.flashcards.api.services.DeckService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/decks")
public class DeckController {

    @Autowired
    private DeckService deckService;

    @PostMapping
    public ResponseEntity<DeckResponseDTO> create(@RequestBody @Valid DeckRequestDTO dto) {
        Deck newDeck = deckService.createDeck(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DeckResponseDTO(newDeck));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeckResponseDTO>> listByUser(@PathVariable UUID userId) {
        List<Deck> decks = deckService.listDecksByUser(userId);

        List<DeckResponseDTO> response = decks.stream()
                .map(DeckResponseDTO::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckResponseDTO> getDeckById(@PathVariable UUID id) {
        Deck deck = deckService.findDeckById(id);
        return ResponseEntity.ok(new DeckResponseDTO(deck));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deckService.deleteDeck(id);
        return ResponseEntity.noContent().build();
    }
}