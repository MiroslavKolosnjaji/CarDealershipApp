package cardealershipapp.server.repository.impl;

import cardealershipapp.server.database.DataBase;
import java.sql.*;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.server.repository.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class BrandRepositoryImpl implements Repository<Brand, Long> {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void add(Brand brand) throws Exception {
        try {
            String query = "INSERT INTO Brand(BrandName) VALUES(?)";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.executeUpdate();

            ResultSet rsId = preparedStatement.getGeneratedKeys();
            if (rsId.next()) {
                brand.setId(rsId.getLong("Id"));
            }
            rsId.close();
            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom dodavanja novog brenda u bazu!");
        }
    }

    @Override
    public void update(Brand brand) throws Exception {
        try {
            String query = "UPDATE brand SET BrandName = ? WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.setLong(2, brand.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka brenda u bazi!");

        }
    }

    @Override
    public void delete(Brand brand) throws Exception {
        try {
            String query = "DELETE FROM brand WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, brand.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja brenda iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<Brand> brands) throws Exception {
        try {

            String query = generateDeleteMultiQuery(brands);
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            
            int counter = 1;
            for (Brand brand : brands) {
                preparedStatement.setLong(counter++, brand.getId());
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

    private String generateDeleteMultiQuery(List<Brand> brands) {
        StringBuffer bufferedQuery = new StringBuffer("DELETE FROM brand WHERE Id IN(");

        for (int i = 0; i < brands.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
    }

    @Override
    public List<Brand> getAll() throws Exception {
        try {

            List<Brand> brands = new ArrayList<>();

            String query = "SELECT Id, BrandName FROM brand";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("Id");
                String name = rs.getString("BrandName");

                Brand b = new Brand(id, name);

                brands.add(b);

            }

            rs.close();
            statement.close();
            db.confirmTransaction();

            return brands;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom ucitavanja brendova iz baze!");
        }

    }

    @Override
    public Brand findById(Long id) throws Exception {
        try {

            String query = "SELECT Id, BrandName FROM brand where Id = ?";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setLong(1, id);
            ResultSet rs = prepStat.executeQuery();

            if (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getLong("Id"));
                brand.setBrandName(rs.getString("BrandName"));

                prepStat.close();
                db.confirmTransaction();
                return brand;

            } else {
                throw new Exception("Marka sa ovim Id brojem ne postoji!");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom ucitavanja podataka marke!");
        }
    }

    @Override
    public List<Brand> findByQuery(String query) throws Exception {

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
            db.confirmTransaction();
            return brands;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja marke!");
        }
    }

}
