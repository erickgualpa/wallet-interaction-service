CREATE TABLE wallet (
                entity_id CHAR(36) PRIMARY KEY,
                created_at TIMESTAMP NOT NULL,
                UNIQUE KEY uk_wallet_entity_id(entity_id)
              );