package com.colvir.bootcamp.homework5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Time;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Song {
    private Long id;
    private Artist artist;
    private String title;
    private Time duration;
    private Integer rating;
}
