package com.colvir.bootcamp.homework5.api;

import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    Optional<SongDto> getById(Long songId);
    Optional<SongDto> save(SongDto songDto);
    void delete(SongDto songDto);
    List<SongDto> getPlaylist(Pageable pageable);
    void delete(ArtistDto artist);
    Optional<ArtistDto> getByArtistId(Long id);
    Long getSongsCount();
}
