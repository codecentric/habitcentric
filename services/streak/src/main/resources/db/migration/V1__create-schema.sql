CREATE TABLE streaks
(
    id            UUID   NOT NULL PRIMARY KEY,
    track_entries DATE[] NOT NULL
);

CREATE TABLE habits
(
    id          UUID        NOT NULL PRIMARY KEY,
    frequency   VARCHAR(12) NOT NULL,
    repetitions INT         NOT NULL,
    streak      UUID        NOT NULL
);
