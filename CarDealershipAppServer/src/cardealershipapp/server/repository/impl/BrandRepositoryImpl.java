package cardealershipapp.server.repository.impl;

import cardealershipapp.server.database.DataBase;

import java.sql.*;

import cardealershipapp.common.domain.Brand;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.repository.Repository;

import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class BrandRepositoryImpl implements Repository<Brand, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void add(Brand brand) throws Exception {
        try {
            String query = "INSERT INTO Brand(BrandName) VALUES(?)";
            paramsQueue.add(brand.getBrandName());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja novog brenda u bazu!");
        }
    }

    @Override
    public void update(Brand brand) throws Exception {
        try {
            String query = "UPDATE brand SET BrandName = ? WHERE Id = ?";
            paramsQueue.addAll(List.of(brand.getBrandName(), brand.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka brenda u bazi!");

        }
    }

    @Override
    public void delete(Brand brand) throws Exception {
        try {
            String query = "DELETE FROM brand WHERE Id = ?";
            paramsQueue.add(brand.getId());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja brenda iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<Brand> brands) throws Exception {
        try {
            String query = db.generateDeleteMultiQuery(brands, "brand");
            paramsQueue.addAll(brands);
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }
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
