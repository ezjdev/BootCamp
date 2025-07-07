package com.colvir.bootcamp.homework5.controller;

import com.colvir.bootcamp.homework5.api.PlaylistService;
import com.colvir.bootcamp.homework5.dto.SongDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RefreshScope
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<SongDto>> getPlaylist(
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer page,
            @RequestParam(required = false, defaultValue = "30") @Max(100) Integer size) {
        return ResponseEntity.ok(playlistService.getPlaylist(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getById(@PathVariable Long id) {
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
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid SongDto songDto) {
        return Optional.ofNullable(id)
                .map(songDto::setId)
                .map(it -> {
                    playlistService.save(it);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Deleting song: {}", id);
        return Optional.of(id)
                .flatMap(playlistService::getById)
                .map(it -> {
                    playlistService.delete(it);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/project-name")
    public String configServer(@Value("${info.project.name}") String foo) {
        return foo;
    }

}
