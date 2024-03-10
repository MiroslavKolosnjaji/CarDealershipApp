package cardealershipapp.server.repository.impl;

import cardealershipapp.server.database.DataBase;

import java.sql.*;

import cardealershipapp.common.domain.Brand;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.query.SqlQueries;

import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class BrandRepositoryImpl implements Repository<Brand, Long> {
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(Brand brand) throws RepositoryException {
        try {

            paramsQueue.add(brand.getBrandName());
            db.executeSqlUpdate(SqlQueries.Brands.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom unosa brenda u bazu!");
        }
    }

    @Override
    public void update(Brand brand) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(brand.getBrandName(), brand.getId()));
            db.executeSqlUpdate(SqlQueries.Brands.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka brenda u bazi!");

        }
    }

    @Override
    public void delete(Brand brand) throws RepositoryException {
        try {

            paramsQueue.add(brand.getId());
            db.executeSqlUpdate(SqlQueries.Brands.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja brenda iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<Brand> brands) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(brands, SqlQueries.Brands.DELETE_MULTIPLE_ID);
            paramsQueue.addAll(brands);
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja brendova iz baze!");
        }
    }

    @Override
    public List<Brand> getAll() throws RepositoryException {
        try {

            List<Brand> brands = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.Brands.SELECT_ALL);

            while (rs.next()) {
                Long id = rs.getLong("Id");
                String name = rs.getString("BrandName");

                Brand b = new Brand(id, name);

                brands.add(b);

            }

            rs.close();
            statement.close();
            return brands;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja brendova iz baze!");
        }

    }

    @Override
    public Brand findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement prepStat = db.getConnection().prepareStatement(SqlQueries.Brands.SELECT_BY_ID);
            prepStat.setLong(1, id);
            ResultSet rs = prepStat.executeQuery();

            if (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getLong("Id"));
                brand.setBrandName(rs.getString("BrandName"));

                prepStat.close();
                return brand;

            } else {
                throw new EntityNotFoundException("Marka sa ovim Id brojem ne postoji!");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrage marke po ID broju!");
        }
    }

    @Override
    public List<Brand> findByQuery(String query) throws RepositoryException {

        try {

            List<Brand> brands = new ArrayList<>();
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Brand brand = new Brand();
                brand.setBrandName(rs.getString("BrandName"));
                brands.add(brand);
            }

            rs.close();
            statement.close();
            return brands;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja marke po upitu!");
        }
    }

}
