package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.server.repository.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ModelRepositoryImpl implements Repository<Model, Long> {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void add(Model model) throws Exception {
        try {
            String query = "INSERT INTO model(ModelName, BrandId) VALUES(?,?)";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, model.getName());
            preparedStatement.setLong(2, model.getBrand().getId());
            preparedStatement.executeUpdate();

            ResultSet rsId = preparedStatement.getGeneratedKeys();
            if (rsId.next()) {
                model.setId(rsId.getLong(1));
            }
            rsId.close();
            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja novog modela u bazu!\n" + sqle.getMessage());
        }
    }

    @Override
    public void update(Model model) throws Exception {
        try {
            String query = "UPDATE model SET ModelName = ?, BrandId = ? WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setString(1, model.getName());
            preparedStatement.setLong(2, model.getBrand().getId());
            preparedStatement.setLong(3, model.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka modela u bazi!\n" + sqle.getMessage());
        }
    }

    @Override
    public void delete(Model model) throws Exception {
        try {
            String query = "DELETE FROM model WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, model.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja modela iz baze!\n" + sqle.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<Model> models )throws Exception {
        try {

            String query = generateDeleteMultiQuery(models);
            System.out.println("MultyQuery: " + query);
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            
            int counter = 1;
            for (Model model : models) {
                preparedStatement.setLong(counter++, model.getId());
            }
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            db.confirmTransaction();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }

    private String generateDeleteMultiQuery(List<Model> models) {
        StringBuffer bufferedQuery = new StringBuffer("DELETE FROM model WHERE Id IN(");

        for (int i = 0; i < models.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
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
