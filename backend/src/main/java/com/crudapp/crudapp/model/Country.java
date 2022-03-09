package com.crudapp.crudapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "countrycode")
    private String countrycode;

    public Country(String name, String countrycode) {
        this.name = name;
        this.countrycode = countrycode;
    }

    @Override
    public String toString() {
        return "Tutorial [id=" + id + ", name=" + name + ", countrycode=" + countrycode + "]";
    }

}
