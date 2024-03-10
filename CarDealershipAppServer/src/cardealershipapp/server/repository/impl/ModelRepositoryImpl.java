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

import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelRepositoryImpl implements Repository<Model, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(Model model) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(model.getName(), model.getBrand().getId()));
            db.executeSqlUpdate(SqlQueries.Models.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
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
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka modela u bazi!");
        }
    }

    @Override
    public void delete(Model model) throws RepositoryException {
        try {

            paramsQueue.add(model.getId());
            db.executeSqlUpdate(SqlQueries.Models.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
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
            dbe.printStackTrace();
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
            sqle.printStackTrace();
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

            throw new EntityNotFoundException("Model sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
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
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja modela!");
        }

    }

}
