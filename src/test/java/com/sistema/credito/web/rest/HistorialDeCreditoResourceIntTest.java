package com.sistema.credito.web.rest;

import com.sistema.credito.CreditoApp;

import com.sistema.credito.domain.HistorialDeCredito;
import com.sistema.credito.domain.Cliente;
import com.sistema.credito.domain.Factura;
import com.sistema.credito.domain.Abono;
import com.sistema.credito.repository.HistorialDeCreditoRepository;
import com.sistema.credito.service.HistorialDeCreditoService;
import com.sistema.credito.service.dto.HistorialDeCreditoDTO;
import com.sistema.credito.service.mapper.HistorialDeCreditoMapper;
import com.sistema.credito.web.rest.errors.ExceptionTranslator;
import com.sistema.credito.service.dto.HistorialDeCreditoCriteria;
import com.sistema.credito.service.HistorialDeCreditoQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
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

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private HistorialDeCreditoRepository historialDeCreditoRepository;

    @Autowired
    private HistorialDeCreditoMapper historialDeCreditoMapper;

    @Autowired
    private HistorialDeCreditoService historialDeCreditoService;

    @Autowired
    private HistorialDeCreditoQueryService historialDeCreditoQueryService;

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
        final HistorialDeCreditoResource historialDeCreditoResource = new HistorialDeCreditoResource(historialDeCreditoService, historialDeCreditoQueryService);
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
            .fecha(DEFAULT_FECHA);
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
        HistorialDeCreditoDTO historialDeCreditoDTO = historialDeCreditoMapper.toDto(historialDeCredito);
        restHistorialDeCreditoMockMvc.perform(post("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCreditoDTO)))
            .andExpect(status().isCreated());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeCreate + 1);
        HistorialDeCredito testHistorialDeCredito = historialDeCreditoList.get(historialDeCreditoList.size() - 1);
        assertThat(testHistorialDeCredito.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void createHistorialDeCreditoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historialDeCreditoRepository.findAll().size();

        // Create the HistorialDeCredito with an existing ID
        historialDeCredito.setId(1L);
        HistorialDeCreditoDTO historialDeCreditoDTO = historialDeCreditoMapper.toDto(historialDeCredito);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistorialDeCreditoMockMvc.perform(post("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCreditoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = historialDeCreditoRepository.findAll().size();
        // set the field null
        historialDeCredito.setFecha(null);

        // Create the HistorialDeCredito, which fails.
        HistorialDeCreditoDTO historialDeCreditoDTO = historialDeCreditoMapper.toDto(historialDeCredito);

        restHistorialDeCreditoMockMvc.perform(post("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCreditoDTO)))
            .andExpect(status().isBadRequest());

        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
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
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    public void getAllHistorialDeCreditosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        // Get all the historialDeCreditoList where fecha equals to DEFAULT_FECHA
        defaultHistorialDeCreditoShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the historialDeCreditoList where fecha equals to UPDATED_FECHA
        defaultHistorialDeCreditoShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllHistorialDeCreditosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        // Get all the historialDeCreditoList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultHistorialDeCreditoShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the historialDeCreditoList where fecha equals to UPDATED_FECHA
        defaultHistorialDeCreditoShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllHistorialDeCreditosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        // Get all the historialDeCreditoList where fecha is not null
        defaultHistorialDeCreditoShouldBeFound("fecha.specified=true");

        // Get all the historialDeCreditoList where fecha is null
        defaultHistorialDeCreditoShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistorialDeCreditosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        // Get all the historialDeCreditoList where fecha greater than or equals to DEFAULT_FECHA
        defaultHistorialDeCreditoShouldBeFound("fecha.greaterOrEqualThan=" + DEFAULT_FECHA);

        // Get all the historialDeCreditoList where fecha greater than or equals to UPDATED_FECHA
        defaultHistorialDeCreditoShouldNotBeFound("fecha.greaterOrEqualThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllHistorialDeCreditosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        // Get all the historialDeCreditoList where fecha less than or equals to DEFAULT_FECHA
        defaultHistorialDeCreditoShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the historialDeCreditoList where fecha less than or equals to UPDATED_FECHA
        defaultHistorialDeCreditoShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }


    @Test
    @Transactional
    public void getAllHistorialDeCreditosByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        Cliente cliente = ClienteResourceIntTest.createEntity(em);
        em.persist(cliente);
        em.flush();
        historialDeCredito.setCliente(cliente);
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);
        Long clienteId = cliente.getId();

        // Get all the historialDeCreditoList where cliente equals to clienteId
        defaultHistorialDeCreditoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the historialDeCreditoList where cliente equals to clienteId + 1
        defaultHistorialDeCreditoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }


    @Test
    @Transactional
    public void getAllHistorialDeCreditosByFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        Factura factura = FacturaResourceIntTest.createEntity(em);
        em.persist(factura);
        em.flush();
        historialDeCredito.setFactura(factura);
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);
        Long facturaId = factura.getId();

        // Get all the historialDeCreditoList where factura equals to facturaId
        defaultHistorialDeCreditoShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the historialDeCreditoList where factura equals to facturaId + 1
        defaultHistorialDeCreditoShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }


    @Test
    @Transactional
    public void getAllHistorialDeCreditosByAbonoIsEqualToSomething() throws Exception {
        // Initialize the database
        Abono abono = AbonoResourceIntTest.createEntity(em);
        em.persist(abono);
        em.flush();
        historialDeCredito.setAbono(abono);
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);
        Long abonoId = abono.getId();

        // Get all the historialDeCreditoList where abono equals to abonoId
        defaultHistorialDeCreditoShouldBeFound("abonoId.equals=" + abonoId);

        // Get all the historialDeCreditoList where abono equals to abonoId + 1
        defaultHistorialDeCreditoShouldNotBeFound("abonoId.equals=" + (abonoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHistorialDeCreditoShouldBeFound(String filter) throws Exception {
        restHistorialDeCreditoMockMvc.perform(get("/api/historial-de-creditos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historialDeCredito.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));

        // Check, that the count call also returns 1
        restHistorialDeCreditoMockMvc.perform(get("/api/historial-de-creditos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHistorialDeCreditoShouldNotBeFound(String filter) throws Exception {
        restHistorialDeCreditoMockMvc.perform(get("/api/historial-de-creditos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistorialDeCreditoMockMvc.perform(get("/api/historial-de-creditos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

        int databaseSizeBeforeUpdate = historialDeCreditoRepository.findAll().size();

        // Update the historialDeCredito
        HistorialDeCredito updatedHistorialDeCredito = historialDeCreditoRepository.findById(historialDeCredito.getId()).get();
        // Disconnect from session so that the updates on updatedHistorialDeCredito are not directly saved in db
        em.detach(updatedHistorialDeCredito);
        updatedHistorialDeCredito
            .fecha(UPDATED_FECHA);
        HistorialDeCreditoDTO historialDeCreditoDTO = historialDeCreditoMapper.toDto(updatedHistorialDeCredito);

        restHistorialDeCreditoMockMvc.perform(put("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCreditoDTO)))
            .andExpect(status().isOk());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeUpdate);
        HistorialDeCredito testHistorialDeCredito = historialDeCreditoList.get(historialDeCreditoList.size() - 1);
        assertThat(testHistorialDeCredito.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void updateNonExistingHistorialDeCredito() throws Exception {
        int databaseSizeBeforeUpdate = historialDeCreditoRepository.findAll().size();

        // Create the HistorialDeCredito
        HistorialDeCreditoDTO historialDeCreditoDTO = historialDeCreditoMapper.toDto(historialDeCredito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialDeCreditoMockMvc.perform(put("/api/historial-de-creditos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historialDeCreditoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistorialDeCredito in the database
        List<HistorialDeCredito> historialDeCreditoList = historialDeCreditoRepository.findAll();
        assertThat(historialDeCreditoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHistorialDeCredito() throws Exception {
        // Initialize the database
        historialDeCreditoRepository.saveAndFlush(historialDeCredito);

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

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistorialDeCreditoDTO.class);
        HistorialDeCreditoDTO historialDeCreditoDTO1 = new HistorialDeCreditoDTO();
        historialDeCreditoDTO1.setId(1L);
        HistorialDeCreditoDTO historialDeCreditoDTO2 = new HistorialDeCreditoDTO();
        assertThat(historialDeCreditoDTO1).isNotEqualTo(historialDeCreditoDTO2);
        historialDeCreditoDTO2.setId(historialDeCreditoDTO1.getId());
        assertThat(historialDeCreditoDTO1).isEqualTo(historialDeCreditoDTO2);
        historialDeCreditoDTO2.setId(2L);
        assertThat(historialDeCreditoDTO1).isNotEqualTo(historialDeCreditoDTO2);
        historialDeCreditoDTO1.setId(null);
        assertThat(historialDeCreditoDTO1).isNotEqualTo(historialDeCreditoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historialDeCreditoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historialDeCreditoMapper.fromId(null)).isNull();
    }
}
