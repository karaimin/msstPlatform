package com.msst.platform.repository.reactive;

import com.msst.platform.domain.Authority;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {
}
