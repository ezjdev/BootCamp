package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.dto.ArtistDto;
import com.colvir.bootcamp.homework5.dto.SongDto;
import com.colvir.bootcamp.homework5.exception.ArtistNotFoundException;
import com.colvir.bootcamp.homework5.exception.PageNotFoundException;
import com.colvir.bootcamp.homework5.mapper.SongMapper;
import com.colvir.bootcamp.homework5.model.Artist;
import com.colvir.bootcamp.homework5.repository.ArtistRepository;
import com.colvir.bootcamp.homework5.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {

    private final SongMapper mapper;
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public Optional<SongDto> getById(Long songId) {
        return Optional.ofNullable(songId)
                .flatMap(songRepository::findById)
                .map(mapper::toDto);
    }

    @Transactional
    public Optional<SongDto> save(SongDto songDto) {
        return Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .map(it ->
                        Optional.ofNullable(it.getArtist())
                                .map(artist -> Optional.ofNullable(artist.getId())
                                        .flatMap(artistRepository::findById)
                                        .map(managedArtist -> applyChanges(artist, managedArtist))
                                        .orElseGet(() -> Optional.ofNullable(artist.getName())
                                                .map(artistRepository::findByName)
                                                .map(managedArtist -> applyChanges(artist, managedArtist))
                                                .orElseGet(() -> artistRepository.save(artist))
                                        )
                                )
                                .map(it::setArtist)
                                .map(songRepository::save)
                                .orElseThrow(() -> new ArtistNotFoundException("Can't add song without artist")))
                .map(mapper::toDto);
    }

    private Artist applyChanges(Artist source, Artist target) {
        return target.setName(source.getName())
                .setCountry(source.getCountry())
                .setGenre(source.getGenre());
    }

    @Transactional
    public void delete(SongDto songDto) {
        Optional.ofNullable(songDto)
                .map(mapper::fromDto)
                .ifPresent(songRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<SongDto> getPlaylist(Pageable pageable) {
        return Optional.ofNullable(pageable)
                .map(songRepository::findAll)
                .filter(it -> it.getTotalPages()-1 >= pageable.getPageNumber())
                .map(Page::getContent)
                .map(mapper::toDtoList)
                .orElseThrow(() -> new PageNotFoundException("Page " +
                        Optional.ofNullable(pageable)
                                .map(Pageable::getPageNumber)
                                .map(String::valueOf)
                                .orElse("null")
                        + " not found"));
    }

    @Transactional
    public void delete(ArtistDto artist) {
        Optional.ofNullable(artist)
                .map(mapper::fromDto)
                .ifPresent(it -> {
                    artistRepository.findById(it.getId())
                            .ifPresent(a -> songRepository.deleteAll(a.getSongs()));
                    artistRepository.delete(it);
                });
    }

    @Transactional(readOnly = true)
    public Optional<ArtistDto> getByArtistId(Long id) {
        return Optional.ofNullable(id)
                .flatMap(artistRepository::findById)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Long getSongsCount() {
        return songRepository.count();
    }
}
