package com.sistema.credito.web.rest;

import com.sistema.credito.CreditoApp;

import com.sistema.credito.domain.Abono;
import com.sistema.credito.domain.Factura;
import com.sistema.credito.repository.AbonoRepository;
import com.sistema.credito.service.AbonoService;
import com.sistema.credito.service.dto.AbonoDTO;
import com.sistema.credito.service.mapper.AbonoMapper;
import com.sistema.credito.web.rest.errors.ExceptionTranslator;
import com.sistema.credito.service.dto.AbonoCriteria;
import com.sistema.credito.service.AbonoQueryService;

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
 * Test class for the AbonoResource REST controller.
 *
 * @see AbonoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CreditoApp.class)
public class AbonoResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ABONO = 1L;
    private static final Long UPDATED_ABONO = 2L;

    @Autowired
    private AbonoRepository abonoRepository;

    @Autowired
    private AbonoMapper abonoMapper;

    @Autowired
    private AbonoService abonoService;

    @Autowired
    private AbonoQueryService abonoQueryService;

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

    private MockMvc restAbonoMockMvc;

    private Abono abono;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AbonoResource abonoResource = new AbonoResource(abonoService, abonoQueryService);
        this.restAbonoMockMvc = MockMvcBuilders.standaloneSetup(abonoResource)
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
    public static Abono createEntity(EntityManager em) {
        Abono abono = new Abono()
            .fecha(DEFAULT_FECHA)
            .abono(DEFAULT_ABONO);
        return abono;
    }

    @Before
    public void initTest() {
        abono = createEntity(em);
    }

    @Test
    @Transactional
    public void createAbono() throws Exception {
        int databaseSizeBeforeCreate = abonoRepository.findAll().size();

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);
        restAbonoMockMvc.perform(post("/api/abonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isCreated());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeCreate + 1);
        Abono testAbono = abonoList.get(abonoList.size() - 1);
        assertThat(testAbono.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testAbono.getAbono()).isEqualTo(DEFAULT_ABONO);
    }

    @Test
    @Transactional
    public void createAbonoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = abonoRepository.findAll().size();

        // Create the Abono with an existing ID
        abono.setId(1L);
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonoMockMvc.perform(post("/api/abonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonoRepository.findAll().size();
        // set the field null
        abono.setFecha(null);

        // Create the Abono, which fails.
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        restAbonoMockMvc.perform(post("/api/abonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isBadRequest());

        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAbonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonoRepository.findAll().size();
        // set the field null
        abono.setAbono(null);

        // Create the Abono, which fails.
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        restAbonoMockMvc.perform(post("/api/abonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isBadRequest());

        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAbonos() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList
        restAbonoMockMvc.perform(get("/api/abonos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abono.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].abono").value(hasItem(DEFAULT_ABONO.intValue())));
    }
    
    @Test
    @Transactional
    public void getAbono() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get the abono
        restAbonoMockMvc.perform(get("/api/abonos/{id}", abono.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(abono.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.abono").value(DEFAULT_ABONO.intValue()));
    }

    @Test
    @Transactional
    public void getAllAbonosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where fecha equals to DEFAULT_FECHA
        defaultAbonoShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the abonoList where fecha equals to UPDATED_FECHA
        defaultAbonoShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAbonosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultAbonoShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the abonoList where fecha equals to UPDATED_FECHA
        defaultAbonoShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAbonosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where fecha is not null
        defaultAbonoShouldBeFound("fecha.specified=true");

        // Get all the abonoList where fecha is null
        defaultAbonoShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllAbonosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where fecha greater than or equals to DEFAULT_FECHA
        defaultAbonoShouldBeFound("fecha.greaterOrEqualThan=" + DEFAULT_FECHA);

        // Get all the abonoList where fecha greater than or equals to UPDATED_FECHA
        defaultAbonoShouldNotBeFound("fecha.greaterOrEqualThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllAbonosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where fecha less than or equals to DEFAULT_FECHA
        defaultAbonoShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the abonoList where fecha less than or equals to UPDATED_FECHA
        defaultAbonoShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }


    @Test
    @Transactional
    public void getAllAbonosByAbonoIsEqualToSomething() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where abono equals to DEFAULT_ABONO
        defaultAbonoShouldBeFound("abono.equals=" + DEFAULT_ABONO);

        // Get all the abonoList where abono equals to UPDATED_ABONO
        defaultAbonoShouldNotBeFound("abono.equals=" + UPDATED_ABONO);
    }

    @Test
    @Transactional
    public void getAllAbonosByAbonoIsInShouldWork() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where abono in DEFAULT_ABONO or UPDATED_ABONO
        defaultAbonoShouldBeFound("abono.in=" + DEFAULT_ABONO + "," + UPDATED_ABONO);

        // Get all the abonoList where abono equals to UPDATED_ABONO
        defaultAbonoShouldNotBeFound("abono.in=" + UPDATED_ABONO);
    }

    @Test
    @Transactional
    public void getAllAbonosByAbonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where abono is not null
        defaultAbonoShouldBeFound("abono.specified=true");

        // Get all the abonoList where abono is null
        defaultAbonoShouldNotBeFound("abono.specified=false");
    }

    @Test
    @Transactional
    public void getAllAbonosByAbonoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where abono greater than or equals to DEFAULT_ABONO
        defaultAbonoShouldBeFound("abono.greaterOrEqualThan=" + DEFAULT_ABONO);

        // Get all the abonoList where abono greater than or equals to UPDATED_ABONO
        defaultAbonoShouldNotBeFound("abono.greaterOrEqualThan=" + UPDATED_ABONO);
    }

    @Test
    @Transactional
    public void getAllAbonosByAbonoIsLessThanSomething() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList where abono less than or equals to DEFAULT_ABONO
        defaultAbonoShouldNotBeFound("abono.lessThan=" + DEFAULT_ABONO);

        // Get all the abonoList where abono less than or equals to UPDATED_ABONO
        defaultAbonoShouldBeFound("abono.lessThan=" + UPDATED_ABONO);
    }


    @Test
    @Transactional
    public void getAllAbonosByFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        Factura factura = FacturaResourceIntTest.createEntity(em);
        em.persist(factura);
        em.flush();
        abono.setFactura(factura);
        abonoRepository.saveAndFlush(abono);
        Long facturaId = factura.getId();

        // Get all the abonoList where factura equals to facturaId
        defaultAbonoShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the abonoList where factura equals to facturaId + 1
        defaultAbonoShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAbonoShouldBeFound(String filter) throws Exception {
        restAbonoMockMvc.perform(get("/api/abonos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abono.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].abono").value(hasItem(DEFAULT_ABONO.intValue())));

        // Check, that the count call also returns 1
        restAbonoMockMvc.perform(get("/api/abonos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAbonoShouldNotBeFound(String filter) throws Exception {
        restAbonoMockMvc.perform(get("/api/abonos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAbonoMockMvc.perform(get("/api/abonos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAbono() throws Exception {
        // Get the abono
        restAbonoMockMvc.perform(get("/api/abonos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbono() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();

        // Update the abono
        Abono updatedAbono = abonoRepository.findById(abono.getId()).get();
        // Disconnect from session so that the updates on updatedAbono are not directly saved in db
        em.detach(updatedAbono);
        updatedAbono
            .fecha(UPDATED_FECHA)
            .abono(UPDATED_ABONO);
        AbonoDTO abonoDTO = abonoMapper.toDto(updatedAbono);

        restAbonoMockMvc.perform(put("/api/abonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isOk());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
        Abono testAbono = abonoList.get(abonoList.size() - 1);
        assertThat(testAbono.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testAbono.getAbono()).isEqualTo(UPDATED_ABONO);
    }

    @Test
    @Transactional
    public void updateNonExistingAbono() throws Exception {
        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonoMockMvc.perform(put("/api/abonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAbono() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        int databaseSizeBeforeDelete = abonoRepository.findAll().size();

        // Get the abono
        restAbonoMockMvc.perform(delete("/api/abonos/{id}", abono.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Abono.class);
        Abono abono1 = new Abono();
        abono1.setId(1L);
        Abono abono2 = new Abono();
        abono2.setId(abono1.getId());
        assertThat(abono1).isEqualTo(abono2);
        abono2.setId(2L);
        assertThat(abono1).isNotEqualTo(abono2);
        abono1.setId(null);
        assertThat(abono1).isNotEqualTo(abono2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AbonoDTO.class);
        AbonoDTO abonoDTO1 = new AbonoDTO();
        abonoDTO1.setId(1L);
        AbonoDTO abonoDTO2 = new AbonoDTO();
        assertThat(abonoDTO1).isNotEqualTo(abonoDTO2);
        abonoDTO2.setId(abonoDTO1.getId());
        assertThat(abonoDTO1).isEqualTo(abonoDTO2);
        abonoDTO2.setId(2L);
        assertThat(abonoDTO1).isNotEqualTo(abonoDTO2);
        abonoDTO1.setId(null);
        assertThat(abonoDTO1).isNotEqualTo(abonoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(abonoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(abonoMapper.fromId(null)).isNull();
    }
}
