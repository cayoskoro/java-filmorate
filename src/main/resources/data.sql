INSERT INTO users (id, email, login, name, birthday)
VALUES (1, 'abc@ya.ru', 'yaUser', 'Yandex Userovich', '2010-10-10'),
(2, 'abc@main.ru', 'mailUser', 'Mail Userovich', '2001-01-01'),
(3, 'abc@google.ru', 'googleUser', 'Google Userovich', '2012-12-12')
;
-- ON CONFLICT DO NOTHING;

INSERT INTO genres (id, name)
VALUES (1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

INSERT INTO mpa (id, name)
VALUES (1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');