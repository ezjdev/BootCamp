package com.colvir.bootcamp.homework5.repository;

import com.colvir.bootcamp.homework5.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByName(String name);
}
