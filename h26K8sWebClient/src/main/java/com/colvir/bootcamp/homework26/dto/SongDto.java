package com.colvir.bootcamp.homework26.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Time;

@Data
@Accessors(chain = true)
public class SongDto {
    @Min(0)
    private Long id;
    private ArtistDto artist;
    @NotEmpty
    private String title;
    private Time duration;
    @Min(4)
    private Integer rating;
}