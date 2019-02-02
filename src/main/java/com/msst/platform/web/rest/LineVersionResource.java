package com.msst.platform.web.rest;
import com.msst.platform.domain.LineVersion;
import com.msst.platform.service.LineVersionService;
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
 * REST controller for managing LineVersion.
 */
@RestController
@RequestMapping("/api")
public class LineVersionResource {

    private final Logger log = LoggerFactory.getLogger(LineVersionResource.class);

    private static final String ENTITY_NAME = "lineVersion";

    private final LineVersionService lineVersionService;

    public LineVersionResource(LineVersionService lineVersionService) {
        this.lineVersionService = lineVersionService;
    }

    /**
     * POST  /line-versions : Create a new lineVersion.
     *
     * @param lineVersion the lineVersion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lineVersion, or with status 400 (Bad Request) if the lineVersion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/line-versions")
    public ResponseEntity<LineVersion> createLineVersion(@RequestBody LineVersion lineVersion) throws URISyntaxException {
        log.debug("REST request to save LineVersion : {}", lineVersion);
        if (lineVersion.getId() != null) {
            throw new BadRequestAlertException("A new lineVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineVersion result = lineVersionService.save(lineVersion);
        return ResponseEntity.created(new URI("/api/line-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /line-versions : Updates an existing lineVersion.
     *
     * @param lineVersion the lineVersion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lineVersion,
     * or with status 400 (Bad Request) if the lineVersion is not valid,
     * or with status 500 (Internal Server Error) if the lineVersion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/line-versions")
    public ResponseEntity<LineVersion> updateLineVersion(@RequestBody LineVersion lineVersion) throws URISyntaxException {
        log.debug("REST request to update LineVersion : {}", lineVersion);
        if (lineVersion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LineVersion result = lineVersionService.save(lineVersion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lineVersion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /line-versions : get all the lineVersions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lineVersions in body
     */
    @GetMapping("/line-versions")
    public List<LineVersion> getAllLineVersions() {
        log.debug("REST request to get all LineVersions");
        return lineVersionService.findAll();
    }

    /**
     * GET  /line-versions/:id : get the "id" lineVersion.
     *
     * @param id the id of the lineVersion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lineVersion, or with status 404 (Not Found)
     */
    @GetMapping("/line-versions/{id}")
    public ResponseEntity<LineVersion> getLineVersion(@PathVariable String id) {
        log.debug("REST request to get LineVersion : {}", id);
        Optional<LineVersion> lineVersion = lineVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineVersion);
    }

    /**
     * DELETE  /line-versions/:id : delete the "id" lineVersion.
     *
     * @param id the id of the lineVersion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/line-versions/{id}")
    public ResponseEntity<Void> deleteLineVersion(@PathVariable String id) {
        log.debug("REST request to delete LineVersion : {}", id);
        lineVersionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
