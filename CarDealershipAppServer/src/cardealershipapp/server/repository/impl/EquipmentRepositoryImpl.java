package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.Equipment;
import cardealershipapp.server.repository.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class EquipmentRepositoryImpl implements Repository<Equipment, Long> {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void add(Equipment equipment) throws Exception {
        try {
            String query = "INSERT INTO equipment(BrandId, Name, Price, Currency) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, equipment.getBrand().getId());
            preparedStatement.setString(2, equipment.getName());
            preparedStatement.setBigDecimal(3, equipment.getPrice());
            preparedStatement.setString(4, equipment.getCurrency().toString());
            preparedStatement.executeUpdate();

            ResultSet rsId = preparedStatement.getGeneratedKeys();

            if (rsId.next()) {
                equipment.setId(rsId.getLong(1));
            }

            rsId.close();
            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja nove opreme u bazu!\n" + sqle.getMessage());
        }
    }

    @Override
    public void update(Equipment equipment) throws Exception {
        try {
            String query = "UPDATE equipment SET BrandId = ?, 'Name' = ?, Price = ?, Currency = ? WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, equipment.getBrand().getId());
            preparedStatement.setString(2, equipment.getName());
            preparedStatement.setBigDecimal(3, equipment.getPrice());
            preparedStatement.setString(4, equipment.getCurrency().toString());
            preparedStatement.setLong(5, equipment.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka opreme!\n" + sqle.getMessage());
        }
    }

    @Override
    public void delete(Equipment equipment) throws Exception {
        try {
            String query = "DELETE FROM equipment WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, equipment.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja opreme iz baze!\n" + sqle.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<Equipment> equipments) throws Exception {
        try {

            String query = generateDeleteMultiQuery(equipments);
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            
            int counter = 1;
            for (Equipment equipment : equipments) {
                preparedStatement.setLong(counter++, equipment.getId());
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

    private String generateDeleteMultiQuery(List<Equipment> equipments) {
        StringBuffer bufferedQuery = new StringBuffer("DELETE FROM equipment WHERE Id IN(");

        for (int i = 0; i < equipments.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
    }

    @Override
    public List<Equipment> getAll() throws Exception {
        try {
            List<Equipment> equipments = new ArrayList<>();

            String query = "SELECT E.Id, B.Id, B.BrandName, E.Name, E.Price, E.Currency FROM equipment E JOIN Brand B ON E.BrandId = B.Id";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long equipmentId = rs.getLong("E.Id");
                Long brandId = rs.getLong("B.Id");
                String brandName = rs.getString("B.BrandName");
                String equipmentName = rs.getString("E.Name");
                BigDecimal equipmentPrice = rs.getBigDecimal("E.Price");
                Currency priceCurrency = Currency.valueOf(rs.getString("E.Currency"));

                Brand brand = new Brand(brandId, brandName);
                Equipment e = new Equipment(equipmentId, brand, equipmentName, equipmentPrice, priceCurrency);

                equipments.add(e);
            }

            rs.close();
            statement.close();
            db.confirmTransaction();
            return equipments;
            
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom ucitavanja oprema iz baze!\n" + sqle.getMessage());
        }
    }

    @Override
    public Equipment findById(Long id) throws Exception {
        try {
            String query = "Select Id, BrandId, 'Name', Price, Currency FROM equipment WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setId(id);
                equipment.setName(rs.getString("Name"));
                equipment.setPrice(rs.getBigDecimal("Price"));
                equipment.setCurrency(Currency.valueOf(rs.getString("Currency")));

                //BRAND
                Brand brand = new Brand(rs.getLong("BrandId"));
                equipment.setBrand(brand);
                
                rs.close();
                preparedStatement.close();
                db.confirmTransaction();
                return equipment;
            }

            throw new Exception("Oprema sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom pretrage opreme po id-u!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<Equipment> findByQuery(String query) throws Exception {

        try {
            List<Equipment> equipments = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setId(rs.getLong("Id"));
                equipment.setName(rs.getString("Name"));
                equipment.setPrice(rs.getBigDecimal("Price"));
                equipment.setCurrency(Currency.valueOf(rs.getString("Currency")));

                //BRAND
                Brand brand = new Brand(rs.getLong("BrandId"));
                equipment.setBrand(brand);

                equipments.add(equipment);
            }

            rs.close();
            statement.close();
            db.confirmTransaction();
            return equipments;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom pretrazivanja opreme!\n" + sqle.getMessage());
        }
    }
}
