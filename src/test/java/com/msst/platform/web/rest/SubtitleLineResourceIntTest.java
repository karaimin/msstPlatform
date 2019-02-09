package com.msst.platform.web.rest;

import com.msst.platform.MsstPlatformApp;

import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.repository.SubtitleLineRepository;
import com.msst.platform.service.SubtitleLineService;
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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.msst.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SubtitleLineResource REST controller.
 *
 * @see SubtitleLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsstPlatformApp.class)
@Ignore
public class SubtitleLineResourceIntTest {

    private static final Duration DEFAULT_START_TIME = Duration.of(10, ChronoUnit.SECONDS);
    private static final Duration UPDATED_START_TIME = Duration.of(12, ChronoUnit.SECONDS);

    private static final Duration DEFAULT_END_TIME = Duration.of(20, ChronoUnit.SECONDS);
    private static final Duration UPDATED_END_TIME = Duration.of(22, ChronoUnit.SECONDS);

    @Autowired
    private SubtitleLineRepository subtitleLineRepository;

    @Autowired
    private SubtitleLineService subtitleLineService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSubtitleLineMockMvc;

    private SubtitleLine subtitleLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubtitleLineResource subtitleLineResource = new SubtitleLineResource(subtitleLineService);
        this.restSubtitleLineMockMvc = MockMvcBuilders.standaloneSetup(subtitleLineResource)
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
    public static SubtitleLine createEntity() {
        SubtitleLine subtitleLine = new SubtitleLine()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return subtitleLine;
    }

    @Before
    public void initTest() {
        subtitleLineRepository.deleteAll();
        subtitleLine = createEntity();
    }

    @Test
    public void createSubtitleLine() throws Exception {
        int databaseSizeBeforeCreate = subtitleLineRepository.findAll().size();

        // Create the SubtitleLine
        restSubtitleLineMockMvc.perform(post("/api/subtitle-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subtitleLine)))
            .andExpect(status().isCreated());

        // Validate the SubtitleLine in the database
        List<SubtitleLine> subtitleLineList = subtitleLineRepository.findAll();
        assertThat(subtitleLineList).hasSize(databaseSizeBeforeCreate + 1);
        SubtitleLine testSubtitleLine = subtitleLineList.get(subtitleLineList.size() - 1);
        assertThat(testSubtitleLine.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testSubtitleLine.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    public void createSubtitleLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subtitleLineRepository.findAll().size();

        // Create the SubtitleLine with an existing ID
        subtitleLine.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubtitleLineMockMvc.perform(post("/api/subtitle-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subtitleLine)))
            .andExpect(status().isBadRequest());

        // Validate the SubtitleLine in the database
        List<SubtitleLine> subtitleLineList = subtitleLineRepository.findAll();
        assertThat(subtitleLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllSubtitleLines() throws Exception {
        // Initialize the database
        subtitleLineRepository.save(subtitleLine);

        // Get all the subtitleLineList
        restSubtitleLineMockMvc.perform(get("/api/subtitle-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subtitleLine.getId())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }
    
    @Test
    public void getSubtitleLine() throws Exception {
        // Initialize the database
        subtitleLineRepository.save(subtitleLine);

        // Get the subtitleLine
        restSubtitleLineMockMvc.perform(get("/api/subtitle-lines/{id}", subtitleLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subtitleLine.getId()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    public void getNonExistingSubtitleLine() throws Exception {
        // Get the subtitleLine
        restSubtitleLineMockMvc.perform(get("/api/subtitle-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSubtitleLine() throws Exception {
        // Initialize the database
        subtitleLineService.save(subtitleLine);

        int databaseSizeBeforeUpdate = subtitleLineRepository.findAll().size();

        // Update the subtitleLine
        SubtitleLine updatedSubtitleLine = subtitleLineRepository.findById(subtitleLine.getId()).get();
        updatedSubtitleLine
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);

        restSubtitleLineMockMvc.perform(put("/api/subtitle-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubtitleLine)))
            .andExpect(status().isOk());

        // Validate the SubtitleLine in the database
        List<SubtitleLine> subtitleLineList = subtitleLineRepository.findAll();
        assertThat(subtitleLineList).hasSize(databaseSizeBeforeUpdate);
        SubtitleLine testSubtitleLine = subtitleLineList.get(subtitleLineList.size() - 1);
        assertThat(testSubtitleLine.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSubtitleLine.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    public void updateNonExistingSubtitleLine() throws Exception {
        int databaseSizeBeforeUpdate = subtitleLineRepository.findAll().size();

        // Create the SubtitleLine

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubtitleLineMockMvc.perform(put("/api/subtitle-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subtitleLine)))
            .andExpect(status().isBadRequest());

        // Validate the SubtitleLine in the database
        List<SubtitleLine> subtitleLineList = subtitleLineRepository.findAll();
        assertThat(subtitleLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSubtitleLine() throws Exception {
        // Initialize the database
        subtitleLineService.save(subtitleLine);

        int databaseSizeBeforeDelete = subtitleLineRepository.findAll().size();

        // Delete the subtitleLine
        restSubtitleLineMockMvc.perform(delete("/api/subtitle-lines/{id}", subtitleLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SubtitleLine> subtitleLineList = subtitleLineRepository.findAll();
        assertThat(subtitleLineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubtitleLine.class);
        SubtitleLine subtitleLine1 = new SubtitleLine();
        subtitleLine1.setId("id1");
        SubtitleLine subtitleLine2 = new SubtitleLine();
        subtitleLine2.setId(subtitleLine1.getId());
        assertThat(subtitleLine1).isEqualTo(subtitleLine2);
        subtitleLine2.setId("id2");
        assertThat(subtitleLine1).isNotEqualTo(subtitleLine2);
        subtitleLine1.setId(null);
        assertThat(subtitleLine1).isNotEqualTo(subtitleLine2);
    }
}
