package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
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
public class ModelRepositoryImpl implements Repository<Model, Long> {

    public static final Logger log = LoggerFactory.getLogger(ModelRepositoryImpl.class);
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(Model model) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(model.getName(), model.getBrand().getId()));
            db.executeSqlUpdate(SqlQueries.Models.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa modela '" + model.getName() + "' u bazu podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom dodavanja novog modela u bazu!");
        }
    }

    @Override
    public void update(Model model) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(model.getName(),
                    model.getBrand().getId(),
                    model.getId()));
            db.executeSqlUpdate(SqlQueries.Models.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom ažuriranja modela '" + model.getName() + "' u bazi podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka modela u bazi!");
        }
    }

    @Override
    public void delete(Model model) throws RepositoryException {
        try {

            paramsQueue.add(model.getId());
            db.executeSqlUpdate(SqlQueries.Models.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja modela '" + model.getName() + "' iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja modela iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<Model> models) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(models, SqlQueries.Models.DELETE_MULTIPLE_ID);
            models.forEach(model -> paramsQueue.add(model.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja '" + models.size() + "' modela iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }

    @Override
    public List<Model> getAll() throws RepositoryException {
        try {

            List<Model> models = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.Models.SELECT_ALL);

            while (rs.next()) {
                Long modelId = rs.getLong("M.Id");
                String modelName = rs.getString("M.ModelName");
                Long brandId = rs.getLong("M.BrandId");
                String brandName = rs.getString("B.BrandName");

                Brand brand = new Brand(brandId, brandName);
                Model model = new Model(modelId, modelName, brand);

                models.add(model);

            }

            rs.close();
            statement.close();
            return models;

        } catch (SQLException sqle) {
            log.error("Greška prilikom učitavanja modela iz baze podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja modela iz baze!");
        }
    }

    @Override
    public Model findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(SqlQueries.Models.SELECT_BY_ID);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Model model = new Model();
                model.setId(id);
                model.setName(rs.getString("ModelName"));
                model.setBrand(new Brand(rs.getLong("BrandId")));

                rs.close();
                preparedStatement.close();
                return model;
            }

            throw new EntityNotFoundException("Model ne postoji!");

        } catch (SQLException sqle) {
            log.error("Greška prilikom pretraživanja modela po ID '" + id + "' u bazi podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja modela po id-u!");
        }
    }

    @Override
    public List<Model> findByQuery(String query) throws RepositoryException {
        try {

            List<Model> models = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Model model = new Model();
                model.setId(rs.getLong("Id"));
                model.setName(rs.getString("ModelName"));
                model.setBrand(new Brand(rs.getLong("BrandId")));

                models.add(model);
            }

            rs.close();
            statement.close();
            return models;

        } catch (SQLException sqle) {
            log.error(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + query + " u metodi findByQeury klase: " +this.getClass().getSimpleName()+ " : " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja modela!");
        }

    }

}
