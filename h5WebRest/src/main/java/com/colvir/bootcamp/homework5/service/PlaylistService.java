package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.api.PlaylistDao;
import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework5.mapper.SongMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final SongMapper mapper;
    private final PlaylistDao playlistJpaDao;

    public Optional<SongDto> getById(Long songId) {
        return Optional.ofNullable(songId)
                .map(playlistJpaDao::getById)
                .map(mapper::toDto);
    }

    public Optional<SongDto> add(SongDto songDto) {
        return Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .map(playlistJpaDao::insertOrUpdate)
                .map(mapper::toDto);
    }

    public void delete(SongDto songDto) {
        Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .ifPresent(playlistJpaDao::delete);
    }

    public void update(SongDto songDto) {
        Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .ifPresent(playlistJpaDao::insertOrUpdate);
    }

    public List<SongDto> getPlaylist() {
        return mapper.toDtoList(playlistJpaDao.getPlaylist());
    }

    public void delete(ArtistDto artist) {
        Optional.ofNullable(artist)
                .map(mapper::fromDto)
                .ifPresent(playlistJpaDao::delete);
    }

    public Optional<ArtistDto> getByArtistId(Long id) {
        return Optional.ofNullable(id)
                .map(playlistJpaDao::getArtistById)
                .map(mapper::toDto);
    }
}
