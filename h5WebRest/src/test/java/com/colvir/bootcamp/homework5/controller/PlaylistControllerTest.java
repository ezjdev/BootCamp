package com.colvir.bootcamp.homework5.controller;

import com.colvir.bootcamp.homework5.api.PlaylistService;
import com.colvir.bootcamp.homework5.dto.SongDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaylistController.class)
@AutoConfigureMockMvc
class PlaylistControllerTest {

    public static final long SONG_ID = 1L;
    public static final String TITLE = "Title";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlaylistService playlistService;

    @Test
    @WithMockUser(username = "chupacabra", authorities = {"ROLE_CHUPACABRA"})
    void getAllPlaylists() throws Exception {

        when(playlistService.getById(SONG_ID))
                .thenReturn(Optional.of(new SongDto()
                        .setId(SONG_ID)
                        .setTitle(TITLE)));

        this.mockMvc.perform(get("/api/playlist/{id}", SONG_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SONG_ID))
                .andExpect(jsonPath("$.title").value(TITLE));
    }

}