package com.colvir.bootcamp.homework13.service;

import com.colvir.bootcamp.homework13.dto.ArtistDto;
import com.colvir.bootcamp.homework13.dto.SongDto;
import com.colvir.bootcamp.homework13.mapper.PlaylistMapper;
import com.colvir.bootcamp.homework13.repository.ArtistRepository;
import com.colvir.bootcamp.homework13.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final PlaylistMapper mapper;

    @Transactional
    public void delete(ArtistDto artist) {
        Optional.ofNullable(artist)
                .map(ArtistDto::getId)
                .filter(Predicate.not(String::isBlank))
                .flatMap(artistRepository::findById)
                .map(it -> {
                            Optional.ofNullable(it.getSongs())
                                    .ifPresent(songRepository::deleteAll);
                            return it;
                        }
                )
                .ifPresent(artistRepository::delete);
    }

    public void delete(SongDto song) {
        Optional.ofNullable(song)
                .map(SongDto::getId)
                .filter(Predicate.not(String::isBlank))
                .flatMap(songRepository::findById)
                .map(it -> {
                            Optional.ofNullable(it.getArtist())
                                    .ifPresent(artistRepository::delete);
                            return it;
                        }
                )
                .ifPresent(songRepository::delete);

    }

    public Optional<ArtistDto> save(ArtistDto artistDto) {
        return Optional.of(
                mapper.toDto(
                        artistRepository.save(
                                                Optional.ofNullable(artistDto)
                                                        .filter(it -> Objects.isNull(it.getId()))
                                                        .map(ArtistDto::getName)
                                                        .map(artistRepository::findByName)
                                        .orElseGet(() -> mapper.fromDto(artistDto)))));
    }

    @Transactional
    public Optional<SongDto> save(SongDto songDto) {
        return Optional.ofNullable(songDto)
                .map(it -> it.setArtist(save(it.getArtist())
                        .orElseThrow(() -> new RuntimeException("Artist does not exist"))))
                .map(it -> mapper.toDto(songRepository.save(mapper.fromDto(it))));
    }

    @Transactional(readOnly = true)
    public Optional<SongDto> getById(String id) {
        return songRepository.findById(id).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ArtistDto> getByArtistId(String artistId) {
        return Optional.ofNullable(artistId)
                .filter(Predicate.not(String::isBlank))
                .flatMap(artistRepository::findById)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<SongDto> getPlaylist() {
        return mapper.toDto(songRepository.findAll());
    }

}
