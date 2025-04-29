package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework5.exception.NoSuchSongException;
import com.colvir.bootcamp.homework5.mapper.SongMapper;
import com.colvir.bootcamp.homework5.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final SongMapper mapper;
    private final List<Song> playlist = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    public Optional<SongDto> getById(Long songId) {
        return Optional.ofNullable(songId)
                .map(id -> playlist.stream()
                        .filter(it -> id.equals(it.getId()))
                        .findFirst()
                        .map(mapper::toDto))
                .orElseThrow(() -> new NoSuchSongException("Song with this ID not found"));
    }

    public Optional<SongDto> add(SongDto songDto) {
        return Optional.ofNullable(songDto)
                .map(dto -> dto.setId(sequence.incrementAndGet()))
                .map(mapper::fromDto)
                .map(model -> {
                    playlist.add(model);
                    return model;
                })
                .map(mapper::toDto);
    }

    public Optional<SongDto> delete(SongDto songDto) {
        return Optional.ofNullable(songDto)
                        .map(mapper::fromDto)
                        .filter(playlist::contains)
                        .filter(playlist::remove)
                        .map(mapper::toDto);
    }

    public void update(SongDto songDto) {
        Optional.ofNullable(songDto)
                .flatMap(it -> playlist.stream()
                                            .filter(song -> song.getId().equals(songDto.getId()))
                                            .findFirst())
                .ifPresent(it -> playlist.set(playlist.indexOf(it), mapper.fromDto(songDto)));
    }

    public List<SongDto> getbPlaylist() {
        return playlist.stream().map(mapper::toDto).toList();
    }
}
