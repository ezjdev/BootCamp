package com.colvir.bootcamp.homework26.controller;

import com.colvir.bootcamp.homework26.dto.SongDto;
import com.colvir.bootcamp.homework26.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Slf4j
@Controller
@RefreshScope
@RequiredArgsConstructor
public class PlaylistClientController {

    public static final String REDIRECT = "redirect:/";
    public static final String ADD_SONG = "add-song";
    public static final String INDEX = "index";
    public static final String UPDATE_SONG = "update-song";

    private final PlaylistService playlistService;

    @GetMapping
    public String listItems(Model model) {
        model.addAttribute("songs", playlistService.getPlaylist().orElse(Collections.emptyList()));
        return INDEX;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("song", new SongDto());
        return ADD_SONG;
    }

    @PostMapping("/add")
    public String addSong(@ModelAttribute("song") @Valid SongDto song, BindingResult result) {
        if (result.hasErrors()) {
            return ADD_SONG;
        }
        playlistService.add(song);
        return REDIRECT;
    }

    @GetMapping("/update/{id}")
    public String showFormSong(@PathVariable Long id, Model model) {
        playlistService.getById(id).ifPresent(it -> model.addAttribute("song", it));
        return UPDATE_SONG;
    }

    @PostMapping("/update/{id}")
    public String updateSong(@PathVariable Long id, @ModelAttribute("song") @Valid SongDto songDto, BindingResult result) {
        if (result.hasErrors()) {
            return UPDATE_SONG;
        }
        songDto.setId(id);
        playlistService.update(songDto);
        return REDIRECT;
    }

    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        log.info("delete song {}", id);
        playlistService.delete(id);
        return REDIRECT;
    }

}
