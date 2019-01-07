package com.sistema.credito.web.rest;

import com.sistema.credito.CreditoApp;

import com.sistema.credito.domain.Cliente;
import com.sistema.credito.domain.Factura;
import com.sistema.credito.repository.ClienteRepository;
import com.sistema.credito.service.ClienteService;
import com.sistema.credito.service.dto.ClienteDTO;
import com.sistema.credito.service.mapper.ClienteMapper;
import com.sistema.credito.web.rest.errors.ExceptionTranslator;
import com.sistema.credito.service.dto.ClienteCriteria;
import com.sistema.credito.service.ClienteQueryService;

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
import java.util.List;


import static com.sistema.credito.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClienteResource REST controller.
 *
 * @see ClienteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CreditoApp.class)
public class ClienteResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteQueryService clienteQueryService;

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

    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClienteResource clienteResource = new ClienteResource(clienteService, clienteQueryService);
        this.restClienteMockMvc = MockMvcBuilders.standaloneSetup(clienteResource)
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
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .nombre(DEFAULT_NOMBRE)
            .primerApellido(DEFAULT_PRIMER_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .correo(DEFAULT_CORREO)
            .telefono(DEFAULT_TELEFONO);
        return cliente;
    }

    @Before
    public void initTest() {
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    public void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);
        restClienteMockMvc.perform(post("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCliente.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testCliente.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testCliente.getCorreo()).isEqualTo(DEFAULT_CORREO);
        assertThat(testCliente.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    public void createClienteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // Create the Cliente with an existing ID
        cliente.setId(1L);
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc.perform(post("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setNombre(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc.perform(post("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrimerApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setPrimerApellido(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc.perform(post("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())));
    }
    
    @Test
    @Transactional
    public void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.primerApellido").value(DEFAULT_PRIMER_APELLIDO.toString()))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO.toString()))
            .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()));
    }

    @Test
    @Transactional
    public void getAllClientesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre equals to DEFAULT_NOMBRE
        defaultClienteShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the clienteList where nombre equals to UPDATED_NOMBRE
        defaultClienteShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllClientesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultClienteShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the clienteList where nombre equals to UPDATED_NOMBRE
        defaultClienteShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllClientesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombre is not null
        defaultClienteShouldBeFound("nombre.specified=true");

        // Get all the clienteList where nombre is null
        defaultClienteShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByPrimerApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido equals to DEFAULT_PRIMER_APELLIDO
        defaultClienteShouldBeFound("primerApellido.equals=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the clienteList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultClienteShouldNotBeFound("primerApellido.equals=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllClientesByPrimerApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido in DEFAULT_PRIMER_APELLIDO or UPDATED_PRIMER_APELLIDO
        defaultClienteShouldBeFound("primerApellido.in=" + DEFAULT_PRIMER_APELLIDO + "," + UPDATED_PRIMER_APELLIDO);

        // Get all the clienteList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultClienteShouldNotBeFound("primerApellido.in=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllClientesByPrimerApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where primerApellido is not null
        defaultClienteShouldBeFound("primerApellido.specified=true");

        // Get all the clienteList where primerApellido is null
        defaultClienteShouldNotBeFound("primerApellido.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesBySegundoApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido equals to DEFAULT_SEGUNDO_APELLIDO
        defaultClienteShouldBeFound("segundoApellido.equals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the clienteList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldNotBeFound("segundoApellido.equals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllClientesBySegundoApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido in DEFAULT_SEGUNDO_APELLIDO or UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldBeFound("segundoApellido.in=" + DEFAULT_SEGUNDO_APELLIDO + "," + UPDATED_SEGUNDO_APELLIDO);

        // Get all the clienteList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultClienteShouldNotBeFound("segundoApellido.in=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllClientesBySegundoApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where segundoApellido is not null
        defaultClienteShouldBeFound("segundoApellido.specified=true");

        // Get all the clienteList where segundoApellido is null
        defaultClienteShouldNotBeFound("segundoApellido.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByCorreoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo equals to DEFAULT_CORREO
        defaultClienteShouldBeFound("correo.equals=" + DEFAULT_CORREO);

        // Get all the clienteList where correo equals to UPDATED_CORREO
        defaultClienteShouldNotBeFound("correo.equals=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    public void getAllClientesByCorreoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo in DEFAULT_CORREO or UPDATED_CORREO
        defaultClienteShouldBeFound("correo.in=" + DEFAULT_CORREO + "," + UPDATED_CORREO);

        // Get all the clienteList where correo equals to UPDATED_CORREO
        defaultClienteShouldNotBeFound("correo.in=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    public void getAllClientesByCorreoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where correo is not null
        defaultClienteShouldBeFound("correo.specified=true");

        // Get all the clienteList where correo is null
        defaultClienteShouldNotBeFound("correo.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono equals to DEFAULT_TELEFONO
        defaultClienteShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the clienteList where telefono equals to UPDATED_TELEFONO
        defaultClienteShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllClientesByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultClienteShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the clienteList where telefono equals to UPDATED_TELEFONO
        defaultClienteShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllClientesByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono is not null
        defaultClienteShouldBeFound("telefono.specified=true");

        // Get all the clienteList where telefono is null
        defaultClienteShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        Factura factura = FacturaResourceIntTest.createEntity(em);
        em.persist(factura);
        em.flush();
        cliente.addFactura(factura);
        clienteRepository.saveAndFlush(cliente);
        Long facturaId = factura.getId();

        // Get all the clienteList where factura equals to facturaId
        defaultClienteShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the clienteList where factura equals to facturaId + 1
        defaultClienteShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultClienteShouldBeFound(String filter) throws Exception {
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())));

        // Check, that the count call also returns 1
        restClienteMockMvc.perform(get("/api/clientes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultClienteShouldNotBeFound(String filter) throws Exception {
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClienteMockMvc.perform(get("/api/clientes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).get();
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente
            .nombre(UPDATED_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .correo(UPDATED_CORREO)
            .telefono(UPDATED_TELEFONO);
        ClienteDTO clienteDTO = clienteMapper.toDto(updatedCliente);

        restClienteMockMvc.perform(put("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCliente.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testCliente.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testCliente.getCorreo()).isEqualTo(UPDATED_CORREO);
        assertThat(testCliente.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void updateNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc.perform(put("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Get the cliente
        restClienteMockMvc.perform(delete("/api/clientes/{id}", cliente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        Cliente cliente2 = new Cliente();
        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);
        cliente2.setId(2L);
        assertThat(cliente1).isNotEqualTo(cliente2);
        cliente1.setId(null);
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClienteDTO.class);
        ClienteDTO clienteDTO1 = new ClienteDTO();
        clienteDTO1.setId(1L);
        ClienteDTO clienteDTO2 = new ClienteDTO();
        assertThat(clienteDTO1).isNotEqualTo(clienteDTO2);
        clienteDTO2.setId(clienteDTO1.getId());
        assertThat(clienteDTO1).isEqualTo(clienteDTO2);
        clienteDTO2.setId(2L);
        assertThat(clienteDTO1).isNotEqualTo(clienteDTO2);
        clienteDTO1.setId(null);
        assertThat(clienteDTO1).isNotEqualTo(clienteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clienteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clienteMapper.fromId(null)).isNull();
    }
}
