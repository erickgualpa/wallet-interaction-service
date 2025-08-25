CREATE TABLE transfer (
                  id CHAR(36) PRIMARY KEY,
                  created_at TIMESTAMP NOT NULL,
                  source_account_id CHAR(36) NOT NULL,
                  destination_account_id CHAR(36) NOT NULL,
                  amount DOUBLE NOT NULL,
                  FOREIGN KEY (source_account_id) REFERENCES account(entity_id),
                  FOREIGN KEY (destination_account_id) REFERENCES account(entity_id)
                );