package com.colvir.bootcamp.homework13.mapper;

import com.colvir.bootcamp.homework13.dto.ArtistDto;
import com.colvir.bootcamp.homework13.dto.SongDto;
import com.colvir.bootcamp.homework13.model.Artist;
import com.colvir.bootcamp.homework13.model.Song;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PlaylistMapper {

    SongDto toDto(Song song);
    Song fromDto(SongDto songDto);

    ArtistDto toDto(Artist artist);
    Artist fromDto(ArtistDto artistDto);

    default List<SongDto> toDto(List<Song> songs) {
        return Optional.ofNullable(songs)
                .map(it -> it.stream().map(this::toDto).toList())
                .orElse(null);
    }
}
