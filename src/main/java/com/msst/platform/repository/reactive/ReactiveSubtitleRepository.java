package com.msst.platform.repository.reactive;

import com.msst.platform.domain.Subtitle;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveSubtitleRepository extends ReactiveMongoRepository<Subtitle, String> {

}
