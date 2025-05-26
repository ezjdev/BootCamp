package com.colvir.bootcamp.homework13.repository;

import com.colvir.bootcamp.homework13.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepository extends MongoRepository<Song, String> {
}
