package com.flashcards.api.controllers;
import com.flashcards.api.dtos.request.EssayRequestDto;
import com.flashcards.api.dtos.response.EssayCorrectionResponseDto;
import com.flashcards.api.services.AiCorrectionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/essays")
public class EssayCorrectionController {

    private final AiCorrectionService aiCorrectionService;

    public EssayCorrectionController(AiCorrectionService aiCorrectionService) {
        this.aiCorrectionService = aiCorrectionService;
    }

    @PostMapping("/correct")
    public ResponseEntity<EssayCorrectionResponseDto> correctEssay(@RequestBody @Valid EssayRequestDto request) {
        EssayCorrectionResponseDto correction = aiCorrectionService.correctEssay(request);
        return ResponseEntity.ok(correction);
    }
}

