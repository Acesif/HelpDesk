/* Seeding user database */

-- Inserting data into the user table
INSERT IGNORE INTO user (name, email, phone_number, office_id, designation, created_at, updated_at) VALUES ('Admin', 'admin@admin.com', '01234567891', '101', 'GRO', NOW(), NOW())

