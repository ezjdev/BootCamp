package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.WebRunner;
import com.colvir.bootcamp.homework5.dto.SongDto;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {WebRunner.class})
class PlaylistServiceTest {

    public static final String TITLE = "Title";
    public static final String ARTIST = "Artist";
    public static final String VALID_RATING = "6";
    private static final @NotEmpty String POSTFIX = "0";

    @Autowired
    private PlaylistService underTest;

    @Test
    void testCreate() {
        SongDto created = addSongDto();
        assertNotNull(created.getId());
        Assertions.assertEquals(ARTIST, created.getArtist());
        Assertions.assertEquals(TITLE, created.getTitle());
        Assertions.assertEquals(VALID_RATING, created.getRating());
    }

    private SongDto addSongDto() {
        return underTest.add(new SongDto().setArtist(ARTIST)
                        .setTitle(TITLE)
                        .setRating(VALID_RATING))
                .orElseThrow(() -> new RuntimeException("Can't add song to playlist"));
    }

    @Test
    void testRead() {
        Optional<SongDto> read = underTest.getById(addSongDto().getId());

        assertTrue(read.isPresent());
        Assertions.assertEquals(ARTIST, read.get().getArtist());
        Assertions.assertEquals(TITLE, read.get().getTitle());
        Assertions.assertEquals(VALID_RATING, read.get().getRating());
    }

    @Test
    void testUpdate() {
        SongDto forUpdate = addSongDto();
        forUpdate.setArtist(forUpdate.getArtist() + POSTFIX)
                .setTitle(forUpdate.getTitle() + POSTFIX)
                .setRating(addSongDto().getRating() + POSTFIX);

        underTest.update(forUpdate);
        Optional<SongDto> updated = underTest.getById(forUpdate.getId());

        assertTrue(updated.isPresent());
        Assertions.assertEquals(ARTIST + POSTFIX, updated.get().getArtist());
        Assertions.assertEquals(TITLE + POSTFIX, updated.get().getTitle());
        Assertions.assertEquals(VALID_RATING + POSTFIX, updated.get().getRating());
    }

    @Test
    void testDelete() {
        SongDto forUpdate = addSongDto();
        underTest.delete(forUpdate);
        Assertions.assertFalse(underTest.getById(forUpdate.getId()).isPresent());
    }

}