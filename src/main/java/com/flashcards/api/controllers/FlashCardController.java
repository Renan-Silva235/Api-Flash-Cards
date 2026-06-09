package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.CreateFlashCardRequest;
import com.flashcards.api.dtos.response.FlashCardResponse;
import com.flashcards.api.entities.FlashCard;
import com.flashcards.api.services.FlashCardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flashcards")
public class FlashCardController {

    @Autowired
    private FlashCardService flashCardService;

    @PostMapping
    public ResponseEntity<FlashCardResponse> create(@RequestBody @Valid CreateFlashCardRequest dto) {
        FlashCard card = flashCardService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FlashCardResponse(card));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashCardResponse> update(@PathVariable UUID id, @RequestBody @Valid CreateFlashCardRequest dto) {
        FlashCard updated = flashCardService.update(id, dto);
        return ResponseEntity.ok(new FlashCardResponse(updated));
    }

    @GetMapping("/deck/{deckId}")
    public ResponseEntity<List<FlashCardResponse>> listByDeck(@PathVariable UUID deckId) {
        List<FlashCardResponse> response = flashCardService.listByDeck(deckId).stream()
                .map(FlashCardResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlashCardResponse>> search(@RequestParam String q) {
        List<FlashCardResponse> response = flashCardService.search(q).stream()
                .map(FlashCardResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        flashCardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
