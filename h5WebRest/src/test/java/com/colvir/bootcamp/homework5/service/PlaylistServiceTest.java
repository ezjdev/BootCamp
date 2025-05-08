package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.WebRunner;
import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    @DisplayName("Cascade create test")
    void testCreate() {
        SongDto created = addSongDto();
        assertNotNull(created.getId());
        defaultArtist.setId(created.getArtist().getId());
        Assertions.assertEquals(defaultArtist, created.getArtist());
        Assertions.assertEquals(TITLE, created.getTitle());
        Assertions.assertEquals(VALID_RATING, created.getRating());
    }

    private SongDto addSongDto() {
        return underTest.add(new SongDto().setArtist(defaultArtist/*.setId(null)*/)
                        .setTitle(TITLE)
                        .setRating(VALID_RATING))
                .orElseThrow(() -> new RuntimeException("Can't add song to playlist"));
    }

    @Test
    @DisplayName("N+1 selects problem")
//  Do not reproduce the problem in full, only in part of fetch.LAZY
//  If you don't use JOIN FETCH throws the org.hibernate.LazyInitializationException error
    void testRead() {
        Optional<SongDto> read = underTest.getById(addSongDto().getId());

        assertTrue(read.isPresent());
        defaultArtist.setId(read.get().getArtist().getId());
        Assertions.assertEquals(defaultArtist, read.get().getArtist());
        Assertions.assertEquals(TITLE, read.get().getTitle());
        Assertions.assertEquals(VALID_RATING, read.get().getRating());
    }

    @Test
    @DisplayName("Cascade update test")
    void testUpdate() {
        SongDto forUpdate = addSongDto();
        forUpdate.setArtist(forUpdate.getArtist().setName(forUpdate.getArtist().getName() + POSTFIX))
                .setTitle(forUpdate.getTitle() + POSTFIX)
                .setRating(forUpdate.getRating() + POSTFIX_INT);
        underTest.update(forUpdate);
        Optional<SongDto> updated = underTest.getById(forUpdate.getId());

        assertTrue(updated.isPresent());
        defaultArtist.setId(forUpdate.getArtist().getId());
        Assertions.assertEquals(defaultArtist.setName(defaultArtist.getName() + POSTFIX), updated.get().getArtist());
        Assertions.assertEquals(TITLE + POSTFIX, updated.get().getTitle());
        Assertions.assertEquals(VALID_RATING + POSTFIX_INT, updated.get().getRating());
    }

    @Test
    @DisplayName("Cascade delete test")
    void testDelete() {
        SongDto forDelete = addSongDto();
        Long id = forDelete.getId();
        Long artistId = forDelete.getArtist().getId();
        underTest.delete(forDelete.getArtist());
        Assertions.assertFalse(underTest.getById(id).isPresent());
        Assertions.assertFalse(underTest.getByArtistId(artistId).isPresent());
    }

}
