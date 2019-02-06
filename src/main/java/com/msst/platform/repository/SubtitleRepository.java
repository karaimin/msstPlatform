package com.msst.platform.repository;

import com.msst.platform.domain.Language;
import com.msst.platform.domain.Subtitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Subtitle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubtitleRepository extends MongoRepository<Subtitle, String> {
  List<Subtitle> findByVersionAndMovieIdAndLanguage(String version, String movieId, Language language);

  Optional<Subtitle> findByParentAndLanguage(Subtitle parent, Language language);
}
