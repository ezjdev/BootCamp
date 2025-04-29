package com.colvir.bootcamp.homework5.mapper;

import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework5.model.Song;
import org.mapstruct.Mapper;

@Mapper
public interface SongMapper {

    SongDto toDto (Song song);
    Song fromDto (SongDto songDto);

}
