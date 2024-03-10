package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.Equipment;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.query.SqlQueries;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class EquipmentRepositoryImpl implements Repository<Equipment, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(Equipment equipment) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(equipment.getBrand().getId(),
                    equipment.getName(),
                    equipment.getPrice(),
                    equipment.getCurrency().toString()));
            db.executeSqlUpdate(SqlQueries.Equipments.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom dodavanja opreme u bazu!");
        }
    }

    @Override
    public void update(Equipment equipment) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(equipment.getBrand().getId(),
                    equipment.getName(),
                    equipment.getPrice(),
                    equipment.getCurrency().toString(),
                    equipment.getId()));
            db.executeSqlUpdate(SqlQueries.Equipments.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka opreme!");
        }
    }

    @Override
    public void delete(Equipment equipment) throws RepositoryException {
        try {

            paramsQueue.add(equipment.getId());
            db.executeSqlUpdate(SqlQueries.Equipments.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja opreme iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<Equipment> equipments) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(equipments, SqlQueries.Equipments.DELETE_MULTIPLE_ID);
            equipments.forEach(equipment -> paramsQueue.add(equipment.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja vise oprema iz baze!");
        }

    }

    @Override
    public List<Equipment> getAll() throws RepositoryException {
        try {

            List<Equipment> equipments = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.Equipments.SELECT_ALL);

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
            return equipments;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja oprema iz baze!");
        }
    }

    @Override
    public Equipment findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(SqlQueries.Equipments.SELECT_BY_ID);
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
                return equipment;
            }

            throw new EntityNotFoundException("Oprema sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrage opreme po ID broju!");
        }
    }

    @Override
    public List<Equipment> findByQuery(String query) throws RepositoryException {
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
            return equipments;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja opreme po upitu!");
        }
    }
}
