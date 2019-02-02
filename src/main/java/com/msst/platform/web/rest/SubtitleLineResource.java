package com.msst.platform.web.rest;
import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.service.SubtitleLineService;
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
 * REST controller for managing SubtitleLine.
 */
@RestController
@RequestMapping("/api")
public class SubtitleLineResource {

    private final Logger log = LoggerFactory.getLogger(SubtitleLineResource.class);

    private static final String ENTITY_NAME = "subtitleLine";

    private final SubtitleLineService subtitleLineService;

    public SubtitleLineResource(SubtitleLineService subtitleLineService) {
        this.subtitleLineService = subtitleLineService;
    }

    /**
     * POST  /subtitle-lines : Create a new subtitleLine.
     *
     * @param subtitleLine the subtitleLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subtitleLine, or with status 400 (Bad Request) if the subtitleLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subtitle-lines")
    public ResponseEntity<SubtitleLine> createSubtitleLine(@RequestBody SubtitleLine subtitleLine) throws URISyntaxException {
        log.debug("REST request to save SubtitleLine : {}", subtitleLine);
        if (subtitleLine.getId() != null) {
            throw new BadRequestAlertException("A new subtitleLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubtitleLine result = subtitleLineService.save(subtitleLine);
        return ResponseEntity.created(new URI("/api/subtitle-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subtitle-lines : Updates an existing subtitleLine.
     *
     * @param subtitleLine the subtitleLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subtitleLine,
     * or with status 400 (Bad Request) if the subtitleLine is not valid,
     * or with status 500 (Internal Server Error) if the subtitleLine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subtitle-lines")
    public ResponseEntity<SubtitleLine> updateSubtitleLine(@RequestBody SubtitleLine subtitleLine) throws URISyntaxException {
        log.debug("REST request to update SubtitleLine : {}", subtitleLine);
        if (subtitleLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubtitleLine result = subtitleLineService.save(subtitleLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subtitleLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subtitle-lines : get all the subtitleLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subtitleLines in body
     */
    @GetMapping("/subtitle-lines")
    public List<SubtitleLine> getAllSubtitleLines() {
        log.debug("REST request to get all SubtitleLines");
        return subtitleLineService.findAll();
    }

    /**
     * GET  /subtitle-lines/:id : get the "id" subtitleLine.
     *
     * @param id the id of the subtitleLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subtitleLine, or with status 404 (Not Found)
     */
    @GetMapping("/subtitle-lines/{id}")
    public ResponseEntity<SubtitleLine> getSubtitleLine(@PathVariable String id) {
        log.debug("REST request to get SubtitleLine : {}", id);
        Optional<SubtitleLine> subtitleLine = subtitleLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subtitleLine);
    }

    /**
     * DELETE  /subtitle-lines/:id : delete the "id" subtitleLine.
     *
     * @param id the id of the subtitleLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subtitle-lines/{id}")
    public ResponseEntity<Void> deleteSubtitleLine(@PathVariable String id) {
        log.debug("REST request to delete SubtitleLine : {}", id);
        subtitleLineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
