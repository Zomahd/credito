package com.sistema.credito.web.rest;

import com.sistema.credito.CreditoApp;

import com.sistema.credito.domain.Factura;
import com.sistema.credito.domain.Cliente;
import com.sistema.credito.domain.Abono;
import com.sistema.credito.repository.FacturaRepository;
import com.sistema.credito.service.FacturaService;
import com.sistema.credito.service.dto.FacturaDTO;
import com.sistema.credito.service.mapper.FacturaMapper;
import com.sistema.credito.web.rest.errors.ExceptionTranslator;
import com.sistema.credito.service.dto.FacturaCriteria;
import com.sistema.credito.service.FacturaQueryService;

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

import com.sistema.credito.domain.enumeration.EstadoDeFactura;
/**
 * Test class for the FacturaResource REST controller.
 *
 * @see FacturaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CreditoApp.class)
public class FacturaResourceIntTest {

    private static final String DEFAULT_NUMERO_DE_FACTURA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DE_FACTURA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_TOTAL = 1L;
    private static final Long UPDATED_TOTAL = 2L;

    private static final Long DEFAULT_ABONADO = 1L;
    private static final Long UPDATED_ABONADO = 2L;

    private static final EstadoDeFactura DEFAULT_ESTADO_DE_FACTURA = EstadoDeFactura.ABIERTA;
    private static final EstadoDeFactura UPDATED_ESTADO_DE_FACTURA = EstadoDeFactura.CANCELADA;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaQueryService facturaQueryService;

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

    private MockMvc restFacturaMockMvc;

    private Factura factura;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FacturaResource facturaResource = new FacturaResource(facturaService, facturaQueryService);
        this.restFacturaMockMvc = MockMvcBuilders.standaloneSetup(facturaResource)
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
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .numeroDeFactura(DEFAULT_NUMERO_DE_FACTURA)
            .fecha(DEFAULT_FECHA)
            .total(DEFAULT_TOTAL)
            .abonado(DEFAULT_ABONADO)
            .estadoDeFactura(DEFAULT_ESTADO_DE_FACTURA);
        return factura;
    }

    @Before
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    public void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);
        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumeroDeFactura()).isEqualTo(DEFAULT_NUMERO_DE_FACTURA);
        assertThat(testFactura.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testFactura.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testFactura.getAbonado()).isEqualTo(DEFAULT_ABONADO);
        assertThat(testFactura.getEstadoDeFactura()).isEqualTo(DEFAULT_ESTADO_DE_FACTURA);
    }

    @Test
    @Transactional
    public void createFacturaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // Create the Factura with an existing ID
        factura.setId(1L);
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroDeFacturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setNumeroDeFactura(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setFecha(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setTotal(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoDeFacturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setEstadoDeFactura(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc.perform(get("/api/facturas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroDeFactura").value(hasItem(DEFAULT_NUMERO_DE_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].abonado").value(hasItem(DEFAULT_ABONADO.intValue())))
            .andExpect(jsonPath("$.[*].estadoDeFactura").value(hasItem(DEFAULT_ESTADO_DE_FACTURA.toString())));
    }
    
    @Test
    @Transactional
    public void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc.perform(get("/api/facturas/{id}", factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.numeroDeFactura").value(DEFAULT_NUMERO_DE_FACTURA.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
            .andExpect(jsonPath("$.abonado").value(DEFAULT_ABONADO.intValue()))
            .andExpect(jsonPath("$.estadoDeFactura").value(DEFAULT_ESTADO_DE_FACTURA.toString()));
    }

    @Test
    @Transactional
    public void getAllFacturasByNumeroDeFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numeroDeFactura equals to DEFAULT_NUMERO_DE_FACTURA
        defaultFacturaShouldBeFound("numeroDeFactura.equals=" + DEFAULT_NUMERO_DE_FACTURA);

        // Get all the facturaList where numeroDeFactura equals to UPDATED_NUMERO_DE_FACTURA
        defaultFacturaShouldNotBeFound("numeroDeFactura.equals=" + UPDATED_NUMERO_DE_FACTURA);
    }

    @Test
    @Transactional
    public void getAllFacturasByNumeroDeFacturaIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numeroDeFactura in DEFAULT_NUMERO_DE_FACTURA or UPDATED_NUMERO_DE_FACTURA
        defaultFacturaShouldBeFound("numeroDeFactura.in=" + DEFAULT_NUMERO_DE_FACTURA + "," + UPDATED_NUMERO_DE_FACTURA);

        // Get all the facturaList where numeroDeFactura equals to UPDATED_NUMERO_DE_FACTURA
        defaultFacturaShouldNotBeFound("numeroDeFactura.in=" + UPDATED_NUMERO_DE_FACTURA);
    }

    @Test
    @Transactional
    public void getAllFacturasByNumeroDeFacturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numeroDeFactura is not null
        defaultFacturaShouldBeFound("numeroDeFactura.specified=true");

        // Get all the facturaList where numeroDeFactura is null
        defaultFacturaShouldNotBeFound("numeroDeFactura.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturasByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha equals to DEFAULT_FECHA
        defaultFacturaShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha equals to UPDATED_FECHA
        defaultFacturaShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllFacturasByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultFacturaShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the facturaList where fecha equals to UPDATED_FECHA
        defaultFacturaShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllFacturasByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha is not null
        defaultFacturaShouldBeFound("fecha.specified=true");

        // Get all the facturaList where fecha is null
        defaultFacturaShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturasByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha greater than or equals to DEFAULT_FECHA
        defaultFacturaShouldBeFound("fecha.greaterOrEqualThan=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha greater than or equals to UPDATED_FECHA
        defaultFacturaShouldNotBeFound("fecha.greaterOrEqualThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void getAllFacturasByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha less than or equals to DEFAULT_FECHA
        defaultFacturaShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha less than or equals to UPDATED_FECHA
        defaultFacturaShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }


    @Test
    @Transactional
    public void getAllFacturasByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total equals to DEFAULT_TOTAL
        defaultFacturaShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the facturaList where total equals to UPDATED_TOTAL
        defaultFacturaShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllFacturasByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultFacturaShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the facturaList where total equals to UPDATED_TOTAL
        defaultFacturaShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllFacturasByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is not null
        defaultFacturaShouldBeFound("total.specified=true");

        // Get all the facturaList where total is null
        defaultFacturaShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturasByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total greater than or equals to DEFAULT_TOTAL
        defaultFacturaShouldBeFound("total.greaterOrEqualThan=" + DEFAULT_TOTAL);

        // Get all the facturaList where total greater than or equals to UPDATED_TOTAL
        defaultFacturaShouldNotBeFound("total.greaterOrEqualThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllFacturasByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total less than or equals to DEFAULT_TOTAL
        defaultFacturaShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the facturaList where total less than or equals to UPDATED_TOTAL
        defaultFacturaShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }


    @Test
    @Transactional
    public void getAllFacturasByAbonadoIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where abonado equals to DEFAULT_ABONADO
        defaultFacturaShouldBeFound("abonado.equals=" + DEFAULT_ABONADO);

        // Get all the facturaList where abonado equals to UPDATED_ABONADO
        defaultFacturaShouldNotBeFound("abonado.equals=" + UPDATED_ABONADO);
    }

    @Test
    @Transactional
    public void getAllFacturasByAbonadoIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where abonado in DEFAULT_ABONADO or UPDATED_ABONADO
        defaultFacturaShouldBeFound("abonado.in=" + DEFAULT_ABONADO + "," + UPDATED_ABONADO);

        // Get all the facturaList where abonado equals to UPDATED_ABONADO
        defaultFacturaShouldNotBeFound("abonado.in=" + UPDATED_ABONADO);
    }

    @Test
    @Transactional
    public void getAllFacturasByAbonadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where abonado is not null
        defaultFacturaShouldBeFound("abonado.specified=true");

        // Get all the facturaList where abonado is null
        defaultFacturaShouldNotBeFound("abonado.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturasByAbonadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where abonado greater than or equals to DEFAULT_ABONADO
        defaultFacturaShouldBeFound("abonado.greaterOrEqualThan=" + DEFAULT_ABONADO);

        // Get all the facturaList where abonado greater than or equals to UPDATED_ABONADO
        defaultFacturaShouldNotBeFound("abonado.greaterOrEqualThan=" + UPDATED_ABONADO);
    }

    @Test
    @Transactional
    public void getAllFacturasByAbonadoIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where abonado less than or equals to DEFAULT_ABONADO
        defaultFacturaShouldNotBeFound("abonado.lessThan=" + DEFAULT_ABONADO);

        // Get all the facturaList where abonado less than or equals to UPDATED_ABONADO
        defaultFacturaShouldBeFound("abonado.lessThan=" + UPDATED_ABONADO);
    }


    @Test
    @Transactional
    public void getAllFacturasByEstadoDeFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estadoDeFactura equals to DEFAULT_ESTADO_DE_FACTURA
        defaultFacturaShouldBeFound("estadoDeFactura.equals=" + DEFAULT_ESTADO_DE_FACTURA);

        // Get all the facturaList where estadoDeFactura equals to UPDATED_ESTADO_DE_FACTURA
        defaultFacturaShouldNotBeFound("estadoDeFactura.equals=" + UPDATED_ESTADO_DE_FACTURA);
    }

    @Test
    @Transactional
    public void getAllFacturasByEstadoDeFacturaIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estadoDeFactura in DEFAULT_ESTADO_DE_FACTURA or UPDATED_ESTADO_DE_FACTURA
        defaultFacturaShouldBeFound("estadoDeFactura.in=" + DEFAULT_ESTADO_DE_FACTURA + "," + UPDATED_ESTADO_DE_FACTURA);

        // Get all the facturaList where estadoDeFactura equals to UPDATED_ESTADO_DE_FACTURA
        defaultFacturaShouldNotBeFound("estadoDeFactura.in=" + UPDATED_ESTADO_DE_FACTURA);
    }

    @Test
    @Transactional
    public void getAllFacturasByEstadoDeFacturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estadoDeFactura is not null
        defaultFacturaShouldBeFound("estadoDeFactura.specified=true");

        // Get all the facturaList where estadoDeFactura is null
        defaultFacturaShouldNotBeFound("estadoDeFactura.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturasByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        Cliente cliente = ClienteResourceIntTest.createEntity(em);
        em.persist(cliente);
        em.flush();
        factura.setCliente(cliente);
        facturaRepository.saveAndFlush(factura);
        Long clienteId = cliente.getId();

        // Get all the facturaList where cliente equals to clienteId
        defaultFacturaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the facturaList where cliente equals to clienteId + 1
        defaultFacturaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }


    @Test
    @Transactional
    public void getAllFacturasByAbonoIsEqualToSomething() throws Exception {
        // Initialize the database
        Abono abono = AbonoResourceIntTest.createEntity(em);
        em.persist(abono);
        em.flush();
        factura.addAbono(abono);
        facturaRepository.saveAndFlush(factura);
        Long abonoId = abono.getId();

        // Get all the facturaList where abono equals to abonoId
        defaultFacturaShouldBeFound("abonoId.equals=" + abonoId);

        // Get all the facturaList where abono equals to abonoId + 1
        defaultFacturaShouldNotBeFound("abonoId.equals=" + (abonoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFacturaShouldBeFound(String filter) throws Exception {
        restFacturaMockMvc.perform(get("/api/facturas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroDeFactura").value(hasItem(DEFAULT_NUMERO_DE_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].abonado").value(hasItem(DEFAULT_ABONADO.intValue())))
            .andExpect(jsonPath("$.[*].estadoDeFactura").value(hasItem(DEFAULT_ESTADO_DE_FACTURA.toString())));

        // Check, that the count call also returns 1
        restFacturaMockMvc.perform(get("/api/facturas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFacturaShouldNotBeFound(String filter) throws Exception {
        restFacturaMockMvc.perform(get("/api/facturas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturaMockMvc.perform(get("/api/facturas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get("/api/facturas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .numeroDeFactura(UPDATED_NUMERO_DE_FACTURA)
            .fecha(UPDATED_FECHA)
            .total(UPDATED_TOTAL)
            .abonado(UPDATED_ABONADO)
            .estadoDeFactura(UPDATED_ESTADO_DE_FACTURA);
        FacturaDTO facturaDTO = facturaMapper.toDto(updatedFactura);

        restFacturaMockMvc.perform(put("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumeroDeFactura()).isEqualTo(UPDATED_NUMERO_DE_FACTURA);
        assertThat(testFactura.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFactura.getAbonado()).isEqualTo(UPDATED_ABONADO);
        assertThat(testFactura.getEstadoDeFactura()).isEqualTo(UPDATED_ESTADO_DE_FACTURA);
    }

    @Test
    @Transactional
    public void updateNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc.perform(put("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Get the factura
        restFacturaMockMvc.perform(delete("/api/facturas/{id}", factura.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factura.class);
        Factura factura1 = new Factura();
        factura1.setId(1L);
        Factura factura2 = new Factura();
        factura2.setId(factura1.getId());
        assertThat(factura1).isEqualTo(factura2);
        factura2.setId(2L);
        assertThat(factura1).isNotEqualTo(factura2);
        factura1.setId(null);
        assertThat(factura1).isNotEqualTo(factura2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturaDTO.class);
        FacturaDTO facturaDTO1 = new FacturaDTO();
        facturaDTO1.setId(1L);
        FacturaDTO facturaDTO2 = new FacturaDTO();
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
        facturaDTO2.setId(facturaDTO1.getId());
        assertThat(facturaDTO1).isEqualTo(facturaDTO2);
        facturaDTO2.setId(2L);
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
        facturaDTO1.setId(null);
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(facturaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(facturaMapper.fromId(null)).isNull();
    }
}
