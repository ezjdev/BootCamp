package com.colvir.bootcamp.homework6.service;

import com.colvir.bootcamp.homework6.feign.PlaylistClient;
import com.colvir.bootcamp.homework6.dto.SongDto;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistClient playlistClient;

    @Timed("get_playlist")
    public Optional<List<SongDto>> getPlaylist() {
        return Optional.ofNullable(playlistClient.getPlaylist().getBody());
    }

    @Timed("add_song")
    public void add(SongDto song) {
        playlistClient.addSong(song);
    }

    @Timed("get_by_id")
    public Optional<SongDto> getById(Long id) {
        return Optional.ofNullable(playlistClient.getById(id).getBody());
    }

    @Timed("update")
    public void update(SongDto songDto) {
        Optional.ofNullable(songDto)
                        .ifPresent(it -> playlistClient.update(songDto.getId(), songDto));
    }

    @Timed("delete")
    public void delete(Long id) {
        log.info("Deleting song: {}", id);
        playlistClient.delete(id);
    }
}
