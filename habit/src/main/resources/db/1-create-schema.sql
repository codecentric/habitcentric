CREATE TABLE hc_habit.habit (id BIGINT PRIMARY KEY, name VARCHAR(64) NOT NULL, repetitions INT NOT NULL, frequency VARCHAR(12) NOT NULL);
CREATE SEQUENCE hc_habit.habit_seq MINVALUE 1 INCREMENT BY 50 NO CYCLE;
ALTER TABLE hc_habit.habit ADD CONSTRAINT unique_habit_name UNIQUE (name);
ALTER TABLE hc_habit.habit ADD CONSTRAINT check_habit_frequency CHECK (frequency IN ('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'));
ALTER TABLE hc_habit.habit ADD COLUMN user_id VARCHAR(64);
