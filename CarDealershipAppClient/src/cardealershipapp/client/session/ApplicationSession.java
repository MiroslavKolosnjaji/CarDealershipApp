package cardealershipapp.client.session;

import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.common.domain.Vehicle;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ApplicationSession {

    private static final ApplicationSession INSTANCE = new ApplicationSession();
    private Brand brand;
    private BusinessUnit businessUnit;
    private Model model;
    private Long modelId;
    private City city;
    private User user;
    private UserProfile loggedUser;
    private Vehicle vehicle;
    private Vehicle orderSelectedVehicle;
    private Customer customer;
    private PurchaseOrder purchaseOrder;
    private boolean purchaseOrderFormIsOpen;

    private ApplicationSession() {
    }

    public static ApplicationSession getInstance() {
        return INSTANCE;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfile getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserProfile loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getOrderSelectedVehicle() {
        return orderSelectedVehicle;
    }

    public void setOrderSelectedVehicle(Vehicle orderSelectedVehicle) {
        this.orderSelectedVehicle = orderSelectedVehicle;
    }

    public boolean isPurchaseOrderFormIsOpen() {
        return purchaseOrderFormIsOpen;
    }

    public void setPurchaseOrderFormIsOpen(boolean purchaseOrderFormIsOpen) {
        this.purchaseOrderFormIsOpen = purchaseOrderFormIsOpen;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
    
    

}
