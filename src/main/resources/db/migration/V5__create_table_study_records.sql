CREATE TABLE study_records (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               result VARCHAR(50) NOT NULL,
                               difficulty_level VARCHAR(50) NOT NULL,
                               reviewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               flashcard_id UUID NOT NULL,
                               study_session_id UUID NOT NULL,
                               CONSTRAINT fk_records_session FOREIGN KEY (study_session_id) REFERENCES study_sessions(id) ON DELETE CASCADE,
                               CONSTRAINT fk_records_flashcard FOREIGN KEY (flashcard_id) REFERENCES flashcards(id) ON DELETE CASCADE
);