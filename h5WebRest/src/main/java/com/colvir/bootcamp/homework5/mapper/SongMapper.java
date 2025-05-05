package com.colvir.bootcamp.homework5.mapper;

import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework5.model.Song;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SongMapper {

    SongDto toDto (Song song);
    Song fromDto (SongDto songDto);

    default List<SongDto> toDtoList (List<Song> songList) {
        return songList.stream().map(this::toDto).toList();
    }

}
