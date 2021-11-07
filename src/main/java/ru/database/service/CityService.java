package ru.database.service;

import ru.database.models.City;
import ru.database.models.Region;

import java.util.List;

public interface CityService {
    List<City> findAll();

    void deleteAll();

    List<City> getCityByOrderByNameDescDistrictDesc();

    List<City> getCityByOrderByName();

    List<Region> countCityByRegion();
}
