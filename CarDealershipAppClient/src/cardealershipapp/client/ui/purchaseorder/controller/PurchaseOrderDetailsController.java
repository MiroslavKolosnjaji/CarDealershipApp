package cardealershipapp.client.ui.purchaseorder.controller;

import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.model.PurchaseOrderItemTableModel;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderDetailsForm;
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


/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderDetailsController {

    private final PurchaseOrderDetailsForm purchaseOrderDetailsForm;

    public PurchaseOrderDetailsController(PurchaseOrderDetailsForm purchaseOrderDetailsForm) {
        this.purchaseOrderDetailsForm = purchaseOrderDetailsForm;
    }

    public void vendorField() {
        getPurchaseOrder();
        User user = getPurchaseOrder().getSalesPerson();
        BusinessUnit businessUnit = getPurchaseOrder().getVehicle().getBusinessUnit();

        purchaseOrderDetailsForm.getTxtSalesPersonName().setText(user.getFirstName() + " " + user.getLastName());
        purchaseOrderDetailsForm.getTxtCompany().setText(businessUnit.getName());
        purchaseOrderDetailsForm.getTxtAddress().setText(businessUnit.getAddress());
        purchaseOrderDetailsForm.getTxtEmail().setText(businessUnit.getEmail());
        purchaseOrderDetailsForm.getTxtPhone().setText(businessUnit.getPhone());
    }

    public void customerField() {

        Customer customer = getPurchaseOrder().getCustomer();

        purchaseOrderDetailsForm.getTxtCustomerName().setText(customer.getName());
        purchaseOrderDetailsForm.getTxtCustomerCompany().setText(customer.getCompanyName());
        purchaseOrderDetailsForm.getTxtCustomerAddress().setText(customer.getAddress());
        purchaseOrderDetailsForm.getTxtCustomerEmail().setText(customer.getEmail());
        purchaseOrderDetailsForm.getTxtCustomerPhone().setText(customer.getPhone());
    }

    public void vehicleField() {

        Vehicle vehicle = getPurchaseOrder().getVehicle();

        purchaseOrderDetailsForm.getTxtVehicleBrand().setText(vehicle.getModel().getBrandName());
        purchaseOrderDetailsForm.getTxtVehicleModel().setText(vehicle.getModel().getName());
        purchaseOrderDetailsForm.getTxtVehicleBodyType().setText(vehicle.getBodyType().toString());
        purchaseOrderDetailsForm.getTxtVehicleVinNumber().setText(vehicle.getViNumber());
        purchaseOrderDetailsForm.getTxtVehicleEngineDispl().setText(String.valueOf(vehicle.getEngineDisplacement()));
        purchaseOrderDetailsForm.getTxtVehicleEnginePow().setText(String.valueOf(vehicle.getEnginePower()));
        purchaseOrderDetailsForm.getTxtVehicleFuelType().setText(vehicle.getFuelType().toString());
        purchaseOrderDetailsForm.getTxtVehicleYearOfProduction().setText(String.valueOf(vehicle.getYearOfProd()));
        purchaseOrderDetailsForm.getTxtVehiclePrice().setText(vehicle.getPrice().toString() + " " + vehicle.getCurrency().toString());

    }

    public void totalAmount() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal itemPrice = BigDecimal.ZERO;

        Vehicle vehicle = getPurchaseOrder().getVehicle();
        Currency currency = vehicle.getCurrency();
        List<PurchaseOrderItem> purchaseOrderItems = getPurchaseOrder().getPurchaseOrderItems();

        for (PurchaseOrderItem item : purchaseOrderItems) {
            itemPrice = itemPrice.add(item.getEquipment().getPrice()).multiply(BigDecimal.valueOf(item.getQuantity().longValue()));
            totalPrice = totalPrice.add(itemPrice);
            itemPrice = BigDecimal.ZERO;
        }

        totalPrice = totalPrice.add(vehicle.getPrice());
        purchaseOrderDetailsForm.getTxtTotalPrice().setText(totalPrice + " " + currency.toString());
    }

    public void purchaseOrderNumAndDate() {
        purchaseOrderDetailsForm.getLblPurchaseDate().setText(getPurchaseOrder().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        purchaseOrderDetailsForm.getLblPurchaseOrderID().setText(getPurchaseOrder().getPurchaseOrderNum().toString());
    }

    public void fillTable() {
        purchaseOrderDetailsForm.getTblItems().setModel(new PurchaseOrderItemTableModel(getPurchaseOrder().getPurchaseOrderItems()));
    }

    public PurchaseOrder getPurchaseOrder() {
        return ApplicationSession.getInstance().getPurchaseOrder();
    }

}
