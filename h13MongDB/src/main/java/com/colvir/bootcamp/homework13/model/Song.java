package com.colvir.bootcamp.homework13.model;

import io.github.kaiso.relmongo.annotation.ManyToOne;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Time;

@Data
@Document
public class Song {
    @Id
    private String id;
    @ManyToOne
    private Artist artist;
    private String title;
    private Time duration;
    private Integer rating;
}
