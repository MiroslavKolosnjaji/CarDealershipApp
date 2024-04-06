package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.CarBodyType;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.ExtendedRepository;
import cardealershipapp.server.repository.query.SqlQueries;
import cardealershipapp.server.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderRepositoryImpl implements ExtendedRepository<PurchaseOrder, Long> {

    public static final Logger log = LoggerFactory.getLogger(PurchaseOrderRepositoryImpl.class);
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(PurchaseOrder purchaseOrder) throws RepositoryException {
        //TODO Implement this method if necessary
        throw new UnsupportedOperationException(ExceptionUtils.UNSUPPORTED_OPERATION_MESSAGE);
    }

    @Override
    public PurchaseOrder saveAndReturn(PurchaseOrder purchaseOrder) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(new java.sql.Date(Date.valueOf(purchaseOrder.getDate()).getTime()),
                    purchaseOrder.getCustomer().getId(),
                    purchaseOrder.getVehicle().getId(),
                    purchaseOrder.getSalesPerson().getId()));

            Long id = db.executeSqlUpdateAndGenerateKey(SqlQueries.PurchaseOrders.INSERT, paramsQueue);

            purchaseOrder.setPurchaseOrderNum(id);
            purchaseOrder.getPurchaseOrderItems().forEach(purchaseOrderItem -> purchaseOrderItem.setPurchaseOrder(purchaseOrder));
            return purchaseOrder;

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa porudžbenice u bazu podataka za vozilo sa VIN brojem '" + purchaseOrder.getVehicle().getViNumber() + "' : " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom dodavanja porudzbenice u bazu!");
        }
    }

    @Override
    public void update(PurchaseOrder purchaseOrder) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(new java.sql.Date(Date.valueOf(purchaseOrder.getDate()).getTime()),
                    purchaseOrder.getCustomer().getId(),
                    purchaseOrder.getVehicle().getId(),
                    purchaseOrder.getSalesPerson().getId(),
                    purchaseOrder.getPurchaseOrderNum()));
            db.executeSqlUpdate(SqlQueries.PurchaseOrders.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom ažuriranje porudžbenice u bazi podataka za vozilo sa VIN brojem '" + purchaseOrder.getVehicle().getViNumber() + "' : " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka porudzbenice!");
        }
    }

    @Override
    public void delete(PurchaseOrder purchaseOrder) throws RepositoryException {
        try {

            paramsQueue.add(purchaseOrder.getPurchaseOrderNum());
            db.executeSqlUpdate(SqlQueries.PurchaseOrders.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja porudžbenice u bazi podataka za vozilo sa VIN brojem '" + purchaseOrder.getVehicle().getViNumber() + "' : " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Dogodila se greska prilikom brisanja porudzbenice!");
        }
    }

    @Override
    public void deleteMultiple(List<PurchaseOrder> purchaseOrders) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(purchaseOrders, SqlQueries.PurchaseOrders.DELETE_MULTIPLE_ID);
            purchaseOrders.forEach(purchaseOrder -> paramsQueue.add(purchaseOrder.getPurchaseOrderNum()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja '" + purchaseOrders.size() + "' porudžbenica iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja vise porudzbenica iz baze!");
        }

    }

    @Override
    public List<PurchaseOrder> getAll() throws RepositoryException {
        try {

            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.PurchaseOrders.SELECT_ALL);

            while (rs.next()) {
                purchaseOrders.add(getFullPurchaseOrderFromResultSet(rs));
            }

            rs.close();
            statement.close();
            return purchaseOrders;
        } catch (SQLException sqle) {
            log.error("Greška prilikom učitavanja porudžbenica iz baze podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Dogodila se greska prilikom ucitavanja porudzbenica iz baze!");
        }
    }

    @Override
    public PurchaseOrder findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(SqlQueries.PurchaseOrders.SELECT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                PurchaseOrder purchaseOrder = getPurchaseOrderFromResultSet(rs);

                rs.close();
                preparedStatement.close();

                return purchaseOrder;
            }

            throw new EntityNotFoundException("Porudzbenica ne postoji!");

        } catch (SQLException sqle) {
            log.error("Greška prilikom pretraživanja porudžbenice po ID '" + id + "' u bazi podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrage porudzbenice po ID broju!");
        }
    }

    @Override
    public List<PurchaseOrder> findByQuery(String query) throws RepositoryException {

        try {

            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                purchaseOrders.add(getPurchaseOrderFromResultSet(rs));
            }

            rs.close();
            statement.close();
            return purchaseOrders;

        } catch (SQLException sqle) {
            log.error(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + query + " u metodi findByQeury klase: " +this.getClass().getSimpleName()+ " : " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Dogodila se greska prilikom pretrage porudzbenica po upitu!");
        }

    }

    private PurchaseOrder getFullPurchaseOrderFromResultSet(ResultSet rs) throws SQLException {
        Long purchaseOrderNumb = rs.getLong("PO.PONumber");
        LocalDate purchaseDate = rs.getDate("PO.Purchasedate").toLocalDate();

        //CUSTOMER
        Long customerId = rs.getLong("PO.CustomerId");
        String customerName = rs.getString("CU.Name");
        String customerCompName = rs.getString("CU.CompanyName");
        String customerAddress = rs.getString("CU.Address");
        String customerPhone = rs.getString("CU.Phone");
        String customerEmail = rs.getString("CU.Email");
        Customer customer = new Customer(customerId, customerName, customerCompName, customerAddress, customerPhone, customerEmail);

        //VEHICLE
        Long vehicleId = rs.getLong("PO.VehicleId");
        String vin = rs.getString("V.ViNumber");
        CarBodyType bodyType = Enum.valueOf(CarBodyType.class, rs.getString("V.BodyType").toUpperCase());
        Integer engineCapacity = rs.getInt("V.EngDispl");
        Integer enginePower = rs.getInt("V.EngPowerKW");
        Integer year = rs.getInt("V.YearOfProd");
        FuelType fuel = FuelType.valueOf(rs.getString("V.FuelType").toUpperCase());
        BigDecimal price = rs.getBigDecimal("V.Price");
        Currency currency = Currency.valueOf(rs.getString("V.Currency"));

        //BRAND
        Long brandId = rs.getLong("M.BrandId");
        String brandName = rs.getString("BR.BrandName");
        Brand brand = new Brand(brandId, brandName);

        //MODEL
        Long modelId = rs.getLong("V.ModelId");
        String modelName = rs.getString("M.ModelName");
        Model model = new Model(modelId, modelName, brand);

        //CITY
        Long cityId = rs.getLong("BU.CityId");
        String cityName = rs.getString("C.CityName");
        Integer zipCode = rs.getInt("C.ZipCode");
        City city = new City(cityId, zipCode, cityName);

        //BUSINESS UNIT
        Long businessId = rs.getLong("V.BusinessUId");
        String businessUName = rs.getString("BU.Name");
        String businessRegNum = rs.getString("BU.CompanyRegNum");
        String businessTaxId = rs.getString("BU.TaxId");
        String businessAddress = rs.getString("BU.Address");
        String phoneNumber = rs.getString("BU.Phone");
        String emailAddress = rs.getString("BU.Email");
        BusinessUnit businessUnit = new BusinessUnit(businessId, businessUName, businessRegNum, businessTaxId, city, businessAddress, phoneNumber, emailAddress);

        Vehicle vehicle = new Vehicle(vehicleId, model, vin, bodyType, engineCapacity, enginePower, year, fuel, price, currency, businessUnit);

        //SALES PERSON (USER)
        User user = new User();
        user.setFirstName(rs.getString("U.FirstName"));
        user.setLastName(rs.getString("U.LastName"));

        return new PurchaseOrder(purchaseOrderNumb, purchaseDate, customer, vehicle, user, null);
    }

    private PurchaseOrder getPurchaseOrderFromResultSet(ResultSet rs) throws SQLException {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderNum(rs.getLong("PONumber"));
        purchaseOrder.setDate(rs.getDate("PurchaseDate").toLocalDate());
        purchaseOrder.setCustomer(new Customer(rs.getLong("CustomerId")));
        purchaseOrder.setVehicle(new Vehicle(rs.getLong("VehicleId")));
        purchaseOrder.setSalesPerson(new User(rs.getLong("SalesPersonId")));
        return purchaseOrder;
    }
}
