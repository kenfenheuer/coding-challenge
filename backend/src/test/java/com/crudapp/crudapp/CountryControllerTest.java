package com.crudapp.crudapp;

import com.crudapp.crudapp.model.Country;
import com.crudapp.crudapp.repository.CountryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Kenfenheuer
 * created on 09.03.2022
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(Country.class)
public class CountryControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CountryRepository countryRepository;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc =
                MockMvcBuilders
                        .webAppContextSetup(this.webApplicationContext)
                        .build();
        Country country = new Country();
        country.setName("Germany");
        country.setCountrycode("DE");
        countryRepository.save(country);
        Country country2 = new Country();
        country2.setName("France");
        country2.setCountrycode("FR");
        countryRepository.save(country2);
    }

    @Test
    void getAllTest() throws Exception {
        String response = mockMvc.perform(get("/api/countries").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<Country> countries = mapper.readerForListOf(Country.class).readValue(response);
        assertEquals(2,countries.size(),"Not all countries returned");
        Country germany = countries.get(0);
        assertEquals(1L,germany.getId(), "ID of country is different than expected");
        assertEquals("Germany", germany.getName(),"Name of country is different than expected");
        assertEquals("DE",germany.getCountrycode(), "Countrycode of country is different than expected ");
        Country france = countries.get(1);
        assertEquals(2L,france.getId(),"ID of country is different than expected");
        assertEquals("France", france.getName(),"Name of country is different than expected");
        assertEquals("FR",france.getCountrycode(),"Countrycode of country is different than expected");

    }

    @Test
    void getAllTest_CORS() throws Exception {
        String response = mockMvc.perform(get("/api/countries").contextPath(contextPath).header("Origin","http://localhost:4200").header("Access-Control-Request-Method","GET").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<Country> countries = mapper.readerForListOf(Country.class).readValue(response);
        assertEquals(2,countries.size(),"Not all countries returned");
        Country germany = countries.get(0);
        assertEquals(1L,germany.getId(), "ID of country is different than expected");
        assertEquals("Germany", germany.getName(),"Name of country is wroFng");
        assertEquals("DE",germany.getCountrycode(), "Countrycode of country is different than expected ");
        Country france = countries.get(1);
        assertEquals(2L,france.getId(),"ID of country is different than expected");
        assertEquals("France", france.getName(),"Name of country is different than expected");
        assertEquals("FR",france.getCountrycode(),"Countrycode of country is different than expected");
    }

    @Test
    void getCountriesWithName() throws Exception{
        String response = mockMvc.perform(get("/api/countries/Germany").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<Country> countries = mapper.readerForListOf(Country.class).readValue(response);
        assertEquals(1,countries.size());
        Country germany = countries.get(0);
        assertEquals(1L,germany.getId(), "ID of country is different than expected");
        assertEquals("Germany", germany.getName(),"Name of country is different than expected");
        assertEquals("DE",germany.getCountrycode(), "Countrycode of country is different than expected ");
    }

    @Test
    void addCountry() throws Exception{
        mockMvc.perform(post("/api/countries").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Italy\",\"countrycode\":\"IT\"}")).andExpect(status().is2xxSuccessful());
        Country country = new Country();
        country.setName("Italy");
        country.setCountrycode("IT");
        Optional<Country> optional =countryRepository.findOne(Example.of(country,ExampleMatcher.matchingAll().withIgnorePaths("id")));
        assertTrue(optional.isPresent(),"Country not found");
        Country italy = optional.get();
        assertEquals(3L,italy.getId(),"ID of country is different than expected");
        assertEquals("Italy",italy.getName(),"Name of country is different than expected");
        assertEquals("IT",italy.getCountrycode(),"Countrycode of country is different than expected");
    }

    @Test
    void updateCountry() throws Exception{
        mockMvc.perform(put("/api/countries/1").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Spain\",\"countrycode\":\"ES\"}")).andExpect(status().is2xxSuccessful());
        Optional<Country> countryOptional = countryRepository.findById(1L);
        assertTrue(countryOptional.isPresent());
        Country country = countryOptional.get();
        assertEquals(1L,country.getId(),"ID of countr is different than expected");
        assertEquals("Spain",country.getName(), "Name of country is different than expected");
        assertEquals("ES",country.getCountrycode()," Countrycode of country is different than expected");
    }

    @Test
    void deleteCountry() throws Exception{
        mockMvc.perform(delete("/api/countries/1").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
        Optional<Country> country = countryRepository.findById(1L);
        assertFalse(country.isPresent(),"Country should not be found after deletion");
    }

}
