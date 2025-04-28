package com.colvir.bootcamp.homework5.controller;

import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework5.service.PlaylistService;
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
    public ResponseEntity<List<SongDto>> getPlaylist(){
        return ResponseEntity.ok(playlistService.getbPlaylist());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getById(@PathVariable Long id){
        return playlistService.getById(id)
                                .map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SongDto addSong(@RequestBody @Valid SongDto songDto){
        return Optional.of(songDto)
                        .filter(dto -> Optional.ofNullable(dto.getId()).isEmpty())
                        .flatMap(playlistService::add)
                        .orElseThrow(() -> new IllegalArgumentException("Can't save a song that already have id"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid SongDto songDto){
        playlistService.update(songDto.setId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return Optional.of(id)
                        .flatMap(playlistService::getById)
                        .flatMap(playlistService::delete)
                        .map(it -> ResponseEntity.noContent().build())
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
