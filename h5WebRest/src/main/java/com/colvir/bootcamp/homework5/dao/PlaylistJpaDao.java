package com.colvir.bootcamp.homework5.dao;

import com.colvir.bootcamp.homework5.api.PlaylistDao;
import com.colvir.bootcamp.homework5.model.Artist;
import com.colvir.bootcamp.homework5.model.Song;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
@RequiredArgsConstructor
public class PlaylistJpaDao implements PlaylistDao {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public Song insertOrUpdate(Song song) {
        return Optional.of(song)
                .map(songEntity -> songEntity.setArtist(
                        Optional.ofNullable(songEntity.getArtist())
                                .filter(it -> Objects.nonNull(it.getId()))
                                .map(entityManager::merge)
                                .orElseGet(() ->
                                        Optional.ofNullable(entityManager.createQuery("select t from Artist t where name = :name", Artist.class)
                                                        .setParameter("name", song.getArtist().getName())
                                                        .getResultList())
                                                .filter(Predicate.not(List::isEmpty))
                                                .map(it -> it.get(0))
                                                .orElseGet(() -> {
                                                    Artist artist = songEntity.getArtist();
                                                    entityManager.persist(artist);
                                                    return artist;
                                                })
                                )))
                .filter(it -> Objects.nonNull(it.getId()))
                .map(entityManager::merge)
                .orElseGet(() -> {
                    entityManager.persist(song);
                    return song;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Song> getPlaylist() {
        return entityManager.createQuery("select s from Song s", Song.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Song getById(Long id) {
        return Optional.of(entityManager.createQuery(
                                """
                                        select t from Song t join fetch t.artist a where t.id = :id
                                        """
                                , Song.class)
                        .setParameter("id", id)
                        .getResultList())
                .filter(Predicate.not(List::isEmpty))
                .map(it -> it.get(0))
                .orElse(null);
    }

    @Override
    @Transactional
    public void delete(Song song) {
        Optional.ofNullable(entityManager.find(Song.class, song.getId()))
                .ifPresent(entityManager::remove);
    }

    @Override
    @Transactional(readOnly = true)
    public Artist getArtistById(Long id) {
        return Optional.of(entityManager.createQuery(
                                """
                                        select t from Artist t join fetch t.songs a where t.id = :id
                                        """
                                , Artist.class)
                        .setParameter("id", id)
                        .getResultList())
                .filter(Predicate.not(List::isEmpty))
                .map(it -> it.get(0))
                .orElse(null);
    }

    @Override
    @Transactional
    public void delete(Artist artist) {
        Optional.ofNullable(entityManager.find(Artist.class, artist.getId()))
                .ifPresent(entityManager::remove);
    }

}
