CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  is_deleted BOOLEAN NOT NULL default FALSE,
  UNIQUE(name, password)
);