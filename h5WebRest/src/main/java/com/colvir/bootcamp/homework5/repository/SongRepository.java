package com.colvir.bootcamp.homework5.repository;

import com.colvir.bootcamp.homework5.model.Song;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
//    @Query(value = "SELECT s FROM Song s JOIN FETCH s.artist",
//            countQuery = "SELECT COUNT(s) FROM Song s")
    @Nonnull
    @EntityGraph(attributePaths = {"artist"})
    Page<Song> findAll(Pageable pageable);
}
