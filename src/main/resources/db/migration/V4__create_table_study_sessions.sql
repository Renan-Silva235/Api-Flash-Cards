CREATE TABLE study_sessions (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                study_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                hits INTEGER DEFAULT 0,
                                mistakes INTEGER DEFAULT 0,
                                difficult_cards INTEGER DEFAULT 0,
                                deck_id UUID NOT NULL,
                                user_id UUID NOT NULL,
                                CONSTRAINT fk_sessions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                CONSTRAINT fk_sessions_deck FOREIGN KEY (deck_id) REFERENCES decks(id) ON DELETE CASCADE
);