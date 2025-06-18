package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.api.PlaylistService;
import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
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
    public static final String RETURNED_SONGS_COUNT = "returned_songs_count";

    private final PlaylistServiceImpl playlistService;
    private final CacheManager cacheManager;
    private final MeterRegistry meterRegistry;

    @Timed("get_by_id")
    @Override
    @Cacheable(cacheNames = BOOTCAMP_SONG_ID, key = "#songId", unless = "#result == null")
    public Optional<SongDto> getById(Long songId) {
        return playlistService.getById(songId);
    }


    @Timed("save")
    @Override
    public Optional<SongDto> save(SongDto songDto) {
        Optional<SongDto> result = playlistService.save(songDto);
        result.ifPresent(it -> Optional.ofNullable(cacheManager.getCache(BOOTCAMP_SONG_ID))
                .ifPresent(cache -> cache.put(it.getId(), songDto)));
        return result;
    }

    @Timed("delete_song")
    @Override
    @CacheEvict(cacheNames = BOOTCAMP_SONG_ID, key = "#songDto.id")
    public void delete(SongDto songDto) {
        playlistService.delete(songDto);
    }

    @Timed("get_playlist")
    @Override
    public List<SongDto> getPlaylist(Pageable pageable) {
        List<SongDto> result = playlistService.getPlaylist(pageable);
        meterRegistry.counter(RETURNED_SONGS_COUNT).increment(result.size());
        return result;
    }

    @Timed("delete_artist")
    @Override
    @CacheEvict(cacheNames = BOOTCAMP_ARTIST_ID, key = "#artist.id")
    public void delete(ArtistDto artist) {
        playlistService.delete(artist);
    }

    @Timed("get_artist_by_id")
    @Override
    @Cacheable(cacheNames = BOOTCAMP_ARTIST_ID, key = "#id", unless = "#result == null")
    public Optional<ArtistDto> getByArtistId(Long id) {
        return playlistService.getByArtistId(id);
    }

    @Timed("get_songs_count")
    @Override
    public Long getSongsCount() {
        return playlistService.getSongsCount();
    }
}
