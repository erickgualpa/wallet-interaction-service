CREATE TABLE deposit (
                entity_id CHAR(36) PRIMARY KEY,
                created_at TIMESTAMP NOT NULL,
                account_entity_id CHAR(36) NOT NULL,
                amount DOUBLE NOT NULL,
                UNIQUE KEY uk_deposit_entity_id(entity_id)
              );