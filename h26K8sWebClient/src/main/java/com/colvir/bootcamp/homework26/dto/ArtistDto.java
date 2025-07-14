package com.colvir.bootcamp.homework26.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArtistDto {
    @Min(0)
    private Long id;
    @NotEmpty
    private String name;
    private String genre;
    private String country;
}
