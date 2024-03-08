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
import cardealershipapp.server.repository.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleRepositoryImpl implements Repository<Vehicle, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque();

    @Override
    public void add(Vehicle vehicle) throws Exception {
        try {
            String query = "INSERT INTO vehicle(ViNumber, BodyType, EngDispl, EngPowerKW, YearOFProd, FuelType, Price, Currency, ModelId, BusinessUId) VALUES(?,?,?,?,?,?,?,?,?,?)";
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

            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja novog vozila u bazu!\n" + dbe.getMessage());
        }
    }

    @Override
    public void update(Vehicle vehicle) throws Exception {

        try {
            String query = "UPDATE vehicle SET ViNumber = ?, BodyType = ?, EngDispl = ?, EngPowerKW = ?, YearOFProd = ?, FuelType = ?, Price = ?, Currency = ?, ModelId = ?, BusinessUId = ? WHERE Id = ?";
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

            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka vozila u bazi!\n" + dbe.getMessage());
        }
    }

    @Override
    public void delete(Vehicle vehicle) throws Exception {
        try {
            String query = "DELETE FROM vehicle WHERE Id = ?";
            paramsQueue.add(vehicle.getId());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja vozila iz baze!\n" + dbe.getMessage());
        }
    }

    @Override
    public void deleteMultiple(List<Vehicle> vehicles) throws Exception {
        try {

            String query = db.generateDeleteMultiQuery(vehicles, "vehicle");
            vehicles.forEach(v -> paramsQueue.add(v.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja vozila iz baze!");
        }

    }

    @Override
    public List<Vehicle> getAll() throws Exception {
        try {
            List<Vehicle> vehicles = new ArrayList<>();

            String query = "SELECT V.Id, V.ViNumber, V.BodyType, V.EngDispl, V.EngPowerKW, V.YearOfProd, V.FuelType, V.Price, V.Currency, V.ModelId, V.BusinessUId, "
                    + "M.ModelName, M.BrandId, BR.BrandName, BU.Name, BU.CompanyRegNum, BU.TaxId, BU.Address, BU.CityId, BU.Phone, BU.Email, C.ZipCode, C.CityName FROM vehicle V JOIN model M ON V.ModelId = M.Id "
                    + "JOIN brand BR ON M.BrandId = BR.Id JOIN business_unit BU ON V.BusinessUId = BU.Id JOIN city C ON BU.CityId = C.Id;";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

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
            db.confirmTransaction();
            return vehicles;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka vozila u bazi!\n" + sqle.getMessage());
        }
    }

    @Override
    public Vehicle findById(Long id) throws Exception {

        try {
            String query = "SELECT ViNumber, BodyType, EngDispl, EngPowerKW, YearOfProd, FuelType, Price, Currency, ModelId, BusinessUId WHERE Id = ?";
            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
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
                db.confirmTransaction();
                return vehicle;
            }

            throw new Exception("Vozilo sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja vozila po id-u!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<Vehicle> findByQuery(String query) throws Exception {

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
            db.confirmTransaction();
            return vehicles;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja vozila po id-u!\n" + sqle.getMessage());
        }
    }

}
