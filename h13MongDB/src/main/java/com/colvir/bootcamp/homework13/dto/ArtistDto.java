package com.colvir.bootcamp.homework13.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ArtistDto {
    private String id;
    private String name;
    private String genre;
    private String country;
    private List<SongDto> songs;
}
