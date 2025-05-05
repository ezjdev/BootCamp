DROP TABLE IF EXISTS playlist.song;
DROP TABLE IF EXISTS playlist.artist;
DROP SCHEMA IF EXISTS playlist;

CREATE SCHEMA IF NOT EXISTS playlist;

CREATE TABLE IF NOT EXISTS playlist.artist (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    genre VARCHAR(50),
    country VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS playlist.song (
    id SERIAL PRIMARY KEY,
    artist_id INTEGER NOT NULL,
    title VARCHAR(100) NOT NULL,
    duration TIME,
    rating SMALLINT,
    CONSTRAINT fk_artist FOREIGN KEY (artist_id) REFERENCES playlist.artist(id)
);