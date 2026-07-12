CREATE TABLE verification_codes (
                                    id UUID PRIMARY KEY,

                                    email VARCHAR(255) NOT NULL,

                                    code VARCHAR(6) NOT NULL,

                                    type VARCHAR(30) NOT NULL,

                                    expires_at TIMESTAMP NOT NULL,

                                    used BOOLEAN NOT NULL DEFAULT FALSE
);