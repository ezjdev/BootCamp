package com.colvir.bootcamp.homework13.repository;

import com.colvir.bootcamp.homework13.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<Artist, String> {
    Artist findByName(String name);
}
