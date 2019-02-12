package com.msst.platform.service.impl;

import com.msst.platform.domain.LineVersion;
import com.msst.platform.service.SubtitleLineService;
import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.repository.SubtitleLineRepository;
import com.msst.platform.service.LineVersionAddedEventPublisher;
import com.msst.platform.web.rest.errors.SubtitleFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SubtitleLine.
 */
@Service
public class SubtitleLineServiceImpl implements SubtitleLineService {

    private final Logger log = LoggerFactory.getLogger(SubtitleLineServiceImpl.class);

    private final SubtitleLineRepository subtitleLineRepository;

    public SubtitleLineServiceImpl(SubtitleLineRepository subtitleLineRepository) {
        this.subtitleLineRepository = subtitleLineRepository;
    }

    /**
     * Save a subtitleLine.
     *
     * @param subtitleLine the entity to save
     * @return the persisted entity
     */
    @Override
    public SubtitleLine save(SubtitleLine subtitleLine) {
        log.debug("Request to save SubtitleLine : {}", subtitleLine);
        return subtitleLineRepository.save(subtitleLine);
    }

    /**
     * Get all the subtitleLines.
     *
     * @return the list of entities
     */
    @Override
    public List<SubtitleLine> findAll() {
        log.debug("Request to get all SubtitleLines");
        return subtitleLineRepository.findAll();
    }


    /**
     * Get one subtitleLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<SubtitleLine> findOne(String id) {
        log.debug("Request to get SubtitleLine : {}", id);
        return subtitleLineRepository.findById(id);
    }

    /**
     * Delete the subtitleLine by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete SubtitleLine : {}", id);
        subtitleLineRepository.deleteById(id);
    }

  @Override
  public Collection<SubtitleLine> creatBulkWithSameVersion(Collection<SubtitleLine> subtitleLines) {
      return subtitleLineRepository.saveAll(subtitleLines);
  }

  @Override
  public Collection<SubtitleLine> createLinesWithEmptyVersions(Collection<SubtitleLine> subtitleLines) {
      return subtitleLineRepository.saveAll(subtitleLines.stream()
        .map(subtitleLine -> new SubtitleLine()
          .startTime(subtitleLine.getStartTime())
          .endTime(subtitleLine.getEndTime())
          .subtitle(subtitleLine.getSubtitle())
          .sequenceNumber(subtitleLine.getSequenceNumber())
        )
        .map(subtitleLineRepository::save)
        .collect(Collectors.toList())
      );
  }

  @Override
  public LineVersion addNewTranslatedLine(String subtitleId, LineVersion lineVersion) {
      SubtitleLine subtitleLine = subtitleLineRepository.findById(subtitleId)
        .orElseThrow(() -> new SubtitleFormatException("Subtitle line was not found"));

      if(subtitleLine.getVersions() != null && subtitleLine.getVersions().contains(lineVersion)) {
        return lineVersion;
      }

      lineVersion.setVersion(UUID.randomUUID().toString());
      subtitleLine.addVersions(lineVersion);

      subtitleLineRepository.save(subtitleLine);
      return lineVersion;
  }
}
