CREATE SCHEMA hc_track;
CREATE TABLE hc_track.habit_tracking (user_id VARCHAR(64) NOT NULL, habit_id BIGINT NOT NULL, track_date DATE NOT NULL);
ALTER TABLE hc_track.habit_tracking ADD PRIMARY KEY (user_id, habit_id, track_date);
