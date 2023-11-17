package cardealershipapp.client.ui.purchaseorder.ci;

import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.model.PurchaseOrderItemTableModel;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.Vehicle;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderDetailsController {

    private static BigDecimal totalPrice = BigDecimal.ZERO;
    private static Currency currency;
    
    public static void vendorField(JTextField txtSalesPersonName, JTextField txtCompany, JTextField txtAddress, JTextField txtEmail, JTextField txtPhone){
         getPurchaseOrder();
        User user = getPurchaseOrder().getSalesPerson();
        BusinessUnit businessUnit = getPurchaseOrder().getVehicle().getBusinessUnit();
        
        txtSalesPersonName.setText(user.getFirstName() + " " + user.getLastName());
        txtCompany.setText(businessUnit.getName());
        txtAddress.setText(businessUnit.getAddress());
        txtEmail.setText(businessUnit.getEmail());
        txtPhone.setText(businessUnit.getPhone());
    }
    
    public static void customerField(JTextField txtCustomerName, JTextField txtCustomerCompany, JTextField txtCustomerAddress, JTextField txtCustomerEmail, JTextField txtCustomerPhone){
        
        Customer customer = getPurchaseOrder().getCustomer();
        
        txtCustomerName.setText(customer.getName());
        txtCustomerCompany.setText(customer.getCompanyName());
        txtCustomerAddress.setText(customer.getAddress());
        txtCustomerEmail.setText(customer.getEmail());
        txtCustomerPhone.setText(customer.getPhone());
    }
    
    public static void vehicleField(JTextField txtVehicleBrand, JTextField txtVehicleModel, JTextField txtVehicleBodyType, JTextField txtVehicleVinNumber, JTextField txtVehicleEngineDispl, JTextField txtVehicleEnginePow,
    JTextField txtVehicleFuelType, JTextField txtVehicleYearOfProduction, JTextField txtVehiclePrice){
        
        Vehicle vehicle = getPurchaseOrder().getVehicle();
        
        txtVehicleBrand.setText(vehicle.getModel().getBrandName());
        txtVehicleModel.setText(vehicle.getModel().getName());
        txtVehicleBodyType.setText(vehicle.getBodyType().toString());
        txtVehicleVinNumber.setText(vehicle.getViNumber());
        txtVehicleEngineDispl.setText(String.valueOf(vehicle.getEngineDisplacement()));
        txtVehicleEnginePow.setText(String.valueOf(vehicle.getEnginePower()));
        txtVehicleFuelType.setText(vehicle.getFuelType().toString());
        txtVehicleYearOfProduction.setText(String.valueOf(vehicle.getYearOfProd()));
        txtVehiclePrice.setText(vehicle.getPrice().toString() + " " + vehicle.getCurrency().toString());
        
    }
    
    public static void totalAmount(JTextField txtTotalPrice){
        totalPrice = BigDecimal.ZERO;
        BigDecimal itemPrice = BigDecimal.ZERO;
        
        Vehicle vehicle = getPurchaseOrder().getVehicle();
        currency = vehicle.getCurrency();
        List<PurchaseOrderItem> purchaseOrderItems = getPurchaseOrder().getPurchaseOrderItems();
        
        for (PurchaseOrderItem item : purchaseOrderItems) {
            itemPrice = itemPrice.add(item.getEquipment().getPrice()).multiply(BigDecimal.valueOf(item.getQuantity().longValue()));
            totalPrice = totalPrice.add(itemPrice);
            itemPrice = BigDecimal.ZERO;
        }
        
        totalPrice = totalPrice.add(vehicle.getPrice());
        txtTotalPrice.setText(totalPrice.toString() + " " + currency.toString());
    }
    
    public static void purchaseOrderNumAndDate(JLabel lblPurchaseDate, JLabel lblPurchaseOrderID){
        lblPurchaseDate.setText(getPurchaseOrder().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        lblPurchaseOrderID.setText(getPurchaseOrder().getPurchaseOrderNum().toString());
        
    }
    
    public static void fillTable(JTable tblItems){
         tblItems.setModel(new PurchaseOrderItemTableModel(getPurchaseOrder().getPurchaseOrderItems()));
    }
    
    public static PurchaseOrder getPurchaseOrder(){
        return ApplicationSession.getInstance().getPurchaseOrder();
    }
    
}
