package com.colvir.bootcamp.homework5.model;

import lombok.Data;

import java.time.Duration;

@Data
public class Song {
    private Long id;
    private String artist;
    private String title;
    private Duration duration;
    private String rating;
}
