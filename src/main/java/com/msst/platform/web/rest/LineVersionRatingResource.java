package com.msst.platform.web.rest;
import com.msst.platform.domain.LineVersionRating;
import com.msst.platform.service.LineVersionRatingService;
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
 * REST controller for managing LineVersionRating.
 */
@RestController
@RequestMapping("/api")
public class LineVersionRatingResource {

    private final Logger log = LoggerFactory.getLogger(LineVersionRatingResource.class);

    private static final String ENTITY_NAME = "lineVersionRating";

    private final LineVersionRatingService lineVersionRatingService;

    public LineVersionRatingResource(LineVersionRatingService lineVersionRatingService) {
        this.lineVersionRatingService = lineVersionRatingService;
    }

    /**
     * POST  /line-version-ratings : Create a new lineVersionRating.
     *
     * @param lineVersionRating the lineVersionRating to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lineVersionRating, or with status 400 (Bad Request) if the lineVersionRating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/line-version-ratings")
    public ResponseEntity<LineVersionRating> createLineVersionRating(@RequestBody LineVersionRating lineVersionRating) throws URISyntaxException {
        log.debug("REST request to save LineVersionRating : {}", lineVersionRating);
        if (lineVersionRating.getId() != null) {
            throw new BadRequestAlertException("A new lineVersionRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineVersionRating result = lineVersionRatingService.save(lineVersionRating);
        return ResponseEntity.created(new URI("/api/line-version-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /line-version-ratings : Updates an existing lineVersionRating.
     *
     * @param lineVersionRating the lineVersionRating to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lineVersionRating,
     * or with status 400 (Bad Request) if the lineVersionRating is not valid,
     * or with status 500 (Internal Server Error) if the lineVersionRating couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/line-version-ratings")
    public ResponseEntity<LineVersionRating> updateLineVersionRating(@RequestBody LineVersionRating lineVersionRating) throws URISyntaxException {
        log.debug("REST request to update LineVersionRating : {}", lineVersionRating);
        if (lineVersionRating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LineVersionRating result = lineVersionRatingService.save(lineVersionRating);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lineVersionRating.getId().toString()))
            .body(result);
    }

    /**
     * GET  /line-version-ratings : get all the lineVersionRatings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lineVersionRatings in body
     */
    @GetMapping("/line-version-ratings")
    public List<LineVersionRating> getAllLineVersionRatings() {
        log.debug("REST request to get all LineVersionRatings");
        return lineVersionRatingService.findAll();
    }

    /**
     * GET  /line-version-ratings/:id : get the "id" lineVersionRating.
     *
     * @param id the id of the lineVersionRating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lineVersionRating, or with status 404 (Not Found)
     */
    @GetMapping("/line-version-ratings/{id}")
    public ResponseEntity<LineVersionRating> getLineVersionRating(@PathVariable String id) {
        log.debug("REST request to get LineVersionRating : {}", id);
        Optional<LineVersionRating> lineVersionRating = lineVersionRatingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineVersionRating);
    }

    /**
     * DELETE  /line-version-ratings/:id : delete the "id" lineVersionRating.
     *
     * @param id the id of the lineVersionRating to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/line-version-ratings/{id}")
    public ResponseEntity<Void> deleteLineVersionRating(@PathVariable String id) {
        log.debug("REST request to delete LineVersionRating : {}", id);
        lineVersionRatingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
