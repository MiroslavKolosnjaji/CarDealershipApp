package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.City;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.repository.Repository;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class BusinessUnitRepositoryImpl implements Repository<BusinessUnit, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void add(BusinessUnit businessUnit) throws Exception {
        try {
            String query = "INSERT INTO business_unit(`Name`, CompanyRegNum, TaxId, Address, CityId, Phone, Email) VALUES(?,?,?,?,?,?,?)";
            paramsQueue.addAll(List.of(businessUnit.getName(),
                    businessUnit.getCompanyRegId(),
                    businessUnit.getTaxId(),
                    businessUnit.getAddress(),
                    businessUnit.getCity().getId(),
                    businessUnit.getPhone(),
                    businessUnit.getEmail()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja nove poslovne jedinice u bazu!\n" + dbe.getMessage());
        }
    }

    @Override
    public void update(BusinessUnit businessUnit) throws Exception {
        try {
            String query = "UPDATE business_unit SET `Name` = ?, CompanyRegNum = ?, TaxId = ?, Address = ?,  CityId = ?, Phone = ?, Email = ? WHERE Id = ?";
            paramsQueue.addAll(List.of(businessUnit.getName(),
                    businessUnit.getCompanyRegId(),
                    businessUnit.getTaxId(),
                    businessUnit.getAddress(),
                    businessUnit.getCity().getId(),
                    businessUnit.getPhone(),
                    businessUnit.getEmail(),
                    businessUnit.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka poslovne jedinice u bazi!\n" + dbe.getMessage());
        }
    }

    @Override
    public void delete(BusinessUnit businessUnit) throws Exception {
        try {
            String query = "DELETE FROM business_unit WHERE Id = ?";
            paramsQueue.add(businessUnit.getId());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja poslovne jedinice iz baze!\n" + dbe.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<BusinessUnit> businessUnits) throws Exception {
        try {
            String query = db.generateDeleteMultiQuery(businessUnits, "business_unit");
            businessUnits.forEach(businessUnit -> paramsQueue.add(businessUnit.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }


    @Override
    public List<BusinessUnit> getAll() throws Exception {

        try {
            List<BusinessUnit> businessUnits = new ArrayList<>();
            String query = "SELECT BU.Id, BU.Name, BU.CompanyRegNum, BU.TaxId, BU.Address, BU.CityId, BU.Phone, BU.Email, C.ZipCode, C.CityName FROM business_unit BU JOIN city C ON BU.CityId = C.Id";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Long id = rs.getLong("BU.Id");
                String companyName = rs.getString("BU.Name");
                String companyRegId = rs.getString("BU.CompanyRegNum");
                String TaxId = rs.getString("BU.TaxId");
                String Address = rs.getString("BU.Address");
                Long CityId = rs.getLong("BU.CityId");
                String Phone = rs.getString("BU.Phone");
                String email = rs.getString("BU.Email");
                Integer zipCode = rs.getInt("C.ZipCode");
                String cityName = rs.getString("C.CityName");

                City city = new City(CityId, zipCode, cityName);
                BusinessUnit businessUnit = new BusinessUnit(id, companyName, companyRegId, TaxId, city, Address, Phone, email);
                businessUnits.add(businessUnit);
            }
            rs.close();
            statement.close();
            db.confirmTransaction();
            return businessUnits;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja poslovne jedinice iz baze!\n" + sqle.getMessage());
        }
    }

    @Override
    public BusinessUnit findById(Long id) throws Exception {
        try {
            String query = "SELECT Name, CompanyRegNum, TaxId, Address, CityId, Phone, Email FROM business_unit WHERE Id = ?";
            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setLong(1, id);

            ResultSet rs = prepStat.executeQuery();

            if (rs.next()) {
                BusinessUnit businessUnit = new BusinessUnit();
                businessUnit.setId(id);
                businessUnit.setName(rs.getString("Name"));
                businessUnit.setCompanyRegId(rs.getString("CompanyRegNum"));
                businessUnit.setTaxId(rs.getString("TaxId"));
                businessUnit.setAddress(rs.getString("Address"));
                businessUnit.setCity(new City(rs.getLong("CityId")));
                businessUnit.setPhone(rs.getString("Phone"));
                businessUnit.setEmail(rs.getString("Email"));

                rs.close();
                prepStat.close();
                db.confirmTransaction();
                return businessUnit;
            }

            throw new Exception("Poslovna jedinica sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja poslovne jedinice po id-u!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<BusinessUnit> findByQuery(String query) throws Exception {
        try {
            List<BusinessUnit> businessUnits = new ArrayList<>();
            PreparedStatement prepStat = db.getConnection().prepareStatement(query);

            ResultSet rs = prepStat.executeQuery();

            while (rs.next()) {
                BusinessUnit businessUnit = new BusinessUnit();
                businessUnit.setName(rs.getString("Name"));
                businessUnit.setCompanyRegId(rs.getString("CompanyRegNum"));
                businessUnit.setTaxId(rs.getString("TaxId"));
                businessUnit.setAddress(rs.getString("Address"));
                businessUnit.setCity(new City(rs.getLong("CityId")));
                businessUnit.setPhone(rs.getString("Phone"));
                businessUnit.setEmail(rs.getString("Email"));
            }

            rs.close();
            prepStat.close();
            db.confirmTransaction();
            return businessUnits;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja poslovne jedinice po id-u!\n" + sqle.getMessage());
        }
    }

}
