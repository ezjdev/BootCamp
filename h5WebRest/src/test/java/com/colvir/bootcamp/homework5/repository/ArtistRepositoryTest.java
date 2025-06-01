package com.colvir.bootcamp.homework5.repository;

import com.colvir.bootcamp.homework5.model.Artist;
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
class ArtistRepositoryTest {

    public static final int ARTIST_LIST_SIZE = 98;
    public static final String THE_BEATLES = "The Beatles";
    public static final long THE_BEATLES_ID = 1L;
    public static final String PK_NAME = "id";
    public static final String NAME = "Name";
    public static final String COUNTRY = "Country";
    public static final String GENRE = "Genre";

    @Autowired
    private ArtistRepository underTest;

    @Test
    @DisplayName("Your context has been loaded")
    void contextLoad() {}

    @DisplayName("Read list")
    @Test
    void readList() {
        var artistList = underTest.findAll();
        assertThat(artistList).isNotNull()
                .hasSize(ARTIST_LIST_SIZE)
                .allMatch(not(it -> it.getId() == null))
                .allMatch(not(it -> it.getName() == null || it.getName().isEmpty()));
    }

    @DisplayName("Read")
    @Test
    void read() {
        var artist = underTest.findByName(THE_BEATLES);
        assertThat(artist).isNotNull()
                .hasFieldOrPropertyWithValue(PK_NAME, THE_BEATLES_ID);
    }

    @DisplayName("Insert and delete")
    @Test
    void insertAndDelete() {
        var artistListSizeBefore = underTest.findAll().size();
        Optional.of(underTest.save(new Artist().setName(NAME)
                        .setCountry(COUNTRY)
                        .setGenre(GENRE)))
                .ifPresent(underTest::delete);
        assertThat(artistListSizeBefore).isEqualTo(underTest.findAll().size());
    }

}