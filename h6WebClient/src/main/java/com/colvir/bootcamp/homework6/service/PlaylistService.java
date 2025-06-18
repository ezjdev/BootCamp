package com.colvir.bootcamp.homework6.service;

import com.colvir.bootcamp.homework6.api.PlaylistClient;
import com.colvir.bootcamp.homework6.dto.SongDto;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistClient playlistClient;
    private final AuthService authService;

    @Timed("get_playlist")
    public Optional<List<SongDto>> getPlaylist() {
        return Optional.ofNullable(playlistClient.getPlaylist(authService.getBearerString()).getBody());
    }

    @Timed("add_song")
    public void add(SongDto song) {
        playlistClient.addSong(song, authService.getBearerString());
    }

    @Timed("get_by_id")
    public Optional<SongDto> getById(Long id) {
        return Optional.ofNullable(playlistClient.getById(id).getBody());
    }

    @Timed("update")
    public void update(SongDto songDto) {
        Optional.ofNullable(songDto)
                        .ifPresent(it -> playlistClient.update(songDto.getId(), songDto, authService.getBearerString()));
    }

    @Timed("delete")
    public void delete(Long id) {
        playlistClient.delete(id, authService.getBearerString());
    }
}
