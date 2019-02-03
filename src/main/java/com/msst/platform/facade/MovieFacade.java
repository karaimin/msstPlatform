package com.msst.platform.facade;

import com.msst.platform.domain.Movie;
import com.msst.platform.service.dto.MovieInfo;

import java.util.List;
import java.util.Optional;

public interface MovieFacade {
    Movie createMovie(Movie movie);

    List<MovieInfo> getAllMovieInfos();

    Optional<MovieInfo> getMovieInfo(String id);

    void delete(String id);
}
