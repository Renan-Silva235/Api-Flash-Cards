-- Troca o histórico "uma linha por revisão" por contadores que são atualizados,
-- para o banco não crescer a cada estudo.

-- 1) Contadores de histórico no próprio flashcard, com os nomes dos botões da tela:
--    Fácil (HIT) -> easy, Médio (DIFFICULT) -> medium, Difícil (MISTAKE) -> hard
ALTER TABLE flashcards
    ADD COLUMN review_count     INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN easy_count       INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN medium_count     INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN hard_count       INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN last_reviewed_at TIMESTAMP;

-- 2) Aproveita o histórico já gravado (precisa vir antes de apagar sessões,
--    pois study_records tem ON DELETE CASCADE na sessão)
UPDATE flashcards f SET
    review_count     = r.total,
    easy_count       = r.easy,
    medium_count     = r.medium,
    hard_count       = r.hard,
    last_reviewed_at = r.last_at
FROM (
    SELECT flashcard_id,
           COUNT(*)                                     AS total,
           COUNT(*) FILTER (WHERE result = 'HIT')       AS easy,
           COUNT(*) FILTER (WHERE result = 'DIFFICULT') AS medium,
           COUNT(*) FILTER (WHERE result = 'MISTAKE')   AS hard,
           MAX(reviewed_at)                             AS last_at
    FROM study_records
    GROUP BY flashcard_id
) r
WHERE f.id = r.flashcard_id;

-- 3) Uma sessão por usuário + deck: mantém só a mais recente
UPDATE study_sessions SET started_at = COALESCE(started_at, study_date, CURRENT_TIMESTAMP);

DELETE FROM study_sessions s
USING study_sessions s2
WHERE s.user_id = s2.user_id
  AND s.deck_id = s2.deck_id
  AND (s.started_at, s.id) < (s2.started_at, s2.id);

ALTER TABLE study_sessions
    ADD CONSTRAINT uk_sessions_user_deck UNIQUE (user_id, deck_id);

-- 4) Placar da última sessão (colunas que já existiam e não eram usadas),
--    renomeadas para os nomes dos botões da tela
ALTER TABLE study_sessions RENAME COLUMN hits TO easy;
ALTER TABLE study_sessions RENAME COLUMN difficult_cards TO medium;
ALTER TABLE study_sessions RENAME COLUMN mistakes TO hard;

UPDATE study_sessions SET easy = 0, medium = 0, hard = 0;

ALTER TABLE study_sessions
    ALTER COLUMN started_at SET NOT NULL,
    ALTER COLUMN easy SET NOT NULL,
    ALTER COLUMN medium SET NOT NULL,
    ALTER COLUMN hard SET NOT NULL,
    DROP COLUMN study_date;

-- 5) O histórico agora vive nos contadores; libera o espaço ocupado.
--    A tabela em si fica para ser removida numa migration futura.
TRUNCATE study_records;
