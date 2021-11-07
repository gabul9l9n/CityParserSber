package ru.database.service;

import ru.database.models.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.database.models.Region;
import ru.database.repository.CityRepository;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private CityRepository cityRepository;

    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public void deleteAll() {
        cityRepository.deleteAll();
    }

    @Override
    public List<City> getCityByOrderByNameDescDistrictDesc() {
        return cityRepository.getCityByOrderByNameDescDistrictDesc();
    }

    @Override
    public List<City> getCityByOrderByName() {
        return cityRepository.getCityByOrderByName();
    }

    @Override
    public List<Region> countCityByRegion() {
        return cityRepository.countCityByRegion();
    }


}