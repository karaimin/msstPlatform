package com.msst.platform.repository;

import com.msst.platform.domain.LineVersion;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the LineVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineVersionRepository extends MongoRepository<LineVersion, String> {

}
