package com.colvir.bootcamp.homework5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Artist {
    private Long id;
    private String name;
    private String genre;
    private String country;
}
