package cardealershipapp.server.repository.query;


public class SqlQueries {

    public static class Brands {

        public static final String INSERT = "INSERT INTO Brand(BrandName) VALUES(?)";
        public static final String UPDATE = "UPDATE brand SET BrandName = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM brand WHERE Id = ?";
        public static final String SELECT_ALL = "SELECT Id, BrandName FROM brand";
        public static final String SELECT_BY_ID = "SELECT Id, BrandName FROM brand where Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM Brand WHERE Id IN";

    }

    public static class BusinessUnits {

        public static final String INSERT = "INSERT INTO business_unit(`Name`, CompanyRegNum, TaxId, Address, CityId, Phone, Email) VALUES(?,?,?,?,?,?,?)";
        public static final String UPDATE = "UPDATE business_unit SET `Name` = ?, CompanyRegNum = ?, TaxId = ?, Address = ?,  CityId = ?, Phone = ?, Email = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM business_unit WHERE Id = ?";
        public static final String SELECT_ALL = "SELECT BU.Id, BU.Name, BU.CompanyRegNum, BU.TaxId, BU.Address, BU.CityId, BU.Phone, BU.Email, C.ZipCode, C.CityName FROM business_unit BU JOIN city C ON BU.CityId = C.Id";
        public static final String SELECT_BY_ID = "SELECT Name, CompanyRegNum, TaxId, Address, CityId, Phone, Email FROM business_unit WHERE Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM business_unit WHERE Id IN";

    }

    public static class Cities {

        public static final String INSERT = "INSERT INTO city(ZipCode, CityName) VALUES(?,?)";
        public static final String UPDATE = "UPDATE city SET ZipCode = ?, CityName = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM city WHERE Id = ?";
        public static final String SELECT_ALL = "SELECT Id, ZipCode, CityName FROM city";
        public static final String SELECT_BY_ID = "SELECT ZipCode, CityName FROM city where Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM city WHERE Id IN";

    }

    public static class Customers {

        public static final String INSERT = "INSERT INTO customer(Name, CompanyName, Address, Phone, Email) VALUES(?,?,?,?,?)";
        public static final String UPDATE = "UPDATE customer SET Name = ?, CompanyName = ?, Address = ?, Phone = ?, Email = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM customer WHERE Id = ?";
        public static final String SELECT_ALL = "SELECT Id, 'Name', CompanyName, Address, Phone, Email FROM customer";
        public static final String SELECT_BY_ID = "SELECT Id, Name, CompanyName, Address, Phone, Email FROM customer WHERE Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM customer WHERE Id IN";

    }

    public static class Equipments {

        public static final String INSERT = "INSERT INTO equipment(BrandId, Name, Price, Currency) VALUES(?,?,?,?)";
        public static final String UPDATE = "UPDATE equipment SET BrandId = ?, 'Name' = ?, Price = ?, Currency = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM equipment WHERE Id = ?";
        public static final String SELECT_ALL = "SELECT E.Id, B.Id, B.BrandName, E.Name, E.Price, E.Currency FROM equipment E JOIN Brand B ON E.BrandId = B.Id";
        public static final String SELECT_BY_ID = "SELECT Id, BrandId, 'Name', Price, Currency FROM equipment WHERE Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM equipment WHERE Id IN";

    }

    public static class Models {

        public static final String INSERT = "INSERT INTO model(ModelName, BrandId) VALUES(?,?)";
        public static final String UPDATE = "UPDATE model SET ModelName = ?, BrandId = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM model WHERE Id = ?";
        public static final String SELECT_ALL = "SELECT M.id, M.ModelName, M.BrandId, B.BrandName FROM model M JOIN Brand B ON M.BrandId = B.Id";
        public static final String SELECT_BY_ID = "SELECT Id, ModelName, BrandId from model WHERE Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM model WHERE Id IN";

    }

    public static class PurchaseOrderItems {

        public static final String INSERT = "INSERT INTO purchase_order_item(PONumber, OrdinalNumber, EquipmentId, Quantity) VALUES(?,?,?,?)";
        public static final String UPDATE = "UPDATE purchase_order_item SET EquipmentId = ?, Quantity = ? WHERE PONumber = ? AND OrdinalNumber = ?";
        public static final String DELETE_BY_ID = "DELETE FROM purchase_order_item WHERE PONumber = ?";
        public static final String SELECT_ALL = """
                SELECT PI.PONumber, PI.OrdinalNumber, PI.Quantity, PI.EquipmentId, E.Name, E.Price, E.Currency, E.BrandId, B.BrandName FROM purchase_order_item `PI` JOIN
                Equipment E ON PI.EquipmentId = E.Id JOIN Brand B ON E.BrandId = B.Id""";
        public static final String SELECT_BY_ID = "SELECT PONumber, OrdinalNumber, EquipmentId, Quantity FROM purchase_order_item WHERE PONumber = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM purchase_order_item WHERE PONumber IN";

    }

    public static class PurchaseOrders {

        public static final String INSERT = "INSERT INTO purchase_order(PurchaseDate, CustomerId, VehicleId, SalesPersonId) VALUES(?,?,?,?)";
        public static final String UPDATE = "UPDATE purchase_order SET PurchaseDate = ?, CustomerId = ?, VehicleId = ?, SalesPersonId = ? WHERE PONumber = ?";
        public static final String DELETE_BY_ID = "DELETE FROM purchase_order WHERE PONumber = ?";
        public static final String SELECT_ALL = """
                SELECT PO.PONumber, PO.PurchaseDate, PO.CustomerId, PO.VehicleId, PO.SalesPersonId, CU.Name, CU.CompanyName, CU.Address, CU.Phone, CU.Email,
                V.ViNumber, V.BodyType, V.EngDispl, V.EngPowerKW, V.YearOfProd, V.FuelType, V.Price, V.Currency, V.ModelId, V.BusinessUId, M.ModelName, M.BrandId, BR.BrandName, BU.Name, BU.CompanyRegNum,
                BU.TaxId, BU.Address, BU.CityId, BU.Phone, BU.Email, C.ZipCode, C.CityName, U.FirstName, U.LastName FROM PURCHASE_ORDER PO JOIN Customer CU ON PO.CustomerId = CU.Id 
                JOIN Vehicle V ON PO.VehicleId = V.Id JOIN `User` U ON PO.SalesPersonId = U.Id JOIN model M ON V.ModelId = M.Id JOIN brand BR ON M.BrandId = BR.Id JOIN business_unit BU ON V.BusinessUId = BU.Id
                JOIN city C ON BU.CityId = C.Id;""";
        public static final String SELECT_BY_ID = "SELECT PurchaseDate, CustomerId, VehicleId, SalesPersonId FROM purchase_order WHERE PONumber = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM purchase_order WHERE PONumber IN";

    }

    public static class UserProfiles {

        public static final String INSERT = "INSERT INTO user_profile(Email, Password, UserId) VALUES(?,?,?)";
        public static final String UPDATE = "UPDATE user_profile SET Email = ?, Password = ?, UserId = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM user_profile WHERE id = ?";
        public static final String SELECT_ALL = "UP.Email, UP.Password, UP.UserId, U.FirstName, U.LastName, U.DateOfBirth, U.Gender, U.CityId, C.ZipCode, C.CityName FROM user_profile UP JOIN `user` U ON UP.UserId = U.Id JOIN city C ON U.CityId = C.id";
        public static final String SELECT_BY_ID = "SELECT Email, Password, UserId FROM user_profile WHERE Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM user_profile WHERE Id IN";

    }

    public static class Users {

        public static final String INSERT = "INSERT INTO `user`(FirstName, LastName, DateOfBirth, Gender, CityId) VALUES(?,?,?,?,?)";
        public static final String UPDATE = "UPDATE `user` SET FirstName = ?, LastName = ?, DateOfBirth = ?, Gender = ?, CityId = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM `user` WHERE id= ?";
        public static final String SELECT_ALL = "SELECT U.Id, U.FirstName, U.LastName, U.DateOfBirth, U.Gender, U.CityId, C.ZipCode ,C.CityName FROM `user` U JOIN city C ON U.CityId = C.id";
        public static final String SELECT_BY_ID = "SELECT FirstName, LastName, DateOfBirth, Gender, CityId FROM user WHERE Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM `user` WHERE Id IN";


    }

    public static class Vehicles {
        public static final String INSERT = "INSERT INTO vehicle(ViNumber, BodyType, EngDispl, EngPowerKW, YearOFProd, FuelType, Price, Currency, ModelId, BusinessUId) VALUES(?,?,?,?,?,?,?,?,?,?)";
        public static final String UPDATE = "UPDATE vehicle SET ViNumber = ?, BodyType = ?, EngDispl = ?, EngPowerKW = ?, YearOFProd = ?, FuelType = ?, Price = ?, Currency = ?, ModelId = ?, BusinessUId = ? WHERE Id = ?";
        public static final String DELETE_BY_ID = "DELETE FROM vehicle WHERE Id = ?";
        public static final String SELECT_ALL = """
                SELECT V.Id, V.ViNumber, V.BodyType, V.EngDispl, V.EngPowerKW, V.YearOfProd, V.FuelType, V.Price, V.Currency, V.ModelId, V.BusinessUId,
                M.ModelName, M.BrandId, BR.BrandName, BU.Name, BU.CompanyRegNum, BU.TaxId, BU.Address, BU.CityId, BU.Phone, BU.Email, C.ZipCode, C.CityName
                FROM vehicle V JOIN model M ON V.ModelId = M.Id
                JOIN brand BR ON M.BrandId = BR.Id JOIN business_unit BU ON V.BusinessUId = BU.Id JOIN city C ON BU.CityId = C.Id""";
        public static final String SELECT_BY_ID = "SELECT ViNumber, BodyType, EngDispl, EngPowerKW, YearOfProd, FuelType, Price, Currency, ModelId, BusinessUId WHERE Id = ?";
        public static final String DELETE_MULTIPLE_ID = "DELETE FROM vehicle WHERE Id IN";

    }


}
