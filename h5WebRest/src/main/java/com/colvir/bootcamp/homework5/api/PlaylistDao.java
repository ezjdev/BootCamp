package com.colvir.bootcamp.homework5.api;

import com.colvir.bootcamp.homework5.model.Artist;
import com.colvir.bootcamp.homework5.model.Song;

import java.util.List;

public interface PlaylistDao {

    Song insertOrUpdate(Song song);

    List<Song> getPlaylist();

    Song getById(Long id);

    void delete(Song song);

    Artist getArtistById(Long id);

    void delete(Artist artist);
}
