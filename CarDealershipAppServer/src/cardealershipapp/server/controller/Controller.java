package cardealershipapp.server.controller;

import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.domain.Equipment;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.server.configuration.Configuration;
import cardealershipapp.server.service.BrandService;
import cardealershipapp.server.service.BusinessUnitService;
import cardealershipapp.server.service.CityService;
import cardealershipapp.server.service.CustomerService;
import cardealershipapp.server.service.EquipmentService;
import cardealershipapp.server.service.ModelService;
import cardealershipapp.server.service.PurchaseOrderItemService;
import cardealershipapp.server.service.PurchaseOrderService;
import cardealershipapp.server.service.UserProfileService;
import cardealershipapp.server.service.VehicleService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Controller {

    private static final Controller instance = new Controller();

    private UserProfileService userProfileService;
    private CityService cityService;
    private BrandService brandService;
    private ModelService modelService;
    private BusinessUnitService businessUnitService;
    private VehicleService vehicleService;
    private EquipmentService equipmentService;
    private CustomerService customerService;
    private PurchaseOrderService purchaseOrderService;
    private PurchaseOrderItemService purchaseOrderItemService;

    private Controller() {
        try {
            this.userProfileService = Configuration.getInstance().getUserProfileService();
            this.cityService = Configuration.getInstance().getCityService();
            this.brandService = Configuration.getInstance().getBrandService();
            this.modelService = Configuration.getInstance().getModelService();
            this.businessUnitService = Configuration.getInstance().getBusinessUnitService();
            this.vehicleService = Configuration.getInstance().getVehicleService();
            this.equipmentService = Configuration.getInstance().getEquipmentService();
            this.customerService = Configuration.getInstance().getCustomerService();
            this.purchaseOrderService = Configuration.getInstance().getPurchaseOrderService();
            this.purchaseOrderItemService = Configuration.getInstance().getPurchaseOrderItemService();
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Kontroler je inicijalizovan");
    }

    public static Controller getInstance() {
        return instance;
    }

    public UserProfile login(String email, String password) throws Exception {
        return userProfileService.logIn(email, password);
    }

    //CITY
    public void addCity(City city) throws Exception {
        cityService.add(city);
    }

    public void updateCity(City city) throws Exception {
        cityService.update(city);
    }

    public void deleteCity(City city) throws Exception {
        cityService.delete(city);
    }

    public List<City> getAllCities() throws Exception {
        return cityService.getAll();
    }
    
    public City cityFindById(Long id) throws Exception {
        return cityService.findById(id);
    }

    //BRAND
    public void addBrand(Brand brand) throws Exception {
        brandService.add(brand);
    }

    public void updateBrand(Brand brand) throws Exception {
        brandService.update(brand);
    }

    public void deleteBrand(Brand brand) throws Exception {
        brandService.delete(brand);
    }

    public List<Brand> getAllBrands() throws Exception {
        return brandService.getAll();
    }

    public Brand brandFindById(Long id) throws Exception {
        return brandService.findById(id);
    }

    //MODEL
    public void addModel(Model model) throws Exception {
        modelService.add(model);
    }

    public void updateModel(Model model) throws Exception {
        modelService.update(model);
    }

    public void deleteModel(Model model) throws Exception {
        modelService.delete(model);
    }
    
    public void deleteMultipleModels(List<Model> models) throws Exception{
        modelService.deleteMultiple(models);
    }

    public List<Model> getAllModels() throws Exception {
        return modelService.getAll();
    }

    public Model modelFindById(Long id) throws Exception {
        return modelService.findById(id);
    }

    //BUSSINESS UNIT
    public void addBusinessUnit(BusinessUnit businessUnit) throws Exception {
        businessUnitService.add(businessUnit);
    }

    public void updateBusinessUnit(BusinessUnit businessUnit) throws Exception {
        businessUnitService.update(businessUnit);
    }

    public void deleteBusinessUnit(BusinessUnit businessUnit) throws Exception {
        businessUnitService.delete(businessUnit);
    }

    public List<BusinessUnit> getAllBusinessUnits() throws Exception {
        return businessUnitService.getAll();
    }

    public BusinessUnit businessUnitFindById(Long id) throws Exception {
        return businessUnitService.findById(id);
    }
    
    //VEHICLE
    public void addVehicle(Vehicle vehicle) throws Exception{
        vehicleService.add(vehicle);
    }
    
    public void updateVehicle(Vehicle vehicle) throws Exception {
        vehicleService.update(vehicle);
    }
    
    public void deleteVehicle(Vehicle vehicle) throws Exception {
        vehicleService.delete(vehicle);
    }
    
    public void deleteByVin(Vehicle vehicle) throws Exception{
        vehicleService.deleteByVin(vehicle);
    }
    
    public void deleteMultipleByVin(List<Vehicle> vehicles) throws Exception{
        vehicleService.deleteMultipleByVin(vehicles);
    }
    
    public List<Vehicle> getAllVehicles() throws Exception{
        return vehicleService.getAll();
    }
    
    //EQUIPMENT
    public void addEquipment(Equipment equipment) throws Exception {
        equipmentService.add(equipment);
    }
    
    public void updateEquipment(Equipment equipment) throws Exception {
        equipmentService.update(equipment);
    }
    
    public void deleteEquipment(Equipment equipment) throws Exception {
        equipmentService.delete(equipment);
    }
    
    public List<Equipment> getAllEquipments() throws Exception {
        return equipmentService.getAll();
    }
    
    //CUSTOMER
    public void addCustomer(Customer customer) throws Exception{
        customerService.add(customer);
    }
    
    public void updateCustomer(Customer customer) throws Exception{
        customerService.update(customer);
    }
    
    public void deleteCustomer(Customer customer) throws Exception {
        customerService.delete(customer);
    }
    
    public void deleteMultipleCustomers(List<Customer> customers) throws Exception{
        customerService.deleteMultiple(customers);
    }
    
    public List<Customer> getAllCustomers() throws Exception {
        return customerService.getAll();
    }
    
   //PURCHASE ORDER
   public void addPurchaseOrder(PurchaseOrder purchaseOrder) throws Exception {
       purchaseOrderService.add(purchaseOrder);
   }
   
   public void updatePurchaseOrder(PurchaseOrder purchaseOrder) throws Exception {
       purchaseOrderService.update(purchaseOrder);
   }
   
   public void deletePurchaseOrder(PurchaseOrder purchaseOrder) throws Exception {
       purchaseOrderService.delete(purchaseOrder);
   }
   
   public void deleteMultiplePurchaseOrders(List<PurchaseOrder> purchaseOrders) throws Exception {
       purchaseOrderService.deleteMultiple(purchaseOrders);
   }
   
   public List<PurchaseOrder> getAllPurchaseOrders() throws Exception {
       return purchaseOrderService.getAll();
   }
   
   public PurchaseOrder purchaseOrderFindById(Long id) throws Exception {
       return purchaseOrderService.findById(id);
   }
   
   //PURCHASE ORDER ITEM
   public void addPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception {
       purchaseOrderItemService.addItems(purchaseOrderItems);
   }
   
   public void updatePurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception {
       purchaseOrderItemService.updateItems(purchaseOrderItems);
   }
   
   public void deletePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) throws Exception {
       purchaseOrderItemService.delete(purchaseOrderItem);
   }
   
   public List<PurchaseOrderItem> getAllPurchaseOrderItems() throws Exception {
       return purchaseOrderItemService.getAll();
   }
   
   
   
   
    

}
