package com.colvir.bootcamp.homework13.controller;

import com.colvir.bootcamp.homework13.dto.SongDto;
import com.colvir.bootcamp.homework13.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<SongDto>> getPlaylist() {
        return ResponseEntity.ok(playlistService.getPlaylist());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getById(@PathVariable String id) {
        return playlistService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SongDto addSong(@RequestBody @Valid SongDto songDto) {
        return Optional.of(songDto)
                .filter(dto -> Optional.ofNullable(dto.getId()).isEmpty())
                .flatMap(playlistService::save)
                .orElseThrow(() -> new IllegalArgumentException("Can't save a song that already have id"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody @Valid SongDto songDto) {
        return Optional.ofNullable(id)
                .map(songDto::setId)
                .map(it -> {
                    playlistService.save(it);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return Optional.of(id)
                .flatMap(playlistService::getById)
                .map(it -> {
                    playlistService.delete(it);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
