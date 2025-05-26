package com.colvir.bootcamp.homework13.service;

import com.colvir.bootcamp.homework13.MongoDBRunner;
import com.colvir.bootcamp.homework13.dto.ArtistDto;
import com.colvir.bootcamp.homework13.dto.SongDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {MongoDBRunner.class})
@Slf4j
class PlaylistServiceTest {

    public static final String TITLE = "Title";
    public static final Integer VALID_RATING = 6;
    public static final String NAME = "Name";
    public static final String COUNTRY = "Country";
    public static final String GENRE = "Genre";
    public static ArtistDto defaultArtist;
    private static final String POSTFIX = "0";
    private static final Integer POSTFIX_INT = 1;
    @Autowired
    private PlaylistService underTest;

    @BeforeEach
    void setUp() {
        defaultArtist = new ArtistDto().setName(NAME)
                .setCountry(COUNTRY)
                .setGenre(GENRE);
    }

    @Test
    @DisplayName("Create song and artist")
    void testCreate() {
        SongDto created = addSongDto();
        assertNotNull(created.getId());
        defaultArtist.setId(created.getArtist().getId());
        Assertions.assertEquals(defaultArtist, created.getArtist());
        Assertions.assertEquals(TITLE, created.getTitle());
        Assertions.assertEquals(VALID_RATING, created.getRating());
        underTest.delete(created);
        underTest.delete(created.getArtist());
    }

    private SongDto addSongDto() {
        return underTest.save(new SongDto().setArtist(defaultArtist)
                        .setTitle(TITLE)
                        .setRating(VALID_RATING))
                .orElseThrow(() -> new RuntimeException("Can't add song to playlist"));
    }

    @Test
    @DisplayName("Read song with artist by ID")
    void testRead() {
        Optional<SongDto> read = underTest.getById(addSongDto().getId());

        assertTrue(read.isPresent());
        defaultArtist.setId(read.get().getArtist().getId());
        Assertions.assertEquals(defaultArtist, read.get().getArtist());
        Assertions.assertEquals(TITLE, read.get().getTitle());
        Assertions.assertEquals(VALID_RATING, read.get().getRating());
        underTest.delete(read.get());
        underTest.delete(read.get().getArtist());
    }

    @Test
    @DisplayName("Update song and artist")
    void testUpdate() {
        SongDto forUpdate = addSongDto();
        forUpdate.setArtist(forUpdate.getArtist().setName(forUpdate.getArtist().getName() + POSTFIX))
                .setTitle(forUpdate.getTitle() + POSTFIX)
                .setRating(forUpdate.getRating() + POSTFIX_INT);
        underTest.save(forUpdate);
        Optional<SongDto> updated = underTest.getById(forUpdate.getId());

        assertTrue(updated.isPresent());
        defaultArtist.setId(forUpdate.getArtist().getId());
        Assertions.assertEquals(defaultArtist.setName(defaultArtist.getName() + POSTFIX), updated.get().getArtist());
        Assertions.assertEquals(TITLE + POSTFIX, updated.get().getTitle());
        Assertions.assertEquals(VALID_RATING + POSTFIX_INT, updated.get().getRating());
        underTest.delete(forUpdate);
        underTest.delete(forUpdate.getArtist());
    }

    @Test
    @DisplayName("Remove artist with his songs")
    void testDelete() {
        SongDto forDelete = addSongDto();
        String id = forDelete.getId();
        String artistId = forDelete.getArtist().getId();
        underTest.delete(forDelete);
        underTest.delete(forDelete.getArtist());
        Assertions.assertFalse(underTest.getById(id).isPresent());
        Assertions.assertFalse(underTest.getByArtistId(artistId).isPresent());
    }

}
