package com.msst.platform.repository;

import com.msst.platform.domain.Subtitle;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Subtitle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubtitleRepository extends MongoRepository<Subtitle, String> {

}
