package com.colvir.bootcamp.homework13.model;

import io.github.kaiso.relmongo.annotation.OneToMany;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Artist {
    @Id
    private String id;
    private String name;
    private String genre;
    private String country;
    @OneToMany
    private List<Song> songs;
}
