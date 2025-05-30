package com.colvir.bootcamp.homework6.api;

import com.colvir.bootcamp.homework6.dto.CredentialsDto;
import com.colvir.bootcamp.homework6.dto.SongDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

public interface PlaylistClient {
    @GetExchange("api/playlist/{id}")
    ResponseEntity<SongDto> getById(@PathVariable Long id);

    @GetExchange("api/playlist")
    ResponseEntity<List<SongDto>> getPlaylist(@RequestHeader String authorization);

    @PostExchange("/login")
    ResponseEntity<String> login(@RequestBody CredentialsDto credentialsDto);

    @PostExchange("api/playlist")
    void addSong(@RequestBody SongDto song, @RequestHeader String authorization);

    @PutExchange("api/playlist/{id}")
    void update(@PathVariable Long id, @RequestBody SongDto song, @RequestHeader String authorization);

    @DeleteExchange("api/playlist/{id}")
    void delete(@PathVariable Long id, @RequestHeader String authorization);

}
