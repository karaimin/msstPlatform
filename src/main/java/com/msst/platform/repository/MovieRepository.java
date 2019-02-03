package com.msst.platform.repository;

import com.msst.platform.domain.Movie;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the MovieInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

}
