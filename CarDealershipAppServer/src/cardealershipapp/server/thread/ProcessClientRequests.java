package cardealershipapp.server.thread;

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
import static cardealershipapp.common.transfer.Operation.LOGIN;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
import cardealershipapp.server.controller.Controller;
import cardealershipapp.server.session.ServerSession;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ProcessClientRequests extends Thread {

    private Socket socket;
    private ObjectOutputStream sender;
    private ObjectInputStream receiver;
    private UserProfile connectedUser;
    private LocalDateTime loginDateTime;
    private boolean signal;

    public ProcessClientRequests(Socket socket) {
        try {
            this.socket = socket;
            this.sender = new ObjectOutputStream(socket.getOutputStream());
            this.receiver = new ObjectInputStream(socket.getInputStream());
            this.signal = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        ServerSession.getInstance().setConnection(this);
        
        while (signal && !isInterrupted()) {

            try {
                Request request = (Request) receiver.readObject();
                Response response = new Response();
                try {

                    City city;
                    Brand brand;
                    Model model;
                    BusinessUnit businessUnit;
                    Vehicle vehicle;
                    Equipment equipment;
                    Customer customer;
                    PurchaseOrder purchaseOrder;
                    PurchaseOrderItem purchaseOrderItem;

                    switch (request.getOperation()) {

                        case LOGIN:
                            UserProfile up = (UserProfile) request.getArgument();
                            UserProfile userProfile = (UserProfile) Controller.getInstance().login(up.getEmail(), up.getPassword());
                            response.setResult(userProfile);
                            setConnectedUser(userProfile);
                            setLoginDate(LocalDateTime.now());
                            ServerSession.getInstance().addConnectedClient(this);

                            break;

                        case CITY_GET_ALL:
                            List<City> cities = Controller.getInstance().getAllCities();
                            response.setResult(cities);
                            break;

                        case CITY_FIND_BY_ID:
                            city = (City) request.getArgument();
                            response.setResult(Controller.getInstance().cityFindById(city.getId()));
                            break;

                        case BRAND_GET_ALL:
                            List<Brand> brands = Controller.getInstance().getAllBrands();
                            response.setResult(brands);
                            break;

                        case BRAND_FIND_BY_ID:
                            brand = (Brand) request.getArgument();
                            response.setResult(Controller.getInstance().brandFindById(brand.getId()));
                            break;

                        case MODEL_ADD:
                            model = (Model) request.getArgument();
                            Controller.getInstance().addModel(model);
                            break;

                        case MODEL_UPDATE:
                            model = (Model) request.getArgument();
                            Controller.getInstance().updateModel(model);
                            break;

                        case MODEL_DELETE:
                            model = (Model) request.getArgument();
                            Controller.getInstance().deleteModel(model);
                            break;

                        case MODEL_DELETE_MULTI:
                            List<Model> deleteModels = (List<Model>) request.getArgument();
                            Controller.getInstance().deleteMultipleModels(deleteModels);
                            break;

                        case MODEL_GET_ALL:
                            List<Model> models = Controller.getInstance().getAllModels();
                            response.setResult(models);
                            break;

                        case MODEL_FIND_BY_ID:
                            model = (Model) request.getArgument();
                            response.setResult(Controller.getInstance().modelFindById(model.getId()));
                            break;

                        case BUSINESSUNIT_GET_ALL:
                            List<BusinessUnit> businessUnits = Controller.getInstance().getAllBusinessUnits();
                            response.setResult(businessUnits);
                            break;

                        case BUSINESSUNIT_FIND_BY_ID:
                            businessUnit = (BusinessUnit) request.getArgument();
                            response.setResult(Controller.getInstance().businessUnitFindById(businessUnit.getId()));
                            break;

                        case VEHICLE_ADD:
                            vehicle = (Vehicle) request.getArgument();
                            Controller.getInstance().addVehicle(vehicle);
                            break;

                        case VEHICLE_UPDATE:
                            vehicle = (Vehicle) request.getArgument();
                            Controller.getInstance().updateVehicle(vehicle);
                            break;

                        case VEHICLE_DELETE:
                            vehicle = (Vehicle) request.getArgument();
                            Controller.getInstance().deleteVehicle(vehicle);
                            break;

                        case VEHICLE_DELETE_BY_VIN:
                            vehicle = (Vehicle) request.getArgument();
                            Controller.getInstance().deleteByVin(vehicle);
                            break;

                        case VEHICLE_DELETE_MULTIPLE_BY_VIN:
                            List<Vehicle> deleteVehicles = (List<Vehicle>) request.getArgument();
                            Controller.getInstance().deleteMultipleByVin(deleteVehicles);
                            break;

                        case VEHICLE_GET_ALL:
                            List<Vehicle> vehicles = Controller.getInstance().getAllVehicles();
                            response.setResult(vehicles);
                            break;

                        case EQUIPMENT_ADD:
                            equipment = (Equipment) request.getArgument();
                            Controller.getInstance().addEquipment(equipment);
                            break;

                        case EQUIPMENT_UPDATE:
                            equipment = (Equipment) request.getArgument();
                            Controller.getInstance().updateEquipment(equipment);
                            break;

                        case EQUIPMENT_DELETE:
                            equipment = (Equipment) request.getArgument();
                            Controller.getInstance().deleteEquipment(equipment);
                            break;

                        case EQUIPMENT_GET_ALL:
                            List<Equipment> equipments = Controller.getInstance().getAllEquipments();
                            response.setResult(equipments);
                            break;

                        case CUSTOMER_ADD:
                            customer = (Customer) request.getArgument();
                            Controller.getInstance().addCustomer(customer);
                            break;

                        case CUSTOMER_UPDATE:
                            customer = (Customer) request.getArgument();
                            Controller.getInstance().updateCustomer(customer);
                            break;

                        case CUSTOMER_DELETE:
                            customer = (Customer) request.getArgument();
                            Controller.getInstance().deleteCustomer(customer);
                            break;

                        case CUSTOMER_DELETE_MULTI:
                            List<Customer> c = (List<Customer>) request.getArgument();
                            Controller.getInstance().deleteMultipleCustomers(c);
                            break;

                        case CUSTOMER_GET_ALL:
                            List<Customer> customers = Controller.getInstance().getAllCustomers();
                            response.setResult(customers);
                            break;

                        case PURCHASE_ORDER_ADD:
                            purchaseOrder = (PurchaseOrder) request.getArgument();
                            Controller.getInstance().addPurchaseOrder(purchaseOrder);
                            break;

                        case PURCHASE_ORDER_UPDATE:
                            purchaseOrder = (PurchaseOrder) request.getArgument();
                            Controller.getInstance().updatePurchaseOrder(purchaseOrder);
                            break;

                        case PURCHASE_ORDER_DELETE:
                            purchaseOrder = (PurchaseOrder) request.getArgument();
                            Controller.getInstance().deletePurchaseOrder(purchaseOrder);
                            break;

                        case PURCHASE_ORDER_DELETE_MULTI:
                            List<PurchaseOrder> p = (List<PurchaseOrder>) request.getArgument();
                            Controller.getInstance().deleteMultiplePurchaseOrders(p);
                            break;

                        case PURCHASE_ORDER_GET_ALL:
                            List<PurchaseOrder> purchaseOrders = Controller.getInstance().getAllPurchaseOrders();
                            response.setResult(purchaseOrders);
                            break;

                        case PURCHASE_ORDER_FIND_BY_ID:
                            purchaseOrder = (PurchaseOrder) request.getArgument();
                            response.setResult(Controller.getInstance().purchaseOrderFindById(purchaseOrder.getPurchaseOrderNum()));
                            break;

                        case PURCHASE_ORDER_ITEM_ADD:
                            List<PurchaseOrderItem> purchaseOrderItems = (List<PurchaseOrderItem>) request.getArgument();
                            Controller.getInstance().addPurchaseOrderItems(purchaseOrderItems);
                            break;

                        case PURCHASE_ORDER_ITEM_UPDATE:
                            List<PurchaseOrderItem> OrderItems = (List<PurchaseOrderItem>) request.getArgument();
                            Controller.getInstance().updatePurchaseOrderItems(OrderItems);
                            break;

                        case PURCHASE_ORDER_ITEM_DELETE:
                            purchaseOrderItem = (PurchaseOrderItem) request.getArgument();
                            Controller.getInstance().deletePurchaseOrderItem(purchaseOrderItem);
                            break;

                        case PURCHASE_ORDER_ITEM_GET_ALL:
                            List<PurchaseOrderItem> poi = Controller.getInstance().getAllPurchaseOrderItems();
                            response.setResult(poi);
                            break;

                        case LOGOUT:
                            ServerSession.getInstance().getConnectedClients().remove(this);
                            break;

                        case EXIT:
                            interrupt();
                            ServerSession.getInstance().getConnectedClients().remove(this);
                            break;

                        default:
                            System.out.println("Not supported yet!");

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    response.setException(ex);
                }

                sender.writeObject(response);

            } catch (SocketException soe) {
                System.out.println(soe.getMessage());
            } catch (Exception ex) {
                System.out.println("Process client request exception");
                ex.printStackTrace();
            }
        }
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDateTime = loginDate;
    }

    public LocalDateTime getLoginDate() {
        return loginDateTime;
    }

    public void closeClient() throws IOException {
        socket.close();
    }

    public UserProfile getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(UserProfile connectedUser) {
        this.connectedUser = connectedUser;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.socket);
        hash = 89 * hash + Objects.hashCode(this.connectedUser.getUser().getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProcessClientRequests other = (ProcessClientRequests) obj;
        if (!Objects.equals(this.socket, other.socket)) {
            return false;
        }
        return Objects.equals(this.connectedUser.getUser().getId(), other.connectedUser.getUser().getId());
    }

}
