-- Dados iniciais para o sistema de biblioteca

-- Inserir usuários (as senhas são: 123456)
INSERT INTO users (name, email, password, role, is_active, created_at, updated_at) 
VALUES 
  ('Admin Sistema', 'admin@biblioteca.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('João Autor', 'autor@biblioteca.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'AUTHOR', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Maria Leitora', 'leitor@biblioteca.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'READER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserir alguns livros
INSERT INTO books (title, author, isbn, genre, description, available_copies, total_copies, created_by, created_at, updated_at)
VALUES 
  ('Dom Casmurro', 'Machado de Assis', '978-85-359-0277-5', 'Romance', 'Clássico da literatura brasileira que narra a história de Bentinho e Capitu.', 3, 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('O Cortiço', 'Aluísio Azevedo', '978-85-359-0123-4', 'Romance', 'Romance naturalista que retrata a vida em um cortiço no Rio de Janeiro.', 2, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Iracema', 'José de Alencar', '978-85-359-0456-7', 'Romance', 'Romance indianista que conta a lenda da fundação do Ceará.', 4, 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Capitães da Areia', 'Jorge Amado', '978-85-359-0789-0', 'Ficção', 'História dos meninos de rua em Salvador, Bahia.', 2, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('O Quinze', 'Rachel de Queiroz', '978-85-359-0321-9', 'Romance', 'Romance sobre a seca no Nordeste brasileiro.', 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
