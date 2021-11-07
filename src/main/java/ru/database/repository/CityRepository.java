package ru.database.repository;

import org.springframework.data.jpa.repository.Query;
import ru.database.models.City;
import org.springframework.data.repository.CrudRepository;
import ru.database.models.Region;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Long> {
    @Override
    List<City> findAll();

    @Override
    void deleteAll();

    List<City> getCityByOrderByNameDescDistrictDesc();

    List<City> getCityByOrderByName();

    @Query(value = "select region, count(region) from city group by region",
            nativeQuery = true)
    List<Region> countCityByRegion();
}
