package com.msst.platform.repository.reactive;

import com.msst.platform.domain.SubtitleLine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReactiveSubtitleLineRepository extends ReactiveMongoRepository<SubtitleLine, String> {
  Flux<SubtitleLine> findAllBySubtitleId(String subtitleId);
}
