package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.WebRunner;
import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {WebRunner.class})
class PlaylistServiceTest {

    public static final String TITLE = "Title";
    public static final Integer VALID_RATING = 6;
    public static final String NAME = "Name";
    public static final String COUNTRY = "Country";
    public static final String GENRE = "Genre";
    public static final ArtistDto ARTIST = newArtist();
    private static final @NotEmpty String POSTFIX = "0";
    private static final @Min(4) Integer POSTFIX_INT = 1;
    @Autowired
    private PlaylistService underTest;

    private static ArtistDto newArtist() {
        return new ArtistDto().setName(NAME)
                .setCountry(COUNTRY)
                .setGenre(GENRE);
    }

    @Test
    void testCreate() {
        SongDto created = addSongDto();
        assertNotNull(created.getId());
        ARTIST.setId(created.getArtist().getId());
        Assertions.assertEquals(ARTIST, created.getArtist());
        Assertions.assertEquals(TITLE, created.getTitle());
        Assertions.assertEquals(VALID_RATING, created.getRating());
        underTest.delete(created);
    }

    private SongDto addSongDto() {
        return underTest.add(new SongDto().setArtist(ARTIST)
                        .setTitle(TITLE)
                        .setRating(VALID_RATING)
                )
                .orElseThrow(() -> new RuntimeException("Can't add song to playlist"));
    }

    @Test
    void testRead() {
        Optional<SongDto> read = underTest.getById(addSongDto().getId());

        assertTrue(read.isPresent());
        ARTIST.setId(read.get().getArtist().getId());
        Assertions.assertEquals(ARTIST, read.get().getArtist());
        Assertions.assertEquals(TITLE, read.get().getTitle());
        Assertions.assertEquals(VALID_RATING, read.get().getRating());
        underTest.delete(read.get());
    }

    @Test
    void testUpdate() {
        SongDto forUpdate = addSongDto();
        forUpdate.setArtist(forUpdate.getArtist().setName(forUpdate.getArtist().getName() + POSTFIX))
                .setTitle(forUpdate.getTitle() + POSTFIX)
                .setRating(forUpdate.getRating() + POSTFIX_INT);
        underTest.update(forUpdate);
        Optional<SongDto> updated = underTest.getById(forUpdate.getId());

        assertTrue(updated.isPresent());
        ARTIST.setId(forUpdate.getArtist().getId());
        Assertions.assertEquals(ARTIST.setName(ARTIST.getName() + POSTFIX), updated.get().getArtist());
        Assertions.assertEquals(TITLE + POSTFIX, updated.get().getTitle());
        Assertions.assertEquals(VALID_RATING + POSTFIX_INT, updated.get().getRating());
    }

    @Test
    void testDelete() {
        SongDto forDelete = addSongDto();
        underTest.delete(forDelete);
        final Long id = forDelete.getId();
        Assertions.assertThrows(EmptyResultDataAccessException.class
                , () -> underTest.getById(id));
    }

}