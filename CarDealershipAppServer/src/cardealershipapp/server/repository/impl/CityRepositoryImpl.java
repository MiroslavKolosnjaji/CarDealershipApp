package cardealershipapp.server.repository.impl;

import cardealershipapp.server.database.DataBase;
import java.sql.*;
import cardealershipapp.common.domain.City;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.repository.Repository;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class CityRepositoryImpl implements Repository<City, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void add(City city) throws Exception {
        try {
            String query = "INSERT INTO city(ZipCode, CityName) VALUES(?,?)";
            paramsQueue.addAll(List.of(city.getZipCode(), city.getName()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja novog Grada u bazu!");
        }
    }

    @Override
    public void update(City city) throws Exception {
        try {
            String query = "UPDATE city SET ZipCode = ?, CityName = ? WHERE Id = ?";
            paramsQueue.addAll(List.of(city.getZipCode(),
                    city.getName(),
                    city.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka Grada u bazi!");

        }
    }

    @Override
    public void delete(City city) throws Exception {
        try {
            String query = "DELETE FROM city WHERE Id = ?";
            paramsQueue.add(city.getId());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja Grada iz baze!");
        }
    }
    
     @Override
    public void deleteMultiple(List<City> cities) throws Exception {
        try {
            String query = db.generateDeleteMultiQuery(cities, "city");
            cities.forEach(city -> paramsQueue.add(city.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }

    @Override
    public List<City> getAll() throws Exception {
        try {

            List<City> cities = new ArrayList<>();

            String query = "SELECT Id, ZipCode, CityName FROM city";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("Id");
                Integer zipCode = rs.getInt("ZipCode");
                String name = rs.getString("CityName");

                City c = new City(id, zipCode, name);

                cities.add(c);

            }

            rs.close();
            statement.close();
            db.confirmTransaction();

            return cities;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom ucitavanja gradova iz baze!");
        }

    }

    @Override
    public City findById(Long id) throws Exception {
        try {

            String query = "SELECT ZipCode, CityName FROM city where Id = ?";

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                City city = new City();
                city.setZipCode(rs.getInt("ZipCode"));
                city.setName(rs.getString("CityName"));

                rs.close();
                preparedStatement.close();
                db.confirmTransaction();
                return city;

            } else {
                throw new Exception("Grad sa ovim Id brojem ne postoji!");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom ucitavanja podataka grada!");
        }
    }

    @Override
    public List<City> findByQuery(String query) throws Exception {
       try {
            List<City> cities = new ArrayList<>();
            
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            while (rs.next()) {
                Integer zipCode = rs.getInt("ZipCode");
                String name = rs.getString("CityName");

                City city = new City(null, zipCode, name);
                cities.add(city);
            }
            rs.close();
            statement.close();
            db.confirmTransaction();
            return cities;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja grada!");
        }
    }

}
