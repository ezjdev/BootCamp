package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.api.PlaylistService;
import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class CachingPlayListService implements PlaylistService {

    public static final String BOOTCAMP_ARTIST_ID = "bootcamp_artist_id";
    public static final String BOOTCAMP_SONG_ID = "bootcamp_song_id";

    private final PlaylistServiceImpl playlistService;
    private final CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = BOOTCAMP_SONG_ID, key = "#songId")
    public Optional<SongDto> getById(Long songId) {
        return playlistService.getById(songId);
    }

    @Override
    public Optional<SongDto> save(SongDto songDto) {
        Optional<SongDto> result = playlistService.save(songDto);
        result.ifPresent(it -> Optional.ofNullable(cacheManager.getCache(BOOTCAMP_SONG_ID))
                .ifPresent(cache -> cache.put(it.getId(), songDto)));
        return result;
    }

    @Override
    @CacheEvict(cacheNames = BOOTCAMP_SONG_ID, key = "#songDto.id")
    public void delete(SongDto songDto) {
        playlistService.delete(songDto);
    }

    @Override
    public List<SongDto> getPlaylist(Pageable pageable) {
        return playlistService.getPlaylist(pageable);
    }

    @Override
    @CacheEvict(cacheNames = BOOTCAMP_ARTIST_ID, key = "#artist.id")
    public void delete(ArtistDto artist) {
        playlistService.delete(artist);
    }

    @Override
    @Cacheable(cacheNames = BOOTCAMP_ARTIST_ID, key = "#id")
    public Optional<ArtistDto> getByArtistId(Long id) {
        return playlistService.getByArtistId(id);
    }

    @Override
    public Long getSongsCount() {
        return playlistService.getSongsCount();
    }
}
