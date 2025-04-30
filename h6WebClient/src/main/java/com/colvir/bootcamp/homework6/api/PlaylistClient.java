package com.colvir.bootcamp.homework6.api;

import com.colvir.bootcamp.homework5.dto.SongDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

public interface PlaylistClient {
    @GetExchange("/{id}")
    ResponseEntity<SongDto> getById(@PathVariable Long id);

    @GetExchange
    ResponseEntity<List<SongDto>> getPlaylist();

    @PostExchange
    void addSong(@RequestBody SongDto song);

    @PutExchange("/{id}")
    void update(@PathVariable Long id, @RequestBody SongDto song);

    @DeleteExchange("/{id}")
    void delete(@PathVariable Long id);

}
