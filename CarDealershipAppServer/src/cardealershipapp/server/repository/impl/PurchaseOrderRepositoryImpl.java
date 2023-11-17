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
import cardealershipapp.server.repository.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderRepositoryImpl extends PurchaseOrderItemRepositoryImpl implements Repository<PurchaseOrder, Long> {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void add(PurchaseOrder purchaseOrder) throws Exception {
        try {
            String query = "INSERT INTO purchase_order(PurchaseDate, CustomerId, VehicleId, SalesPersonId) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, new java.sql.Date(Date.valueOf(purchaseOrder.getDate()).getTime()));
            preparedStatement.setLong(2, purchaseOrder.getCustomer().getId());
            preparedStatement.setLong(3, purchaseOrder.getVehicle().getId());
            preparedStatement.setLong(4, purchaseOrder.getSalesPerson().getId());
            preparedStatement.executeUpdate();

            ResultSet rsId = preparedStatement.getGeneratedKeys();

            if (rsId.next()) {
                purchaseOrder.setPurchaseOrderNum(rsId.getLong(1));
            }
            
            rsId.close();
            preparedStatement.close();
            
            purchaseOrder.getPurchaseOrderItems().forEach(purchaseOrderItem -> purchaseOrderItem.setPurchaseOrder(purchaseOrder));
            addItems(purchaseOrder.getPurchaseOrderItems());

            db.confirmTransaction();

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja porudzbenice u bazu!\n" + sqle.getMessage());
        }
    }

    @Override
    public void update(PurchaseOrder purchaseOrder) throws Exception {
        try {
            String query = "UPDATE purchase_order SET PurchaseDate = ?, CustomerId = ?, VehicleId = ?, SalesPersonId = ? WHERE PONumber = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setDate(1, new java.sql.Date(Date.valueOf(purchaseOrder.getDate()).getTime()));
            preparedStatement.setLong(2, purchaseOrder.getCustomer().getId());
            preparedStatement.setLong(3, purchaseOrder.getVehicle().getId());
            preparedStatement.setLong(4, purchaseOrder.getSalesPerson().getId());
            preparedStatement.setLong(5, purchaseOrder.getPurchaseOrderNum());
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            db.confirmTransaction();

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka porudzbenice!\n" + sqle.getMessage());
        }
    }

    @Override
    public void delete(PurchaseOrder purchaseOrder) throws Exception {
        try {
            String query = "DELETE FROM purchase_order WHERE PONumber = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, purchaseOrder.getPurchaseOrderNum());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Dogodila se greska prilikom brisanja porudzbenice!\n" + sqle.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<PurchaseOrder> purchaseOrders) throws Exception {
        try {

            String query = generateDeleteMultiQuery(purchaseOrders);
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            
            int counter = 1;
            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                preparedStatement.setLong(counter++, purchaseOrder.getPurchaseOrderNum());
            }
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            db.confirmTransaction();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja porudzbenica iz baze!");
        }

    }

    private String generateDeleteMultiQuery(List<PurchaseOrder> purchaseOrders) {
        StringBuffer bufferedQuery = new StringBuffer("DELETE FROM purchase_order WHERE PoNumber IN(");

        for (int i = 0; i < purchaseOrders.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
    }

    @Override
    public List<PurchaseOrder> getAll() throws Exception {
        try {
            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            String query = """
                           SELECT PO.PONumber, PO.PurchaseDate, PO.CustomerId, PO.VehicleId, PO.SalesPersonId, CU.Name, CU.CompanyName, CU.Address, CU.Phone, CU.Email,
                           V.ViNumber, V.BodyType, V.EngDispl, V.EngPowerKW, V.YearOfProd, V.FuelType, V.Price, V.Currency, V.ModelId, V.BusinessUId, M.ModelName, M.BrandId, BR.BrandName, BU.Name, BU.CompanyRegNum,
                           BU.TaxId, BU.Address, BU.CityId, BU.Phone, BU.Email, C.ZipCode, C.CityName, U.FirstName, U.LastName FROM PURCHASE_ORDER PO JOIN Customer CU ON PO.CustomerId = CU.Id 
                           JOIN Vehicle V ON PO.VehicleId = V.Id JOIN `User` U ON PO.SalesPersonId = U.Id JOIN model M ON V.ModelId = M.Id JOIN brand BR ON M.BrandId = BR.Id JOIN business_unit BU ON V.BusinessUId = BU.Id
                            JOIN city C ON BU.CityId = C.Id;""";

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

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

                PurchaseOrder purchaseOrder = new PurchaseOrder(purchaseOrderNumb, purchaseDate, customer, vehicle, user, null);

                purchaseOrders.add(purchaseOrder);
            }

            rs.close();
            statement.close();
            db.confirmTransaction();
            return purchaseOrders;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Dogodila se greska prilikom ucitavanja porudzbenica iz baze!\n" + sqle.getMessage());
        }
    }

    @Override
    public PurchaseOrder findById(Long id) throws Exception {
        try {
            String query = "SELECT PurchaseDate, CustomerId, VehicleId, SalesPersonId FROM purchase_order WHERE PONumber = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setPurchaseOrderNum(id);
                purchaseOrder.setDate(rs.getDate("PurchaseDate").toLocalDate());
                purchaseOrder.setCustomer(new Customer(rs.getLong("CustomerId")));
                purchaseOrder.setVehicle(new Vehicle(rs.getLong("VehicleId")));
                purchaseOrder.setSalesPerson(new User(rs.getLong("SalesPersonId")));

                rs.close();
                preparedStatement.close();
                db.confirmTransaction();
                return purchaseOrder;
            }

            throw new Exception("Ne postoji porudzbenica sa ovim id brojem!");

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom pretrage porudzbenice po id-ju!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<PurchaseOrder> findByQuery(String query) throws Exception {

        try {
            List<PurchaseOrder> purchaseOrders = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setPurchaseOrderNum(rs.getLong("PONumber"));
                purchaseOrder.setDate(rs.getDate("PurchaseDate").toLocalDate());
                purchaseOrder.setCustomer(new Customer(rs.getLong("CustomerId")));
                purchaseOrder.setVehicle(new Vehicle(rs.getLong("VehicleId")));
                purchaseOrder.setSalesPerson(new User(rs.getLong("SalesPersonId")));

                purchaseOrders.add(purchaseOrder);
            }

            rs.close();
            statement.close();
            db.confirmTransaction();
            return purchaseOrders;

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Dogodila se greska prilikom pretrage porudzbenica!\n" + sqle.getMessage());
        }

    }
}
