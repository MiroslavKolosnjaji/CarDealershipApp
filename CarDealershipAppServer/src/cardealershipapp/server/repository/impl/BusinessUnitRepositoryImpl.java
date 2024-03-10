package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.City;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.query.SqlQueries;

import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class BusinessUnitRepositoryImpl implements Repository<BusinessUnit, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(BusinessUnit businessUnit) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(businessUnit.getName(),
                    businessUnit.getCompanyRegId(),
                    businessUnit.getTaxId(),
                    businessUnit.getAddress(),
                    businessUnit.getCity().getId(),
                    businessUnit.getPhone(),
                    businessUnit.getEmail()));
            db.executeSqlUpdate(SqlQueries.BusinessUnits.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom unosa poslovne jedinice u bazu!\n" + dbe.getMessage());
        }
    }

    @Override
    public void update(BusinessUnit businessUnit) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(businessUnit.getName(),
                    businessUnit.getCompanyRegId(),
                    businessUnit.getTaxId(),
                    businessUnit.getAddress(),
                    businessUnit.getCity().getId(),
                    businessUnit.getPhone(),
                    businessUnit.getEmail(),
                    businessUnit.getId()));
            db.executeSqlUpdate(SqlQueries.BusinessUnits.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka poslovne jedinice u bazi!");
        }
    }

    @Override
    public void delete(BusinessUnit businessUnit) throws RepositoryException {
        try {

            paramsQueue.add(businessUnit.getId());
            db.executeSqlUpdate(SqlQueries.BusinessUnits.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja poslovne jedinice iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<BusinessUnit> businessUnits) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(businessUnits, SqlQueries.BusinessUnits.DELETE_MULTIPLE_ID);
            businessUnits.forEach(businessUnit -> paramsQueue.add(businessUnit.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja vise poslovnih jedinica iz baze!");
        }

    }


    @Override
    public List<BusinessUnit> getAll() throws RepositoryException {

        try {

            List<BusinessUnit> businessUnits = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.BusinessUnits.SELECT_ALL);
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
            return businessUnits;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja poslovnih jedinica iz baze!");
        }
    }

    @Override
    public BusinessUnit findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement prepStat = db.getConnection().prepareStatement(SqlQueries.BusinessUnits.SELECT_BY_ID);
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
                return businessUnit;
            }

            throw new EntityNotFoundException("Poslovna jedinica sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja poslovne jedinice po ID broju!");
        }
    }

    @Override
    public List<BusinessUnit> findByQuery(String query) throws RepositoryException {
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
            return businessUnits;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja poslovne jedinice po upitu!");
        }
    }

}
