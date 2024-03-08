package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.repository.Repository;

import javax.xml.crypto.Data;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ModelRepositoryImpl implements Repository<Model, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void add(Model model) throws Exception {
        try {
            String query = "INSERT INTO model(ModelName, BrandId) VALUES(?,?)";
            paramsQueue.addAll(List.of(model.getName(), model.getBrand().getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja novog modela u bazu!\n" + dbe.getMessage());
        }
    }

    @Override
    public void update(Model model) throws Exception {
        try {
            String query = "UPDATE model SET ModelName = ?, BrandId = ? WHERE Id = ?";
            paramsQueue.addAll(List.of(model.getName(),
                    model.getBrand().getId(),
                    model.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka modela u bazi!\n" + dbe.getMessage());
        }
    }

    @Override
    public void delete(Model model) throws Exception {
        try {
            String query = "DELETE FROM model WHERE Id = ?";
            paramsQueue.add(model.getId());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja modela iz baze!\n" + dbe.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<Model> models )throws Exception {
        try {
            String query = db.generateDeleteMultiQuery(models,"model");
            models.forEach(model -> paramsQueue.add(model.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }

    @Override
    public List<Model> getAll() throws Exception {
        try {

            List<Model> models = new ArrayList<>();

            String query = "SELECT M.id, M.ModelName, M.BrandId, B.BrandName FROM model M JOIN Brand B ON M.BrandId = B.Id";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

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
            db.confirmTransaction();

            return models;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom ucitavanja modela iz baze!\n" + sqle.getMessage());
        }
    }

    @Override
    public Model findById(Long id) throws Exception {
        try {
            String query = "Select ModelName, BrandId from model WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Model model = new Model();
                model.setId(id);
                model.setName(rs.getString("ModelName"));
                model.setBrand(new Brand(rs.getLong("BrandId")));

                rs.close();
                preparedStatement.close();
                db.confirmTransaction();
                return model;
            }

            throw new Exception("Model sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja modela po id-u!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<Model> findByQuery(String query) throws Exception {

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
            db.confirmTransaction();
            return models;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja modela!\n" + sqle.getMessage());
        }

    }

}
