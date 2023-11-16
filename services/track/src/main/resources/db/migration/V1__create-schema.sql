CREATE TABLE habit_tracking (user_id VARCHAR(64) NOT NULL, habit_id BIGINT NOT NULL);
ALTER TABLE habit_tracking ADD PRIMARY KEY (user_id, habit_id);

CREATE TABLE tracked_dates(tracking_date DATE NOT NULL, user_id VARCHAR(64) NOT NULL, habit_id BIGINT NOT NULL);
ALTER TABLE tracked_dates ADD FOREIGN KEY (user_id, habit_id) REFERENCES habit_tracking(user_id, habit_id);
