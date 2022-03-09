package com.crudapp.crudapp.repository;

import com.crudapp.crudapp.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findByNameIgnoreCase(String name);

}
