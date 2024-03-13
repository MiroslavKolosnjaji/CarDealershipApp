package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.CarBodyType;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.query.SqlQueries;
import cardealershipapp.server.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleRepositoryImpl implements Repository<Vehicle, Long> {

    public static final Logger log = LoggerFactory.getLogger(VehicleRepositoryImpl.class);
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(Vehicle vehicle) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(vehicle.getViNumber(),
                    vehicle.getBodyType().toString(),
                    vehicle.getEngineDisplacement(),
                    vehicle.getEnginePower(),
                    vehicle.getYearOfProd(),
                    vehicle.getFuelType().toString(),
                    vehicle.getPrice(),
                    vehicle.getCurrency().toString(),
                    vehicle.getModel().getId(),
                    vehicle.getBusinessUnit().getId()));
            db.executeSqlUpdate(SqlQueries.Vehicles.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa vozila sa VIN brojem '" + vehicle.getViNumber() + "' u bazu podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom unosa vozila u bazu!");
        }
    }

    @Override
    public void update(Vehicle vehicle) throws RepositoryException {

        try {

            paramsQueue.addAll(List.of(vehicle.getViNumber(),
                    vehicle.getBodyType().toString(),
                    vehicle.getEngineDisplacement(),
                    vehicle.getEnginePower(),
                    vehicle.getYearOfProd(),
                    vehicle.getFuelType().toString(),
                    vehicle.getPrice(),
                    vehicle.getCurrency().toString(),
                    vehicle.getModel().getId(),
                    vehicle.getBusinessUnit().getId(),
                    vehicle.getId()));
            db.executeSqlUpdate(SqlQueries.Vehicles.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom ažuriranja vozila sa VIN brojem '" + vehicle.getViNumber() + "' u bazi podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka vozila u bazi!");
        }
    }

    @Override
    public void delete(Vehicle vehicle) throws RepositoryException {
        try {

            paramsQueue.add(vehicle.getId());
            db.executeSqlUpdate(SqlQueries.Vehicles.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja vozila sa VIN brojem '" + vehicle.getViNumber() + "' iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja vozila iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<Vehicle> vehicles) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(vehicles, SqlQueries.Vehicles.DELETE_MULTIPLE_ID);
            vehicles.forEach(v -> paramsQueue.add(v.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja '" + vehicles.size() + "' vozila iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja vise vozila iz baze!");
        }

    }

    @Override
    public List<Vehicle> getAll() throws RepositoryException {
        try {

            List<Vehicle> vehicles = new ArrayList<>();
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.Vehicles.SELECT_ALL);

            while (rs.next()) {
                Long vehicleId = rs.getLong("V.Id");
                String vin = rs.getString("V.ViNumber");
                CarBodyType bodyType = Enum.valueOf(CarBodyType.class, rs.getString("V.BodyType").toUpperCase());
                Integer engineCapacity = rs.getInt("V.EngDispl");
                Integer enginePower = rs.getInt("V.EngPowerKW");
                Integer year = rs.getInt("V.YearOfProd");
                FuelType fuel = FuelType.valueOf(rs.getString("V.FuelType").toUpperCase());
                BigDecimal price = rs.getBigDecimal("V.Price");
                Currency currency = Currency.valueOf(rs.getString("V.Currency"));

                //BRAND
                Brand brand = new Brand(rs.getLong("M.BrandId"));
                brand.setBrandName(rs.getString("BR.BrandName"));

                //MODEL
                Model model = new Model(rs.getLong("V.ModelId"));
                model.setName(rs.getString("M.ModelName"));
                model.setBrand(brand);

                //CITY
                City city = new City(rs.getLong("BU.CityId"));
                city.setName(rs.getString("C.CityName"));
                city.setZipCode(rs.getInt("C.ZipCode"));


                //BUSINESS UNIT
                BusinessUnit businessUnit = new BusinessUnit(rs.getLong("V.BusinessUId"));
                businessUnit.setName(rs.getString("BU.Name"));
                businessUnit.setCity(city);
                businessUnit.setCompanyRegId(rs.getString("BU.CompanyRegNum"));
                businessUnit.setTaxId(rs.getString("BU.TaxId"));
                businessUnit.setAddress(rs.getString("BU.Address"));
                businessUnit.setPhone(rs.getString("BU.Phone"));
                businessUnit.setEmail(rs.getString("BU.Email"));


                Vehicle vehicle = new Vehicle(vehicleId, model, vin, bodyType, engineCapacity, enginePower, year, fuel, price, currency, businessUnit);
                vehicles.add(vehicle);

            }
            rs.close();
            statement.close();
            return vehicles;

        } catch (SQLException sqle) {
            log.error("Greška prilikom učitavanja vozila iz baze podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja vozila iz baze!");
        }

    }

    @Override
    public Vehicle findById(Long id) throws RepositoryException, EntityNotFoundException {

        try {

            PreparedStatement prepStat = db.getConnection().prepareStatement(SqlQueries.Vehicles.SELECT_BY_ID);
            prepStat.setLong(1, id);
            ResultSet rs = prepStat.executeQuery();


            if (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(id);
                vehicle.setViNumber(rs.getString("ViNumber"));
                vehicle.setBodyType(CarBodyType.valueOf(rs.getString("BodyType")));
                vehicle.setEngineDisplacement(rs.getInt("EngDispl"));
                vehicle.setEnginePower(rs.getInt("EngPowerKW"));
                vehicle.setYearOfProd(rs.getInt("YearOfProd"));
                vehicle.setFuelType(FuelType.valueOf(rs.getString("FuelType")));
                vehicle.setPrice(rs.getBigDecimal("Price"));
                vehicle.setCurrency(Currency.valueOf(rs.getString("Currency")));
                vehicle.setModel(new Model(rs.getLong("ModelId")));
                vehicle.setBusinessUnit(new BusinessUnit(rs.getLong("BusinessUnitId")));

                rs.close();
                prepStat.close();
                return vehicle;
            }

            throw new EntityNotFoundException("Vozilo sa ovim Id brojem ne postoji");

        } catch (SQLException sqle) {
            log.error("Greška prilikom pretraživanja vozila po ID '" + id + "' u bazi podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrage vozila po ID broju!");
        }
    }

    @Override
    public List<Vehicle> findByQuery(String query) throws RepositoryException {
        try {

            List<Vehicle> vehicles = new ArrayList<>();
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getLong("Id"));
                vehicle.setViNumber(rs.getString("ViNumber"));
                vehicle.setBodyType(CarBodyType.valueOf(rs.getString("BodyType")));
                vehicle.setEngineDisplacement(rs.getInt("EngDispl"));
                vehicle.setEnginePower(rs.getInt("EngPowerKW"));
                vehicle.setYearOfProd(rs.getInt("YearOfProd"));
                vehicle.setFuelType(FuelType.valueOf(rs.getString("FuelType")));
                vehicle.setPrice(rs.getBigDecimal("Price"));
                vehicle.setCurrency(Currency.valueOf(rs.getString("Currency")));
                vehicle.setModel(new Model(rs.getLong("ModelId")));
                vehicle.setBusinessUnit(new BusinessUnit(rs.getLong("BusinessUId")));

                vehicles.add(vehicle);
            }

            rs.close();
            statement.close();
            return vehicles;

        } catch (SQLException sqle) {
            log.error(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + query + " u metodi findByQeury klase: " +this.getClass().getSimpleName()+ " : " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja vozila po upitu!");
        }
    }

}
