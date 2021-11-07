package ru.database.repository;


import ru.database.models.City;
import ru.database.models.Region;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CityRepositoryImpl {
    private final static String DB_URL = "jdbc:h2:./db/Cities;MV_STORE=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private final static String DOC = "src/main/resources/META-INF/doc.txt";

    private static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * удаляет все данные таблицы
     */
    public void deleteTable() {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from city");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * добавляет данные из файла
     */
    public void insertDataFromFile() {
        File file = new File(DOC);
        try (FileReader fr = new FileReader(file);
             BufferedReader reader = new BufferedReader(fr);
             Connection connection = connect()) {

            String line = reader.readLine();
            while (line != null) {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into city values (?, ?, ?, ?, ?, ?)");

                line = line.substring(0, line.length() - 1);
                preparedStatement.setLong(1, Long.parseLong(line));

                line = reader.readLine();
                preparedStatement.setString(2, line.substring(0, line.length() - 1));

                line = reader.readLine();
                preparedStatement.setString(3, line.substring(0, line.length() - 1));

                line = reader.readLine();
                preparedStatement.setString(4, line.substring(0, line.length() - 1));

                line = reader.readLine();
                preparedStatement.setLong(5, Long.parseLong(line.substring(0, line.length() - 1)));

                line = reader.readLine();
                preparedStatement.setLong(6, Long.parseLong(line.substring(0, line.length() - 1)));

                preparedStatement.executeUpdate();

                reader.readLine();
                reader.readLine();
            }

            System.out.println("Well done!\nEverything is inserted properly.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * сортирует таблицу по имени города
     * @return List<City>
     */
    public List<City> sortByName() {
        List<City> cityList = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from city order by name");
            ResultSet resultSet = preparedStatement.executeQuery();

            cityList = setCity(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }


    /**
     * получает данные из всей таблицы
     * @return List<City>
     */
    public List<City> selectAllData() {
        List<City> cityList = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from city");
            ResultSet resultSet = preparedStatement.executeQuery();

            cityList = setCity(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    /**
     * получает город с наибольшим количеством жителей
     * @return Object[]
     */
    public Object[] getMaxPopulation() {
        int[] population;
        int i = 0;
        Object[] arr = new Object[3];

        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select id from city order by id desc limit 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            population = new int[resultSet.getInt(1)];

            preparedStatement = connection.prepareStatement("select * from city");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                population[i] = resultSet.getInt(5);
                i++;
            }

            int max = population[0];
            for (int j : population) {
                if (j > max) {
                    max = j;
                }
            }

            preparedStatement = connection.prepareStatement("select name from city where population = ?");
            preparedStatement.setLong(1, max);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            arr[0] = Arrays.stream(population).boxed().collect(Collectors.toList()).indexOf(max);
            arr[1] = max;
            arr[2] = resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }

    /**
     *
     * @return List<City>
     */
    public List<City> sortByDistrictAndName() {
        List<City> cityList = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from city order by district, name");
            ResultSet resultSet = preparedStatement.executeQuery();

            cityList = setCity(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    /**
     * группирует города по регионам и выводит количество городов
     * @return List<Region>
     */
    public List<Region> numberOfCitiesInRegion() {
        List<Region> list = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select region, count(region) from city group by region");
            ResultSet resultSet = preparedStatement.executeQuery();

            String region;
            int count;

            while (resultSet.next()) {
                region = resultSet.getString(1);
                count = resultSet.getInt(2);
                list.add(new Region(region, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * вспомогательный метод для добавления городов
     * @return List<City>
     */
    private List<City> setCity(ResultSet resultSet) {
        List<City> cityList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                City city = new City();

                city.setId(resultSet.getLong(1));
                city.setName(resultSet.getString(2));
                city.setRegion(resultSet.getString(3));
                city.setDistrict(resultSet.getString(4));
                city.setPopulation(resultSet.getLong(5));
                city.setFoundation(resultSet.getLong(6));

                cityList.add(city);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return cityList;
    }
}
