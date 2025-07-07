package com.colvir.bootcamp.homework6.feign;

import com.colvir.bootcamp.homework6.dto.SongDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "PlaylistService", fallback = PlaylistServiceFallback.class)
public interface PlaylistClient {

    @GetMapping("api/playlist/{id}")
    ResponseEntity<SongDto> getById(@PathVariable Long id);

    @GetMapping("api/playlist")
    ResponseEntity<List<SongDto>> getPlaylist();

    @PostMapping("api/playlist")
    void addSong(@RequestBody SongDto song);

    @PutMapping("api/playlist/{id}")
    void update(@PathVariable Long id, @RequestBody SongDto song);

    @DeleteMapping("api/playlist/{id}")
    void delete(@PathVariable Long id);

}
