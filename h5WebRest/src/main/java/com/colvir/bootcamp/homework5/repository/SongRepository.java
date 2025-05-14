package com.colvir.bootcamp.homework5.repository;

import com.colvir.bootcamp.homework5.model.Song;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
    @Nonnull
    @EntityGraph(attributePaths = {"artist"})
    Page<Song> findAll(Pageable pageable);
}
