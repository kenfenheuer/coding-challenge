package com.crudapp.crudapp;

import com.crudapp.crudapp.model.Country;
import com.crudapp.crudapp.repository.CountryRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Kenfenheuer
 * created on 09.03.2022
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CountryControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CountryRepository countryRepository;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

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
    }

    @Test
    void getAllTest() throws Exception {
        mockMvc.perform(get("/api/countries").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void getAllTest_CORS() throws Exception {
        mockMvc.perform(get("/api/countries").contextPath(contextPath).header("Origin","http://localhost:4200").header("Access-Control-Request-Method","GET").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void getCountriesWithName() throws Exception{
        mockMvc.perform(get("/api/countries/Germany").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void addCountry() throws Exception{
        mockMvc.perform(post("/api/countries").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Germany\",\"countrycode\":\"DE\"}")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateCountry() throws Exception{
        mockMvc.perform(put("/api/countries/1").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Germany2\",\"countrycode\":\"DE2\"}")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteCountry() throws Exception{
        mockMvc.perform(delete("/api/countries/1").contextPath(contextPath).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
    }

}
