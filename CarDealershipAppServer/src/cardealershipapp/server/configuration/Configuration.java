package cardealershipapp.server.configuration;

import cardealershipapp.common.domain.*;
import cardealershipapp.server.repository.PurchaseOrderItemRepository;
import cardealershipapp.server.repository.ExtendedRepository;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.impl.CustomerRepositoryImpl;
import cardealershipapp.server.service.*;

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
            PROPERTIES.load(new FileInputStream("CarDealershipAppServer/settings.properties"));
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

    private PurchaseOrderService selectPurchaseOrderService (String service, ExtendedRepository purchaseOrderRepository, PurchaseOrderItemRepository purchaseOrderItemRepository, ExtendedRepository customerRepository) throws Exception {
        String className = PROPERTIES.getProperty(service);
        Class<?> c = Class.forName(className);
        Constructor<?> constructor = c.getConstructor(ExtendedRepository.class, PurchaseOrderItemRepository.class, ExtendedRepository.class);
        return (PurchaseOrderService) constructor.newInstance(purchaseOrderRepository, purchaseOrderItemRepository, customerRepository);
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
    
    public ExtendedRepository getCustomerRepository() throws Exception {
        return (ExtendedRepository) selectRepositoryProperty("customer_repository");
    }
   
    public ExtendedRepository getPurchaseOrderRepository() throws Exception {
        return (ExtendedRepository) selectRepositoryProperty("purchase_order_repository");
    }
    
    public PurchaseOrderItemRepository getPurchaseOrderItemRepository() throws Exception {
        return (PurchaseOrderItemRepository) selectRepositoryProperty("purchase_order_item_repository");
    }
    
    
    

    public ServiceCRUD<User, Long> getUserService() throws Exception {
        return (ServiceCRUD<User, Long>) selectServiceProperty("user_service", getUserRepository());
    }

    public ServiceCRUD<Brand, Long> getBrandService() throws Exception {
        return (ServiceCRUD<Brand, Long>) selectServiceProperty("brand_service", getBrandRepository());
    }

    public ServiceCRUD<BusinessUnit, Long> getBusinessUnitService() throws Exception {
        return (ServiceCRUD<BusinessUnit, Long>) selectServiceProperty("business_unit_service", getBusinessUnitRepository());
    }

    public ServiceCRUD<City, Long> getCityService() throws Exception {
        return (ServiceCRUD<City, Long>) selectServiceProperty("city_service", getCityRepository());
    }

    public ServiceCRUD<Model, Long> getModelService() throws Exception {
        return (ServiceCRUD<Model, Long>) selectServiceProperty("model_service", getModelRepository());
    }

    public VehicleService getVehicleService() throws Exception {
        return (VehicleService) selectServiceProperty("vehicle_service", getVehicleRepository());
    }
    
    public ServiceCRUD<Equipment, Long> getEquipmentService() throws Exception {
        return (ServiceCRUD<Equipment, Long>) selectServiceProperty("equipment_service", getEquipmentRepository());
    }
    
    public ServiceCRUD<Customer, Long> getCustomerService() throws Exception {
        return (ServiceCRUD<Customer, Long>) selectServiceProperty("customer_service", getCustomerRepository());
    }
    
    public PurchaseOrderService getPurchaseOrderService() throws Exception {
        return selectPurchaseOrderService("purchase_order_service", getPurchaseOrderRepository(), getPurchaseOrderItemRepository(), getCustomerRepository());
    }
    
    public PurchaseOrderItemService getPurchaseOrderItemService() throws Exception {
        return (PurchaseOrderItemService) selectItemServiceProperty("purchase_order_item_service", getPurchaseOrderItemRepository());
    }
    
    public UserProfileService getUserProfileService() throws Exception {
        return  selectUserProfileServiceProperty("user_profile_service", getUserProfileRepository(), getUserRepository());
    }

}
