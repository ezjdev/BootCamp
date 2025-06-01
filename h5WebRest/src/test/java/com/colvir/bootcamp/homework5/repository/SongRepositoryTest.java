package com.colvir.bootcamp.homework5.repository;

import com.colvir.bootcamp.homework5.model.Song;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
@DirtiesContext(classMode = BEFORE_CLASS)
class SongRepositoryTest {

    public static final int LIST_SIZE = 99;
    public static final String BOHEMIAN_RHAPSODY = "Bohemian Rhapsody";
    public static final long BOHEMIAN_RHAPSODY_ID = 2L;
    public static final String TITLE = "title";
    public static final Integer VALID_RATING = 6;
    public static final long THE_BEATLES_ID = 1L;

    @Autowired
    private SongRepository underTest;
    @Autowired
    private ArtistRepository artistRepository;


    @Test
    @DisplayName("Your context has been loaded")
    void contextLoad() {}

    @DisplayName("Read list")
    @Test
    void readList() {
        var artistList = underTest.findAll();
        assertThat(artistList).isNotNull()
                .hasSize(LIST_SIZE)
                .allMatch(not(it -> it.getId() == null))
                .allMatch(not(it -> it.getTitle() == null || it.getTitle().isEmpty()));
    }

    @DisplayName("Read")
    @Test
    void read() {
        var song = underTest.findById(BOHEMIAN_RHAPSODY_ID).orElseThrow();
        assertThat(song).isNotNull()
                .hasFieldOrPropertyWithValue(TITLE, BOHEMIAN_RHAPSODY);
    }

    @DisplayName("Insert and delete")
    @Test
    void insertAndDelete() {
        var artistListSizeBefore = underTest.findAll().size();
        Optional.of(underTest.save(
                        new Song().setArtist(
                                        artistRepository.findById(THE_BEATLES_ID)
                                                .orElseThrow())
                                .setTitle(TITLE)
                                .setRating(VALID_RATING)))
                .ifPresent(underTest::delete);
        assertThat(artistListSizeBefore).isEqualTo(underTest.findAll().size());
    }
}