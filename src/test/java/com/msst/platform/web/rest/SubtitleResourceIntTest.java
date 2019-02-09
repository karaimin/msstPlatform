package com.msst.platform.web.rest;

import com.msst.platform.MsstPlatformApp;

import com.msst.platform.domain.Subtitle;
import com.msst.platform.facade.SubtitleFacade;
import com.msst.platform.repository.SubtitleRepository;
import com.msst.platform.service.SubtitleService;
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
 * Test class for the SubtitleResource REST controller.
 *
 * @see SubtitleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsstPlatformApp.class)
@Ignore
public class SubtitleResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    @Autowired
    private SubtitleRepository subtitleRepository;

    @Autowired
    private SubtitleService subtitleService;

    @Autowired
    private SubtitleFacade subtitleFacade;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSubtitleMockMvc;

    private Subtitle subtitle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubtitleResource subtitleResource = new SubtitleResource(subtitleService, subtitleFacade);
        this.restSubtitleMockMvc = MockMvcBuilders.standaloneSetup(subtitleResource)
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
    public static Subtitle createEntity() {
        Subtitle subtitle = new Subtitle()
            .version(DEFAULT_VERSION);
        return subtitle;
    }

    @Before
    public void initTest() {
        subtitleRepository.deleteAll();
        subtitle = createEntity();
    }

    @Test
    public void createSubtitle() throws Exception {
        int databaseSizeBeforeCreate = subtitleRepository.findAll().size();

        // Create the Subtitle
        restSubtitleMockMvc.perform(post("/api/subtitles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subtitle)))
            .andExpect(status().isCreated());

        // Validate the Subtitle in the database
        List<Subtitle> subtitleList = subtitleRepository.findAll();
        assertThat(subtitleList).hasSize(databaseSizeBeforeCreate + 1);
        Subtitle testSubtitle = subtitleList.get(subtitleList.size() - 1);
        assertThat(testSubtitle.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    public void createSubtitleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subtitleRepository.findAll().size();

        // Create the Subtitle with an existing ID
        subtitle.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubtitleMockMvc.perform(post("/api/subtitles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subtitle)))
            .andExpect(status().isBadRequest());

        // Validate the Subtitle in the database
        List<Subtitle> subtitleList = subtitleRepository.findAll();
        assertThat(subtitleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllSubtitles() throws Exception {
        // Initialize the database
        subtitleRepository.save(subtitle);

        // Get all the subtitleList
        restSubtitleMockMvc.perform(get("/api/subtitles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subtitle.getId())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())));
    }
    
    @Test
    public void getSubtitle() throws Exception {
        // Initialize the database
        subtitleRepository.save(subtitle);

        // Get the subtitle
        restSubtitleMockMvc.perform(get("/api/subtitles/{id}", subtitle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subtitle.getId()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()));
    }

    @Test
    public void getNonExistingSubtitle() throws Exception {
        // Get the subtitle
        restSubtitleMockMvc.perform(get("/api/subtitles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSubtitle() throws Exception {
        // Initialize the database
        subtitleService.create(subtitle);

        int databaseSizeBeforeUpdate = subtitleRepository.findAll().size();

        // Update the subtitle
        Subtitle updatedSubtitle = subtitleRepository.findById(subtitle.getId()).get();
        updatedSubtitle
            .version(UPDATED_VERSION);

        restSubtitleMockMvc.perform(put("/api/subtitles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubtitle)))
            .andExpect(status().isOk());

        // Validate the Subtitle in the database
        List<Subtitle> subtitleList = subtitleRepository.findAll();
        assertThat(subtitleList).hasSize(databaseSizeBeforeUpdate);
        Subtitle testSubtitle = subtitleList.get(subtitleList.size() - 1);
        assertThat(testSubtitle.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    public void updateNonExistingSubtitle() throws Exception {
        int databaseSizeBeforeUpdate = subtitleRepository.findAll().size();

        // Create the Subtitle

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubtitleMockMvc.perform(put("/api/subtitles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subtitle)))
            .andExpect(status().isBadRequest());

        // Validate the Subtitle in the database
        List<Subtitle> subtitleList = subtitleRepository.findAll();
        assertThat(subtitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSubtitle() throws Exception {
        // Initialize the database
        subtitleService.create(subtitle);

        int databaseSizeBeforeDelete = subtitleRepository.findAll().size();

        // Delete the subtitle
        restSubtitleMockMvc.perform(delete("/api/subtitles/{id}", subtitle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Subtitle> subtitleList = subtitleRepository.findAll();
        assertThat(subtitleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subtitle.class);
        Subtitle subtitle1 = new Subtitle();
        subtitle1.setId("id1");
        Subtitle subtitle2 = new Subtitle();
        subtitle2.setId(subtitle1.getId());
        assertThat(subtitle1).isEqualTo(subtitle2);
        subtitle2.setId("id2");
        assertThat(subtitle1).isNotEqualTo(subtitle2);
        subtitle1.setId(null);
        assertThat(subtitle1).isNotEqualTo(subtitle2);
    }
}
