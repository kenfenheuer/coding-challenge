package com.crudapp.crudapp;

import com.crudapp.crudapp.model.Country;
import com.crudapp.crudapp.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CrudappApplicationTests {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;


    @Test
    public void testSaveNewCountry() {
        Country country = new Country("testcountry", "TE");
        entityManager.persist(country);

        Country foundedcountry = countryRepository.findByNameIgnoreCase("testcountry").get(0);

        assertThat(foundedcountry.getName()).isEqualTo("testcountry");
    }

    @Test
    public void testUpdateCountry() {
        Country country1 = new Country("country1", "C1");
        entityManager.persist(country1);
        Country country2 = new Country("country2", "C2");
        entityManager.persist(country2);
        Country updatedCountry = new Country("updatedcountry", "U1");
        Country country = countryRepository.findById(country2.getId()).get();
        country.setName(updatedCountry.getName());
        country.setCountrycode(updatedCountry.getCountrycode());
        countryRepository.save(country);
        Country checkCountry = countryRepository.findById(country2.getId()).get();

        assertThat(checkCountry.getName()).isEqualTo(updatedCountry.getName());
        assertThat(checkCountry.getCountrycode()).isEqualTo(updatedCountry.getCountrycode());
    }

    @Test
    public void testDeleteCountry() {
        Country country1 = new Country("country1", "C1");
        entityManager.persist(country1);
        Country country2 = new Country("country2", "C2");
        entityManager.persist(country2);

        Optional<Country> country = countryRepository.findById(country2.getId());
        if (country.isPresent()) {
            assertTrue(true);
        } else {
            fail();
        }
        countryRepository.deleteById(country2.getId());
        Optional<Country> deletedCountry = countryRepository.findById(country2.getId());
        if (deletedCountry.isEmpty()) {
            assertTrue(true);
        } else {
            fail();
        }
    }
}
