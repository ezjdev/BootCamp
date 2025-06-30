package com.colvir.bootcamp.homework6.controller;

import com.colvir.bootcamp.homework6.dto.CredentialsDto;
import com.colvir.bootcamp.homework6.dto.SongDto;
import com.colvir.bootcamp.homework6.service.AuthService;
import com.colvir.bootcamp.homework6.service.PlaylistService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

import static com.colvir.bootcamp.homework6.security.filter.JwtSecurityFilter.JWT_TOKEN;

@Controller
@RequiredArgsConstructor
public class PlaylistClientController {

    public static final String REDIRECT = "redirect:/";
    public static final String ADD_SONG = "add-song";
    public static final String INDEX = "index";
    public static final String UPDATE_SONG = "update-song";
    public static final String LOGIN = "login";

    private final PlaylistService playlistService;
    private final AuthService authService;

    @GetMapping
    public String listItems(Model model, Authentication authentication) {
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
        playlistService.delete(id);
        return REDIRECT;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("cred", new CredentialsDto());
        return LOGIN;
    }

    @PostMapping("/login")
    public String authenticate(@ModelAttribute("cred") CredentialsDto cred, Model model, HttpSession session, HttpServletResponse response) {
        try {
            String token = authService.authenticate(cred);
            session.setAttribute(JWT_TOKEN, token);
            return REDIRECT;
        } catch (Exception e) {
            model.addAttribute(
                    "error", "Invalid credentials");
            return LOGIN;
        }
    }

    @GetMapping("/project-name")
    public ResponseEntity<String> configServer(@Value("${info.project.name}") String foo) {
        return ResponseEntity.ok(foo);
    }

}
