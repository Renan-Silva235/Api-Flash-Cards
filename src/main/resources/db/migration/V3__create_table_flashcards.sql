CREATE TABLE flashcards (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            word VARCHAR(255) NOT NULL,
                            translation VARCHAR(255) NOT NULL,
                            past TEXT,
                            present TEXT,
                            future TEXT,
                            example_phrase_1 TEXT,
                            example_phrase_2 TEXT,
                            example_phrase_3 TEXT,
                            difficulty VARCHAR(50) NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            favorite BOOLEAN DEFAULT FALSE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            deck_id UUID NOT NULL,
                            CONSTRAINT fk_flashcards_deck FOREIGN KEY (deck_id) REFERENCES decks(id) ON DELETE CASCADE
);