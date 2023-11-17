package cardealershipapp.server.configuration;

import cardealershipapp.server.repository.PurchaseOrderItemRepository;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.BrandService;
import cardealershipapp.server.service.BusinessUnitService;
import cardealershipapp.server.service.CityService;
import cardealershipapp.server.service.CustomerService;
import cardealershipapp.server.service.EquipmentService;
import cardealershipapp.server.service.ModelService;
import cardealershipapp.server.service.PurchaseOrderItemService;
import cardealershipapp.server.service.PurchaseOrderService;
import cardealershipapp.server.service.UserProfileService;
import cardealershipapp.server.service.UserService;
import cardealershipapp.server.service.VehicleService;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Miroslav Kološnjaji
 */
public class Configuration {

    private final static Configuration INSTANCE = new Configuration();
    private final Properties PROPERTIES = new Properties();

    private Configuration() {
        try {
            PROPERTIES.load(new FileInputStream("settings.properties"));
        } catch (IOException ioe) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    public static Configuration getInstance() {
        return INSTANCE;
    }
    

    private Object selectRepositoryProperty(String repository) throws Exception {
        String className = PROPERTIES.getProperty(repository);
        Class<?> c = Class.forName(className);
        Constructor<?> constructor = c.getConstructor();
        return constructor.newInstance();
    }

    private Object selectServiceProperty(String service, Object repository) throws Exception {
        String className = PROPERTIES.getProperty(service);
        Class<?> c = Class.forName(className);
        Constructor<?> constructor = c.getConstructor(Repository.class);
        return constructor.newInstance(repository);
    }
    
    private Object selectItemServiceProperty(String service, Object repository) throws Exception {
        String className = PROPERTIES.getProperty(service);
        Class<?> c = Class.forName(className);
        Constructor<?> constructor = c.getConstructor(PurchaseOrderItemRepository.class);
        return constructor.newInstance(repository);
    }

    private Object selectPurchaseOrderService(String service, Repository repository, PurchaseOrderItemRepository purchaseOrderItemRepository) throws Exception {
        String className = PROPERTIES.getProperty(service);
        Class<?> c = Class.forName(className);
        Constructor<?> constructor = c.getConstructor(Repository.class, PurchaseOrderItemRepository.class);
        return constructor.newInstance(repository, purchaseOrderItemRepository);
    }

    private UserProfileService selectUserProfileServiceProperty(String service, Repository userProfileRepository, Repository userRepository) throws Exception {
        String className = PROPERTIES.getProperty(service);
        Class<?> c = Class.forName(className);
        Constructor<?> constructor = c.getConstructor(Repository.class, Repository.class);
        return (UserProfileService) constructor.newInstance(userProfileRepository, userRepository);
    }

    
    
    public Repository getUserRepository() throws Exception {
        return (Repository) selectRepositoryProperty("user_repository");
    }

    public Repository getUserProfileRepository() throws Exception {
        return (Repository) selectRepositoryProperty("user_profile_repository");
    }

    public Repository getBrandRepository() throws Exception {
        return (Repository) selectRepositoryProperty("brand_repository");
    }

    public Repository getBusinessUnitRepository() throws Exception {
        return (Repository) selectRepositoryProperty("business_unit_repository");
    }

    public Repository getCityRepository() throws Exception {
        return (Repository) selectRepositoryProperty("city_repository");
    }

    public Repository getModelRepository() throws Exception {
        return (Repository) selectRepositoryProperty("model_repository");
    }

    public Repository getVehicleRepository() throws Exception {
        return (Repository) selectRepositoryProperty("vehicle_repository");
    }
    
    public Repository getEquipmentRepository() throws Exception {
        return (Repository) selectRepositoryProperty("equipment_repository");
    }
    
    public Repository getCustomerRepository() throws Exception {
        return (Repository) selectRepositoryProperty("customer_repository");
    }
   
    public Repository getPurchaseOrderRepository() throws Exception {
        return (Repository) selectRepositoryProperty("purchase_order_repository");
    }
    
    public PurchaseOrderItemRepository getPurchaseOrderItemRepository() throws Exception {
        return (PurchaseOrderItemRepository) selectRepositoryProperty("purchase_order_item_repository");
    }
    
    
    

    public UserService getUserService() throws Exception {
        return (UserService) selectServiceProperty("user_service", getUserRepository());
    }

    public BrandService getBrandService() throws Exception {
        return (BrandService) selectServiceProperty("brand_service", getBrandRepository());
    }

    public BusinessUnitService getBusinessUnitService() throws Exception {
        return (BusinessUnitService) selectServiceProperty("business_unit_service", getBusinessUnitRepository());
    }

    public CityService getCityService() throws Exception {
        return (CityService) selectServiceProperty("city_service", getCityRepository());
    }

    public ModelService getModelService() throws Exception {
        return (ModelService) selectServiceProperty("model_service", getModelRepository());
    }

    public VehicleService getVehicleService() throws Exception {
        return (VehicleService) selectServiceProperty("vehicle_service", getVehicleRepository());
    }
    
    public EquipmentService getEquipmentService() throws Exception {
        return (EquipmentService) selectServiceProperty("equipment_service", getEquipmentRepository());
    }
    
    public CustomerService getCustomerService() throws Exception {
        return (CustomerService) selectServiceProperty("customer_service", getCustomerRepository());
    }
    
    public PurchaseOrderService getPurchaseOrderService() throws Exception {
        return (PurchaseOrderService) selectServiceProperty("purchase_order_service", getPurchaseOrderRepository());
    }
    
    public PurchaseOrderItemService getPurchaseOrderItemService() throws Exception {
        return (PurchaseOrderItemService) selectItemServiceProperty("purchase_order_item_service", getPurchaseOrderItemRepository());
    }
    
    public UserProfileService getUserProfileService() throws Exception {
        return (UserProfileService) selectUserProfileServiceProperty("user_profile_service", getUserProfileRepository(), getUserRepository());
    }

}
