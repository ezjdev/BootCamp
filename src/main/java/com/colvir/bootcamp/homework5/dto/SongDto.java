package com.colvir.bootcamp.homework5.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Duration;

@Data
@Accessors(chain = true)
public class SongDto {
    @Min(0)
    private Long id;
    @NotEmpty
    private String artist;
    @NotEmpty
    private String title;
    private Duration duration;
    @Min(4)
    @NotEmpty
    private String rating;
}
