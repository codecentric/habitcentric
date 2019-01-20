CREATE TABLE habit (id BIGINT PRIMARY KEY, name VARCHAR(64) NOT NULL);
CREATE SEQUENCE habit_seq;
INSERT INTO habit (id, name) VALUES (nextval('habit_seq'), 'Jogging');
INSERT INTO habit (id, name) VALUES (nextval('habit_seq'), 'Play guitar');
INSERT INTO habit (id, name) VALUES (nextval('habit_seq'), 'Meditate');
