package cardealershipapp.server.repository.impl;

import cardealershipapp.server.database.DataBase;

import java.sql.*;

import cardealershipapp.common.domain.City;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.query.SqlQueries;
import cardealershipapp.server.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class CityRepositoryImpl implements Repository<City, Long> {

    private static final Logger log = LoggerFactory.getLogger(CityRepositoryImpl.class);
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(City city) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(city.getZipCode(), city.getName()));
            db.executeSqlUpdate(SqlQueries.Cities.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa grada '" + city.getName() + "' u bazu podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom dodavanja novog grada u bazu!");
        }
    }

    @Override
    public void update(City city) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(city.getZipCode(),
                    city.getName(),
                    city.getId()));
            db.executeSqlUpdate(SqlQueries.Cities.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom ažuriramja grada '" + city.getName() + "' u bazi podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka grada u bazi!");

        }
    }

    @Override
    public void delete(City city) throws RepositoryException {
        try {

            paramsQueue.add(city.getId());
            db.executeSqlUpdate(SqlQueries.Cities.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja grada '" + city.getName() + "' iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja grada iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<City> cities) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(cities, SqlQueries.Cities.DELETE_MULTIPLE_ID);
            cities.forEach(city -> paramsQueue.add(city.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja '" + cities.size() + "' gradova iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja vise gradova iz baze!");
        }

    }

    @Override
    public List<City> getAll() throws RepositoryException {
        try {

            List<City> cities = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.Cities.SELECT_ALL);

            while (rs.next()) {
                Long id = rs.getLong("Id");
                Integer zipCode = rs.getInt("ZipCode");
                String name = rs.getString("CityName");

                City c = new City(id, zipCode, name);

                cities.add(c);

            }

            rs.close();
            statement.close();
            return cities;

        } catch (SQLException sqle) {
            log.error("Greška prilikom učitavanja gradova iz baze podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja gradova iz baze!");
        }

    }

    @Override
    public City findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(SqlQueries.Cities.SELECT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                City city = new City();
                city.setZipCode(rs.getInt("ZipCode"));
                city.setName(rs.getString("CityName"));

                rs.close();
                preparedStatement.close();
                return city;
            }

            throw new EntityNotFoundException("Grad sa ovim Id brojem ne postoji!");


        } catch (SQLException sqle) {
            log.error("Greška prilikom pretraživanja grada po ID '" + id + "' u bazi podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrage grada pod ID broju!");
        }
    }

    @Override
    public List<City> findByQuery(String query) throws RepositoryException {
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
            return cities;

        } catch (SQLException sqle) {
            log.error(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + query + " u metodi findByQeury klase: " + this.getClass().getSimpleName() + " : " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja grada po upitu!");
        }
    }

}
