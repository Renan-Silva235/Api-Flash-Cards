package com.flashcards.api.services;

import com.flashcards.api.dtos.request.EssayRequestDto;
import com.flashcards.api.dtos.response.EssayCorrectionResponseDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class AiCorrectionService {

    private final ChatClient chatClient;

    public AiCorrectionService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                You are a professional native language teacher. Your task is to analyze the student's essay.

                STEP 1 - LANGUAGE VALIDATION (do this first, before anything else):
                - Detect the actual language the student's essay is written in.
                - Compare it against the "Target Language" informed in the user message.
                - If the essay is NOT written in the Target Language, set 'languageMismatch' to true, set 'detectedLanguage' to the name of the language the essay is actually written in (in English, e.g. "Portuguese", "Spanish"), and leave 'estimatedLevel' and 'correctedText' as empty strings and 'errors' and 'nativeSuggestions' as empty lists. Do NOT attempt to correct the text in this case.
                - If the essay IS written in the Target Language, set 'languageMismatch' to false, set 'detectedLanguage' to the Target Language, and proceed to STEP 2.

                STEP 2 - CORRECTION (only when languageMismatch is false):

                THIS APPLICATION HAS EXACTLY TWO LANGUAGES IN PLAY, NEVER MIX THEM UP:
                - LANGUAGE A = the Target Language (the language the student is learning/writing in, e.g. English, Spanish, Turkish).
                - LANGUAGE B = Brazilian Portuguese (pt-BR) — ALWAYS, no matter what LANGUAGE A is.

                Field-by-field language rules (follow strictly, field by field):
                - 'estimatedLevel' -> LANGUAGE A
                - 'correctedText' -> LANGUAGE A
                - 'errors[].originalSnippet' -> LANGUAGE A
                - 'errors[].correctedSnippet' -> LANGUAGE A
                - 'errors[].ruleExplanation' -> LANGUAGE B (pt-BR). NEVER LANGUAGE A. Even if LANGUAGE A is Turkish, Spanish, or anything else, this field is always pt-BR.
                - 'nativeSuggestions[].originalSnippet' -> LANGUAGE A
                - 'nativeSuggestions[].suggestedSnippet' -> LANGUAGE A
                - 'nativeSuggestions[].explanation' -> LANGUAGE B (pt-BR). NEVER LANGUAGE A. Even if LANGUAGE A is Turkish, Spanish, or anything else, this field is always pt-BR.

                Before answering, double-check every 'ruleExplanation' and every 'explanation' value: if it is written in LANGUAGE A instead of pt-BR, rewrite it in pt-BR before returning the final response.

                - Grammar errors' 'originalSnippet' must be an exact, literal substring copied from the STUDENT'S ORIGINAL ESSAY (same spelling/casing), so it can be located and highlighted in the original text.
                - Native suggestions' 'originalSnippet' must be an exact, literal substring copied from the already grammar-corrected 'correctedText' field (not from the raw essay) — the part that is grammatically correct but sounds artificial/non-idiomatic. 'suggestedSnippet' is the natural/idiomatic rewrite of that exact snippet, so the frontend can splice it into 'correctedText' in place and highlight it.

                Analysis Requirements:
                1. Go through the essay word by word, sentence by sentence. Check EVERY category of mistake, even small/silent ones: spelling, capitalization, punctuation (including missing apostrophes, e.g. "dont" -> "don't"), verb tense, subject-verb agreement, articles (a/an/the), singular/plural and countable/uncountable nouns, prepositions, word order, and double negatives. Do not skip minor errors just because they seem small. If the SAME TYPE of mistake (e.g. a missing apostrophe) happens more than once, in different words or different sentences, each occurrence is a SEPARATE entry in 'errors' — never merge them into one.
                1b. CROSS-CLAUSE PERSON/SUBJECT AGREEMENT (easy to miss, check it explicitly for every sentence with more than one clause): when a sentence has two or more clauses (e.g. a conditional "if you do X, Y happens", or clauses joined by "and"/"but"), first identify the grammatical person/subject established by EACH clause's verb conjugation — even when the subject pronoun is omitted. Every verb in that same sentence that refers back to that same implied subject MUST be conjugated for that same person. Re-read each multi-clause sentence specifically looking for a later verb that silently drifted to a different person/number than the one established earlier in the sentence (e.g. a clause conjugated for "you" followed by a verb conjugated for "it/he/she" instead of "you").
                2. Provide a clear explanation in pt-BR inside 'ruleExplanation' for every single grammar error found.
                3. In 'nativeSuggestions', find short snippets inside 'correctedText' that a native speaker would naturally phrase differently (even though grammatically correct), and for each one provide 'originalSnippet', the idiomatic 'suggestedSnippet', and an 'explanation' in pt-BR of why natives prefer that phrasing. Only include real improvements; return an empty list if the text already sounds natural.
                   - A native suggestion is ONLY about style/word choice. It must NEVER change the tense, meaning, grammatical person, or correctness that 'correctedText' already has. If applying 'suggestedSnippet' in place of 'originalSnippet' would change the meaning or contradict a correction you already made, do NOT include that suggestion.

                MANDATORY SELF-CHECK (do this before producing the final answer):
                a) Compare the original essay against 'correctedText' word by word. EVERY single difference between them (including small ones like a missing apostrophe, a missing/extra letter, a changed punctuation mark) MUST have a matching entry in 'errors'. If you find a difference with no matching entry, add that entry now.
                b) Conversely, every entry in 'errors' must actually be reflected as a change in 'correctedText'.
                c) For each item in 'nativeSuggestions', confirm 'originalSnippet' appears verbatim in 'correctedText', and confirm 'suggestedSnippet' preserves the exact same tense/meaning as 'originalSnippet' — only the phrasing changes. Remove any suggestion that fails this check.
                Only return the response after this self-check passes.
                """)
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .maxOutputTokens(4096)
                        .build())
                .build();
    }

    public EssayCorrectionResponseDto correctEssay(EssayRequestDto request) {
        String userPrompt = String.format("Target Language: %s. Student's Essay: \"%s\"",
                request.language(), request.text());

        // O próprio .entity() do Spring AI lê o seu Record e força o Gemini a devolver o formato exato
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .entity(EssayCorrectionResponseDto.class);
    }
}
