package com.colvir.bootcamp.homework6.controller;

import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework6.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class PlaylistClientController {

    public static final String REDIRECT = "redirect:/";
    public static final String ADD_SONG = "add-song";
    public static final String INDEX = "index";

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
    public String addSong(@ModelAttribute SongDto song) {
        playlistService.add(song);
        return REDIRECT;
    }

    @GetMapping("/update/{id}")
    public String showFormSong(@PathVariable Long id, Model model) {
        playlistService.getById(id).ifPresent(it -> model.addAttribute("song", it));
        return "update-song";
    }

    @PostMapping("/update/{id}")
    public String updateSong(@PathVariable Long id, @ModelAttribute SongDto songDto) {
        songDto.setId(id);
        playlistService.update(songDto);
        return REDIRECT;
    }

    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        playlistService.delete(id);
        return REDIRECT;
    }

}
