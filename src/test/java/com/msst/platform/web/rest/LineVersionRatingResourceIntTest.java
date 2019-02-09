package com.msst.platform.web.rest;

import com.msst.platform.MsstPlatformApp;

import com.msst.platform.domain.LineVersionRating;
import com.msst.platform.repository.LineVersionRatingRepository;
import com.msst.platform.service.LineVersionRatingService;
import com.msst.platform.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.List;


import static com.msst.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LineVersionRatingResource REST controller.
 *
 * @see LineVersionRatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsstPlatformApp.class)
@Ignore
public class LineVersionRatingResourceIntTest {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private LineVersionRatingRepository lineVersionRatingRepository;

    @Autowired
    private LineVersionRatingService lineVersionRatingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restLineVersionRatingMockMvc;

    private LineVersionRating lineVersionRating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LineVersionRatingResource lineVersionRatingResource = new LineVersionRatingResource(lineVersionRatingService);
        this.restLineVersionRatingMockMvc = MockMvcBuilders.standaloneSetup(lineVersionRatingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineVersionRating createEntity() {
        LineVersionRating lineVersionRating = new LineVersionRating()
            .rating(DEFAULT_RATING)
            .comment(DEFAULT_COMMENT);
        return lineVersionRating;
    }

    @Before
    public void initTest() {
        lineVersionRatingRepository.deleteAll();
        lineVersionRating = createEntity();
    }

    @Test
    public void createLineVersionRating() throws Exception {
        int databaseSizeBeforeCreate = lineVersionRatingRepository.findAll().size();

        // Create the LineVersionRating
        restLineVersionRatingMockMvc.perform(post("/api/line-version-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineVersionRating)))
            .andExpect(status().isCreated());

        // Validate the LineVersionRating in the database
        List<LineVersionRating> lineVersionRatingList = lineVersionRatingRepository.findAll();
        assertThat(lineVersionRatingList).hasSize(databaseSizeBeforeCreate + 1);
        LineVersionRating testLineVersionRating = lineVersionRatingList.get(lineVersionRatingList.size() - 1);
        assertThat(testLineVersionRating.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testLineVersionRating.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    public void createLineVersionRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineVersionRatingRepository.findAll().size();

        // Create the LineVersionRating with an existing ID
        lineVersionRating.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineVersionRatingMockMvc.perform(post("/api/line-version-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineVersionRating)))
            .andExpect(status().isBadRequest());

        // Validate the LineVersionRating in the database
        List<LineVersionRating> lineVersionRatingList = lineVersionRatingRepository.findAll();
        assertThat(lineVersionRatingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllLineVersionRatings() throws Exception {
        // Initialize the database
        lineVersionRatingRepository.save(lineVersionRating);

        // Get all the lineVersionRatingList
        restLineVersionRatingMockMvc.perform(get("/api/line-version-ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineVersionRating.getId())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @Test
    public void getLineVersionRating() throws Exception {
        // Initialize the database
        lineVersionRatingRepository.save(lineVersionRating);

        // Get the lineVersionRating
        restLineVersionRatingMockMvc.perform(get("/api/line-version-ratings/{id}", lineVersionRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineVersionRating.getId()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    public void getNonExistingLineVersionRating() throws Exception {
        // Get the lineVersionRating
        restLineVersionRatingMockMvc.perform(get("/api/line-version-ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLineVersionRating() throws Exception {
        // Initialize the database
        lineVersionRatingService.save(lineVersionRating);

        int databaseSizeBeforeUpdate = lineVersionRatingRepository.findAll().size();

        // Update the lineVersionRating
        LineVersionRating updatedLineVersionRating = lineVersionRatingRepository.findById(lineVersionRating.getId()).get();
        updatedLineVersionRating
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT);

        restLineVersionRatingMockMvc.perform(put("/api/line-version-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLineVersionRating)))
            .andExpect(status().isOk());

        // Validate the LineVersionRating in the database
        List<LineVersionRating> lineVersionRatingList = lineVersionRatingRepository.findAll();
        assertThat(lineVersionRatingList).hasSize(databaseSizeBeforeUpdate);
        LineVersionRating testLineVersionRating = lineVersionRatingList.get(lineVersionRatingList.size() - 1);
        assertThat(testLineVersionRating.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testLineVersionRating.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    public void updateNonExistingLineVersionRating() throws Exception {
        int databaseSizeBeforeUpdate = lineVersionRatingRepository.findAll().size();

        // Create the LineVersionRating

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineVersionRatingMockMvc.perform(put("/api/line-version-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineVersionRating)))
            .andExpect(status().isBadRequest());

        // Validate the LineVersionRating in the database
        List<LineVersionRating> lineVersionRatingList = lineVersionRatingRepository.findAll();
        assertThat(lineVersionRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteLineVersionRating() throws Exception {
        // Initialize the database
        lineVersionRatingService.save(lineVersionRating);

        int databaseSizeBeforeDelete = lineVersionRatingRepository.findAll().size();

        // Delete the lineVersionRating
        restLineVersionRatingMockMvc.perform(delete("/api/line-version-ratings/{id}", lineVersionRating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LineVersionRating> lineVersionRatingList = lineVersionRatingRepository.findAll();
        assertThat(lineVersionRatingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineVersionRating.class);
        LineVersionRating lineVersionRating1 = new LineVersionRating();
        lineVersionRating1.setId("id1");
        LineVersionRating lineVersionRating2 = new LineVersionRating();
        lineVersionRating2.setId(lineVersionRating1.getId());
        assertThat(lineVersionRating1).isEqualTo(lineVersionRating2);
        lineVersionRating2.setId("id2");
        assertThat(lineVersionRating1).isNotEqualTo(lineVersionRating2);
        lineVersionRating1.setId(null);
        assertThat(lineVersionRating1).isNotEqualTo(lineVersionRating2);
    }
}
