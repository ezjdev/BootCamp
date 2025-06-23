package com.colvir.bootcamp.homework6.feign;

import com.colvir.bootcamp.homework6.dto.CredentialsDto;
import com.colvir.bootcamp.homework6.dto.SongDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "PlaylistService", fallback = PlaylistServiceFallback.class)
public interface PlaylistClient {

    @GetMapping("api/playlist/{id}")
    ResponseEntity<SongDto> getById(@PathVariable Long id, @RequestHeader String authorization);

    @GetMapping("api/playlist")
    ResponseEntity<List<SongDto>> getPlaylist(@RequestHeader String authorization);

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody CredentialsDto credentialsDto);

    @PostMapping("api/playlist")
    void addSong(@RequestBody SongDto song, @RequestHeader String authorization);

    @PutMapping("api/playlist/{id}")
    void update(@PathVariable Long id, @RequestBody SongDto song, @RequestHeader String authorization);

    @DeleteMapping("api/playlist/{id}")
    void delete(@PathVariable Long id, @RequestHeader String authorization);

}
