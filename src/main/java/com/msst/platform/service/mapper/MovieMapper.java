package com.msst.platform.service.mapper;

import com.msst.platform.domain.Movie;
import com.msst.platform.service.dto.MovieInfo;

import java.util.Objects;

public class MovieMapper {
    private MovieMapper() {

    }

    public static MovieInfo.Builder convertToMovieInfo(Movie movie) {
        Objects.requireNonNull(movie);
        return MovieInfo.builder()
            .setResourceId(movie.getId())
            .setDescription(movie.getDescription())
            .setDuration(movie.getDuration())
            .setName(movie.getName());
    }
}
