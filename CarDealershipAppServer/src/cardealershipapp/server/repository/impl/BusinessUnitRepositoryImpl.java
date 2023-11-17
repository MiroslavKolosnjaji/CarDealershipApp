package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.City;
import cardealershipapp.server.repository.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class BusinessUnitRepositoryImpl implements Repository<BusinessUnit, Long> {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void add(BusinessUnit businessUnit) throws Exception {
        try {
            String query = "INSERT INTO business_unit(`Name`, CompanyRegNum, TaxId, Address, CityId, Phone, Email) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement prepStat = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepStat.setString(1, businessUnit.getName());
            prepStat.setString(2, businessUnit.getCompanyRegId());
            prepStat.setString(3, businessUnit.getTaxId());
            prepStat.setString(4, businessUnit.getAddress());
            prepStat.setLong(5, businessUnit.getCity().getId());
            prepStat.setString(6, businessUnit.getPhone());
            prepStat.setString(7, businessUnit.getEmail());
            prepStat.executeUpdate();

            ResultSet rsId = prepStat.getGeneratedKeys();
            if (rsId.next()) {
                businessUnit.setId(rsId.getLong(1));
            }

            rsId.close();
            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja nove poslovne jedinice u bazu!\n" + sqle.getMessage());
        }
    }

    @Override
    public void update(BusinessUnit businessUnit) throws Exception {
        try {
            String query = "UPDATE business_unit SET `Name` = ?, CompanyRegNum = ?, TaxId = ?, Address = ?,  CityId = ?, Phone = ?, Email = ? WHERE Id = ?";
            PreparedStatement prepStat = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepStat.setString(1, businessUnit.getName());
            prepStat.setString(2, businessUnit.getCompanyRegId());
            prepStat.setString(3, businessUnit.getTaxId());
            prepStat.setString(4, businessUnit.getAddress());
            prepStat.setLong(5, businessUnit.getCity().getId());
            prepStat.setString(6, businessUnit.getPhone());
            prepStat.setString(7, businessUnit.getEmail());
            prepStat.setLong(8, businessUnit.getId());
            prepStat.executeUpdate();

            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka poslovne jedinice u bazi!\n" + sqle.getMessage());
        }
    }

    @Override
    public void delete(BusinessUnit businessUnit) throws Exception {
        try {
            String query = "DELETE FROM business_unit WHERE Id = ?";
            PreparedStatement prepStat = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepStat.setLong(1, businessUnit.getId());
            prepStat.executeUpdate();

            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja poslovne jedinice iz baze!\n" + sqle.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<BusinessUnit> businessUnits) throws Exception {
        try {

            String query = generateDeleteMultiQuery(businessUnits);
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            
            int counter = 1;
            for (BusinessUnit businessUnit : businessUnits) {
                preparedStatement.setLong(counter++, businessUnit.getId());
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

    private String generateDeleteMultiQuery(List<BusinessUnit> businessUnits) {
        StringBuffer bufferedQuery = new StringBuffer("DELETE FROM business_unit WHERE Id IN(");

        for (int i = 0; i < businessUnits.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
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
