package com.colvir.bootcamp.homework5.dao;

import com.colvir.bootcamp.homework5.model.Artist;
import com.colvir.bootcamp.homework5.model.Song;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlaylistDao {
    public static final String ARTIST_ID = "artist_id";
    public static final String NAME = "name";
    public static final String GENRE = "genre";
    public static final String COUNTRY = "country";
    public static final String TITLE = "title";
    public static final String DURATION = "duration";
    public static final String RATING = "rating";
    public static final String ID = "id";

    public static final RowMapper<Song> SONG_ROW_MAPPER =
            (rs, rowNum) -> new Song(
                    rs.getLong(ID)
                    , new Artist(
                        rs.getLong(ARTIST_ID)
                        , rs.getString(NAME)
                        , rs.getString(GENRE)
                        , rs.getString(COUNTRY))
                    , rs.getString(TITLE)
                    , rs.getTime(DURATION)
                    , rs.getInt(RATING));

    private final NamedParameterJdbcTemplate template;

    public Song insertOrUpdate(Song song) {
        return Optional.ofNullable(song)
                .map(it -> Optional.of(it)
                        .filter(s -> Objects.nonNull(s.getId()))
                        .map(this::update)
                        .orElseGet(() -> this.insert(it)))
                .orElseThrow(() -> new IllegalArgumentException("Can't insert empty song"));
    }

    private Song update(Song song) {
        Artist artist = insertOrUpdate(song.getArtist());
        template.update(
                """
                        UPDATE playlist.song
                        SET artist_id = :artist_id
                          , title = :title
                          , duration = :duration
                          , rating = :rating
                        WHERE id = :id
                        """
                , new MapSqlParameterSource()
                        .addValue(ID, song.getId())
                        .addValue(ARTIST_ID, artist.getId())
                        .addValue(TITLE, song.getTitle())
                        .addValue(DURATION, song.getDuration())
                        .addValue(RATING, song.getRating())
        );
        return song;
    }

    private Artist insertOrUpdate(Artist artist) {
        return Optional.ofNullable(artist)
                .map(it -> Optional.of(it)
                        .filter(s -> Objects.nonNull(s.getId()))
                        .map(this::update)
                        .orElseGet(() -> this.insert(it)))
                .orElseThrow(() -> new IllegalArgumentException("Can't insert empty artist"));
    }

    private Song insert(Song song) {
        Artist artist = insertOrUpdate(song.getArtist());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(
                """
                        INSERT INTO playlist.song (artist_id, title, duration, rating)
                        VALUES (:artist_id, :title, :duration, :rating)
                        """
                , new MapSqlParameterSource()
                        .addValue(ARTIST_ID, artist.getId())
                        .addValue(TITLE, song.getTitle())
                        .addValue(DURATION, song.getDuration())
                        .addValue(RATING, song.getRating())
                , keyHolder
                , new String[]{"id"}
        );
        return Optional.of(keyHolder)
                .filter(it -> Objects.nonNull(it.getKey()))
                .map(it -> song.setId(it.getKey().longValue()))
                .orElseThrow(() -> new IllegalArgumentException("Can't insert song"));
    }

    private Artist update(Artist artist) {
        template.update(
                """
                        UPDATE playlist.artist
                        SET name = :name
                        , genre = :genre
                        , country = :country
                        WHERE id = :id
                        """
                , new MapSqlParameterSource()
                        .addValue(ID, artist.getId())
                        .addValue(NAME, artist.getName())
                        .addValue(GENRE, artist.getGenre())
                        .addValue(COUNTRY, artist.getCountry())
        );
        return artist;
    }

    private Artist insert(Artist artist) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(
                """ 
                        INSERT INTO playlist.artist (name, genre, country)
                        VALUES (:name, :genre, :country)
                        """
                , new MapSqlParameterSource()
                        .addValue(NAME, artist.getName())
                        .addValue(GENRE, artist.getGenre())
                        .addValue(COUNTRY, artist.getCountry())
                , keyHolder
                , new String[]{"id"}
        );
        return Optional.of(keyHolder)
                .filter(it -> Objects.nonNull(it.getKey()))
                .map(it -> artist.setId(it.getKey().longValue()))
                .orElseThrow(() -> new IllegalArgumentException("Can't insert artist"));
    }

    public List<Song> getPlaylist() {
        return template.query(
                """
                            SELECT s.id, s.artist_id, s.title, s.rating, s.duration
                                , a.name, a.genre, a.country
                            FROM playlist.song s
                            LEFT JOIN playlist.artist a ON s.artist_id = a.id;
                        """
                , SONG_ROW_MAPPER
        );
    }

    public Song getById(Long id) {
        return template.queryForObject(
                """
                            SELECT s.id, s.artist_id, s.title, s.rating, s.duration
                                , a.name, a.genre, a.country
                            FROM playlist.song s
                            LEFT JOIN playlist.artist a ON s.artist_id = a.id
                            WHERE s.id = :id;
                        """
                , Map.of(ID, id)
                , SONG_ROW_MAPPER
        );
    }

    public void delete(Song song) {
        template.update(
                """
                        DELETE FROM playlist.song WHERE id = :id
                        """
                , Map.of(ID, song.getId())
        );
    }

}
