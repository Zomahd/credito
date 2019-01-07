package com.sistema.credito.web.rest;

import com.sistema.credito.CreditoApp;

import com.sistema.credito.domain.HistorialDeCredito;
import com.sistema.credito.repository.HistorialDeCreditoRepository;
import com.sistema.credito.service.HistorialDeCreditoService;
import com.sistema.credito.web.rest.errors.ExceptionTranslator;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.sistema.credito.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HistorialDeCreditoResource REST controller.
 *
 * @see HistorialDeCreditoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CreditoApp.class)
public class HistorialDeCreditoResourceIntTest {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private HistorialDeCreditoRepository historialDeCreditoRepository;

    @Autowired
    private HistorialDeCreditoService historialDeCreditoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restHistorialDeCreditoMockMvc;

    private HistorialDeCredito historialDeCredito;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistorialDeCreditoResource historialDeCreditoResource = new HistorialDeCreditoResource(historialDeCreditoService);
        this.restHistorialDeCreditoMockMvc = MockMvcBuilders.standaloneSetup(historialDeCreditoResource)
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
    public static HistorialDeCredito createEntity(EntityManager em) {
        HistorialDeCredito historialDeCredito = new HistorialDeCredito()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return historialDeCredito;
    }

    @Before
    public void initTest() {
        historialDeCredito = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistorialDeCredito() throws Exception {
        int databaseSizeBeforeCreate = historialDeCreditoRepository.findAll().size();

        // Create the HistorialDeCredito
        restHistorialDeCreditoMockMvc.perform(post("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCredito)))
            .andExpect(status().isCreated());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeCreate + 1);
        HistorialDeCredito testHistorialDeCredito = historialDeCreditoList.get(historialDeCreditoList.size() - 1);
        assertThat(testHistorialDeCredito.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testHistorialDeCredito.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createHistorialDeCreditoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historialDeCreditoRepository.findAll().size();

        // Create the HistorialDeCredito with an existing ID
        historialDeCredito.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistorialDeCreditoMockMvc.perform(post("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCredito)))
            .andExpect(status().isBadRequest());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHistorialDeCreditos() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        // Get all the historialDeCreditoList
        restHistorialDeCreditoMockMvc.perform(get("/api/historial-de-creditos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historialDeCredito.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getHistorialDeCredito() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        // Get the historialDeCredito
        restHistorialDeCreditoMockMvc.perform(get("/api/historial-de-creditos/{id}", historialDeCredito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(historialDeCredito.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHistorialDeCredito() throws Exception {
        // Get the historialDeCredito
        restHistorialDeCreditoMockMvc.perform(get("/api/historial-de-creditos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistorialDeCredito() throws Exception {
        // Initialize the database
        historialDeCreditoService.save(historialDeCredito);

        int databaseSizeBeforeUpdate = historialDeCreditoRepository.findAll().size();

        // Update the historialDeCredito
        HistorialDeCredito updatedHistorialDeCredito = historialDeCreditoRepository.findById(historialDeCredito.getId()).get();
        // Disconnect from session so that the updates on updatedHistorialDeCredito are not directly saved in db
        em.detach(updatedHistorialDeCredito);
        updatedHistorialDeCredito
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restHistorialDeCreditoMockMvc.perform(put("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHistorialDeCredito)))
            .andExpect(status().isOk());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeUpdate);
        HistorialDeCredito testHistorialDeCredito = historialDeCreditoList.get(historialDeCreditoList.size() - 1);
        assertThat(testHistorialDeCredito.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHistorialDeCredito.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingHistorialDeCredito() throws Exception {
        int databaseSizeBeforeUpdate = historialDeCreditoRepository.findAll().size();

        // Create the HistorialDeCredito

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialDeCreditoMockMvc.perform(put("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCredito)))
            .andExpect(status().isBadRequest());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHistorialDeCredito() throws Exception {
        // Initialize the database
        historialDeCreditoService.save(historialDeCredito);

        int databaseSizeBeforeDelete = historialDeCreditoRepository.findAll().size();

        // Get the historialDeCredito
        restHistorialDeCreditoMockMvc.perform(delete("/api/historial-de-creditos/{id}", historialDeCredito.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistorialDeCredito.class);
        HistorialDeCredito historialDeCredito1 = new HistorialDeCredito();
        historialDeCredito1.setId(1L);
        HistorialDeCredito historialDeCredito2 = new HistorialDeCredito();
        historialDeCredito2.setId(historialDeCredito1.getId());
        assertThat(historialDeCredito1).isEqualTo(historialDeCredito2);
        historialDeCredito2.setId(2L);
        assertThat(historialDeCredito1).isNotEqualTo(historialDeCredito2);
        historialDeCredito1.setId(null);
        assertThat(historialDeCredito1).isNotEqualTo(historialDeCredito2);
    }
}
