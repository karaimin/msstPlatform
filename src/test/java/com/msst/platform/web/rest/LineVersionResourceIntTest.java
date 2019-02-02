package com.msst.platform.web.rest;

import com.msst.platform.MsstPlatformApp;

import com.msst.platform.domain.LineVersion;
import com.msst.platform.repository.LineVersionRepository;
import com.msst.platform.service.LineVersionService;
import com.msst.platform.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
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
 * Test class for the LineVersionResource REST controller.
 *
 * @see LineVersionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsstPlatformApp.class)
public class LineVersionResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private LineVersionRepository lineVersionRepository;

    @Autowired
    private LineVersionService lineVersionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restLineVersionMockMvc;

    private LineVersion lineVersion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LineVersionResource lineVersionResource = new LineVersionResource(lineVersionService);
        this.restLineVersionMockMvc = MockMvcBuilders.standaloneSetup(lineVersionResource)
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
    public static LineVersion createEntity() {
        LineVersion lineVersion = new LineVersion()
            .version(DEFAULT_VERSION)
            .text(DEFAULT_TEXT);
        return lineVersion;
    }

    @Before
    public void initTest() {
        lineVersionRepository.deleteAll();
        lineVersion = createEntity();
    }

    @Test
    public void createLineVersion() throws Exception {
        int databaseSizeBeforeCreate = lineVersionRepository.findAll().size();

        // Create the LineVersion
        restLineVersionMockMvc.perform(post("/api/line-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineVersion)))
            .andExpect(status().isCreated());

        // Validate the LineVersion in the database
        List<LineVersion> lineVersionList = lineVersionRepository.findAll();
        assertThat(lineVersionList).hasSize(databaseSizeBeforeCreate + 1);
        LineVersion testLineVersion = lineVersionList.get(lineVersionList.size() - 1);
        assertThat(testLineVersion.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testLineVersion.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    public void createLineVersionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineVersionRepository.findAll().size();

        // Create the LineVersion with an existing ID
        lineVersion.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineVersionMockMvc.perform(post("/api/line-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineVersion)))
            .andExpect(status().isBadRequest());

        // Validate the LineVersion in the database
        List<LineVersion> lineVersionList = lineVersionRepository.findAll();
        assertThat(lineVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllLineVersions() throws Exception {
        // Initialize the database
        lineVersionRepository.save(lineVersion);

        // Get all the lineVersionList
        restLineVersionMockMvc.perform(get("/api/line-versions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineVersion.getId())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }
    
    @Test
    public void getLineVersion() throws Exception {
        // Initialize the database
        lineVersionRepository.save(lineVersion);

        // Get the lineVersion
        restLineVersionMockMvc.perform(get("/api/line-versions/{id}", lineVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineVersion.getId()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    public void getNonExistingLineVersion() throws Exception {
        // Get the lineVersion
        restLineVersionMockMvc.perform(get("/api/line-versions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLineVersion() throws Exception {
        // Initialize the database
        lineVersionService.save(lineVersion);

        int databaseSizeBeforeUpdate = lineVersionRepository.findAll().size();

        // Update the lineVersion
        LineVersion updatedLineVersion = lineVersionRepository.findById(lineVersion.getId()).get();
        updatedLineVersion
            .version(UPDATED_VERSION)
            .text(UPDATED_TEXT);

        restLineVersionMockMvc.perform(put("/api/line-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLineVersion)))
            .andExpect(status().isOk());

        // Validate the LineVersion in the database
        List<LineVersion> lineVersionList = lineVersionRepository.findAll();
        assertThat(lineVersionList).hasSize(databaseSizeBeforeUpdate);
        LineVersion testLineVersion = lineVersionList.get(lineVersionList.size() - 1);
        assertThat(testLineVersion.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testLineVersion.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    public void updateNonExistingLineVersion() throws Exception {
        int databaseSizeBeforeUpdate = lineVersionRepository.findAll().size();

        // Create the LineVersion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineVersionMockMvc.perform(put("/api/line-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineVersion)))
            .andExpect(status().isBadRequest());

        // Validate the LineVersion in the database
        List<LineVersion> lineVersionList = lineVersionRepository.findAll();
        assertThat(lineVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteLineVersion() throws Exception {
        // Initialize the database
        lineVersionService.save(lineVersion);

        int databaseSizeBeforeDelete = lineVersionRepository.findAll().size();

        // Delete the lineVersion
        restLineVersionMockMvc.perform(delete("/api/line-versions/{id}", lineVersion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LineVersion> lineVersionList = lineVersionRepository.findAll();
        assertThat(lineVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineVersion.class);
        LineVersion lineVersion1 = new LineVersion();
        lineVersion1.setId("id1");
        LineVersion lineVersion2 = new LineVersion();
        lineVersion2.setId(lineVersion1.getId());
        assertThat(lineVersion1).isEqualTo(lineVersion2);
        lineVersion2.setId("id2");
        assertThat(lineVersion1).isNotEqualTo(lineVersion2);
        lineVersion1.setId(null);
        assertThat(lineVersion1).isNotEqualTo(lineVersion2);
    }
}
