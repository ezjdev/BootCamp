package com.colvir.bootcamp.homework13.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Time;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SongDto {
    private String id;
    private ArtistDto artist;
    private String title;
    private Time duration;
    private Integer rating;
}
