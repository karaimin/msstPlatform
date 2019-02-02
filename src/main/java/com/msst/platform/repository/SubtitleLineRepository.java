package com.msst.platform.repository;

import com.msst.platform.domain.SubtitleLine;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the SubtitleLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubtitleLineRepository extends MongoRepository<SubtitleLine, String> {

}
