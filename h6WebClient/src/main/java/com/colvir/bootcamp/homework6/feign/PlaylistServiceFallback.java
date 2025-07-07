package com.colvir.bootcamp.homework6.feign;

import com.colvir.bootcamp.homework6.dto.SongDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@Slf4j
public class PlaylistServiceFallback implements PlaylistClient {

    @Override
    public ResponseEntity<SongDto> getById(Long id) {
        log.error("getById {}", id);
        return ResponseEntity.ok(new SongDto());
    }

    @Override
    public ResponseEntity<List<SongDto>> getPlaylist() {
        log.error("get playlist");
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public void addSong(SongDto song) {
        log.error("addSong {}", song);
    }

    @Override
    public void update(Long id, SongDto song) {
        log.error("update {}", id);
    }

    @Override
    public void delete(Long id) {
        log.error("delete {}", id);
    }
}
