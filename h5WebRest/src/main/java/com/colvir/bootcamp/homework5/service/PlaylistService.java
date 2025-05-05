package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.dao.PlaylistDao;
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
    private final PlaylistDao dao;

    public Optional<SongDto> getById(Long songId) {
        return Optional.ofNullable(songId)
                .map(dao::getById)
                .map(mapper::toDto);
    }

    public Optional<SongDto> add(SongDto songDto) {
        return Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .map(dao::insertOrUpdate)
                .map(mapper::toDto);
    }

    public void delete(SongDto songDto) {
        Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .ifPresent(dao::delete);
    }

    public void update(SongDto songDto) {
        Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .ifPresent(dao::insertOrUpdate);
    }

    public List<SongDto> getPlaylist() {
        return mapper.toDtoList(dao.getPlaylist());
    }
}
