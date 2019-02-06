package com.msst.platform.web.rest;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.facade.MsstPlatformFacade;
import com.msst.platform.facade.SubtitleFacade;
import com.msst.platform.service.SubtitleService;
import com.msst.platform.service.dto.StartTranslateSubtitleTranslateInfo;
import com.msst.platform.web.rest.errors.BadRequestAlertException;
import com.msst.platform.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Subtitle.
 */
@RestController
@RequestMapping("/api")
public class SubtitleResource {

    private final Logger log = LoggerFactory.getLogger(SubtitleResource.class);

    private static final String ENTITY_NAME = "subtitle";

    private final SubtitleService subtitleService;
    private final SubtitleFacade subtitleFacade;

    public SubtitleResource(SubtitleService subtitleService, SubtitleFacade subtitleFacade) {
        this.subtitleService = subtitleService;
        this.subtitleFacade = subtitleFacade;
    }

    /**
     * POST  /subtitles : Create a new subtitle.
     *
     * @param subtitle the subtitle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subtitle, or with status 400 (Bad Request) if the subtitle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subtitles")
    public ResponseEntity<Subtitle> createSubtitle(@RequestBody Subtitle subtitle) throws URISyntaxException {
        log.debug("REST request to create Subtitle : {}", subtitle);
        if (subtitle.getId() != null) {
            throw new BadRequestAlertException("A new subtitle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Subtitle result = subtitleService.create(subtitle);
        return ResponseEntity.created(new URI("/api/subtitles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subtitles : Updates an existing subtitle.
     *
     * @param subtitle the subtitle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subtitle,
     * or with status 400 (Bad Request) if the subtitle is not valid,
     * or with status 500 (Internal Server Error) if the subtitle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subtitles")
    public ResponseEntity<Subtitle> updateSubtitle(@RequestBody Subtitle subtitle) throws URISyntaxException {
        log.debug("REST request to update Subtitle : {}", subtitle);
        if (subtitle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Subtitle result = subtitleService.create(subtitle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subtitle.getId().toString()))
            .body(result);
    }

  @PostMapping("/subtitles/translated/{id}/new")
  public ResponseEntity<Subtitle> startTranslate(@PathVariable String id, @RequestBody StartTranslateSubtitleTranslateInfo subtitleTranslateInfo) {
    log.debug("REST request to start translating Subtitle with version : {} and language {}"
      , subtitleTranslateInfo.getParentVersion(), subtitleTranslateInfo.getParentLanguage());

    subtitleFacade.startTranslateSubtitle(subtitleTranslateInfo, id);
    return ResponseEntity.noContent().build();
  }

    /**
     * GET  /subtitles : get all the subtitles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subtitles in body
     */
    @GetMapping("/subtitles")
    public List<Subtitle> getAllSubtitles() {
        log.debug("REST request to get all Subtitles");
        return subtitleService.findAll();
    }

    /**
     * GET  /subtitles/:id : get the "id" subtitle.
     *
     * @param id the id of the subtitle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subtitle, or with status 404 (Not Found)
     */
    @GetMapping("/subtitles/{id}")
    public ResponseEntity<Subtitle> getSubtitle(@PathVariable String id) {
        log.debug("REST request to get Subtitle : {}", id);
        Optional<Subtitle> subtitle = subtitleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subtitle);
    }

    /**
     * DELETE  /subtitles/:id : delete the "id" subtitle.
     *
     * @param id the id of the subtitle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subtitles/{id}")
    public ResponseEntity<Void> deleteSubtitle(@PathVariable String id) {
        log.debug("REST request to delete Subtitle : {}", id);
        subtitleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
