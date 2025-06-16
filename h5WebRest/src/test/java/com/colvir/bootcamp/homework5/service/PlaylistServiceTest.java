package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.WebRunner;
import com.colvir.bootcamp.homework5.api.PlaylistService;
import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework5.exception.PageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {WebRunner.class})
@Slf4j
class PlaylistServiceTest {

    public static final String TITLE = "Title";
    public static final Integer VALID_RATING = 6;
    public static final String NAME = "Name";
    public static final String COUNTRY = "Country";
    public static final String GENRE = "Genre";
    private static final Integer PAGE_SIZE = 30;
    public static final int MAX_PAGE_NUMBER = 10;
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
        underTest.delete(created.getArtist());
    }

    private SongDto addSongDto() {
        return underTest.save(new SongDto().setArtist(defaultArtist/*.setId(null)*/)
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
        underTest.delete(forUpdate.getArtist());
    }

    @Test
    @DisplayName("Remove artist with his songs")
    void testDelete() {
        SongDto forDelete = addSongDto();
        Long id = forDelete.getId();
        Long artistId = forDelete.getArtist().getId();
        underTest.delete(forDelete.getArtist());
        Assertions.assertFalse(underTest.getById(id).isPresent());
        Assertions.assertFalse(underTest.getByArtistId(artistId).isPresent());
    }

    @Test
    @DisplayName("Read the list of songs using pagination")
    void testPagination() {
        Assertions.assertThrows(PageNotFoundException.class, () -> {
            IntStream.iterate(0, it -> it < MAX_PAGE_NUMBER, i -> ++i)
                    .forEach(it -> underTest.getPlaylist(PageRequest.of(it, PAGE_SIZE)));
        });
        Long readRecords = 0L;
        Long maxRecordsOnPage = 0L;
        Long songsCount = underTest.getSongsCount();
        try {
            for (int i = 0; i < MAX_PAGE_NUMBER; ++i) {
                int readCurrentRecords = underTest.getPlaylist(PageRequest.of(i, PAGE_SIZE)).size();
                readRecords += readCurrentRecords;
                maxRecordsOnPage = Math.max(readCurrentRecords, maxRecordsOnPage);
            }
        } catch (PageNotFoundException e) {
            log.error(e.getMessage());
        }
        Assertions.assertEquals(songsCount, readRecords);
        Assertions.assertEquals(Long.valueOf(PAGE_SIZE), maxRecordsOnPage);
    }

}
