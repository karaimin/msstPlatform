package com.msst.platform.repository;

import com.msst.platform.domain.LineVersionRating;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the LineVersionRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineVersionRatingRepository extends MongoRepository<LineVersionRating, String> {

}
