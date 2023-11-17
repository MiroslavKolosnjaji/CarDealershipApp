package cardealershipapp.client.ui.purchaseorder.ci;

import cardealershipapp.client.communication.Communication;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.domain.Equipment;
import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.model.PurchaseOrderItemTableModel;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderDetailsForm;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderEditForm;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.common.domain.Currency;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderEditController {

    private static Vehicle vehicleSession;
    private static BigDecimal totalPrice;
    private static Currency currency;
    private static Long counter = 1L;
    private static int defaultItemListSize = 0;

    public static void update(JTextField txtCustomerName, JTextField txtCustomerCompany, JTextField txtCustomerAddress, JTextField txtCustomerPhone, JTextField txtCustomerEmail,
    List<PurchaseOrderItem> purchaseOrderItems, JDialog dialog) {
        try {

            int answer = confirmDialog("Podaci porudzbenice ce biti promenjeni! Da li se slazete?", dialog);

            if (answer == JOptionPane.NO_OPTION) {
                return;
            }

            String customerName = txtCustomerName.getText().trim();
            String customerCompany = txtCustomerCompany.getText().trim();
            String customerAddress = txtCustomerAddress.getText().trim();
            String customerPhone = txtCustomerPhone.getText().trim();
            String customerEmail = txtCustomerEmail.getText().trim();
            Long customerId = ApplicationSession.getInstance().getPurchaseOrder().getCustomer().getId();

            validateInput(txtCustomerName, txtCustomerAddress, txtCustomerPhone);

            Vehicle vehicle = ApplicationSession.getInstance().getPurchaseOrder().getVehicle();
            User salesPerson = ApplicationSession.getInstance().getLoggedUser().getUser();

            Customer customer = new Customer(customerId, customerName, customerCompany, customerAddress, customerPhone, customerEmail);
            getResponse(Operation.CUSTOMER_UPDATE, customer);

            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setPurchaseOrderNum(ApplicationSession.getInstance().getPurchaseOrder().getPurchaseOrderNum());
            purchaseOrder.setDate(LocalDate.now());
            purchaseOrder.setCustomer(customer);
            purchaseOrder.setVehicle(vehicle);
            purchaseOrder.setSalesPerson(salesPerson);

            for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
                purchaseOrderItem.setPurchaseOrder(purchaseOrder);
            }
            purchaseOrder.setPurchaseOrderItems(purchaseOrderItems);

            if (purchaseOrder.getPurchaseOrderItems().size() == defaultItemListSize) {
                getResponse(Operation.PURCHASE_ORDER_ITEM_UPDATE, purchaseOrder.getPurchaseOrderItems());
            } else {
                PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(new PurchaseOrder(purchaseOrder.getPurchaseOrderNum()));
                getResponse(Operation.PURCHASE_ORDER_ITEM_DELETE, purchaseOrderItem);
                getResponse(Operation.PURCHASE_ORDER_ITEM_ADD, purchaseOrder.getPurchaseOrderItems());
            }
            
            getResponse(Operation.PURCHASE_ORDER_UPDATE, purchaseOrder);
            JOptionPane.showMessageDialog(dialog, "Porudzbenica je uspesno azurirana");
            
            dialog.dispose();
            ApplicationSession.getInstance().setPurchaseOrder(purchaseOrder);
            showDetails();
        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(dialog, ive.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Upozorenje!", JOptionPane.WARNING_MESSAGE);
        }

    }
    
    private static void showDetails(){
        JDialog details = new PurchaseOrderDetailsForm(null, true);
        details.setLocationRelativeTo(null);
        details.setVisible(true);
    }

    private static void validateInput(JTextField txtCustomerName, JTextField txtCustomerAddress, JTextField txtCustomerPhone) throws InputValidationException {

        String phoneNumber = txtCustomerPhone.getText().trim();
        
        if (txtCustomerName.getText().trim().isEmpty()) {
            throw new InputValidationException("Polje Ime i prezime nije popunjeno!");
        } else if (txtCustomerAddress.getText().trim().isEmpty()) {
            throw new InputValidationException("Polje Adresa nije popunjeno!");
        }
        
        if (phoneNumber.isEmpty()) {
            throw new InputValidationException("Polje Telefon nije popunjeno!");
        } if(phoneNumber.length() != 11){
            throw new InputValidationException("Duzina broja telefona zajedno sa (/) i (-) treba da bude 11");
        }else if(!phoneNumber.substring(3, 4).equals("/") || !phoneNumber.substring(7,8).equals("-")){
            throw new InputValidationException("Broj telefona mora biti bbb/bbb-bbb formata");
        }

    }

    public static void activeForm(JTextField txtSalesPersonName, JTextField txtCompany, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail) {
        if (ApplicationSession.getInstance().getOrderSelectedVehicle() != null) {
            UserProfile user = ApplicationSession.getInstance().getLoggedUser();
            txtSalesPersonName.setText(user.getUser().getFirstName() + " " + user.getUser().getLastName());

            Vehicle vehicle = ApplicationSession.getInstance().getOrderSelectedVehicle();
            vehicleSession = vehicle;

            txtCompany.setText(vehicle.getBusinessUnit().getName());
            txtAddress.setText(vehicle.getBusinessUnit().getAddress());
            txtPhone.setText(vehicle.getBusinessUnit().getPhone());
            txtEmail.setText(vehicle.getBusinessUnit().getEmail());

        }
    }

    public static void activeFormVehicleInfoFields(JTextField txtVehicleBrand, JTextField txtVehicleModel, JTextField txtVehicleVinNumber, JTextField txtVehicleBodyType, JTextField txtVehicleEngineDispl,
    JTextField txtVehicleEnginePow, JTextField txtVehicleFuelType, JTextField txtVehicleYearOfProduction, JTextField txtVehiclePrice, JComboBox comboItem,List<PurchaseOrderItem> purchaseOrderItems,
    JLabel lblEngineDisplacement, JTextField txtTotalPrice) {
        if (ApplicationSession.getInstance().getOrderSelectedVehicle() != null) {

            try {
                Vehicle vehicle = vehicleSession;
                
                totalPrice = BigDecimal.ZERO;
                setEngineLblName(vehicle, lblEngineDisplacement);
                
                txtVehicleBrand.setText(vehicle.getModel().getBrandName());
                txtVehicleModel.setText(vehicle.getModel().getName());
                txtVehicleVinNumber.setText(vehicle.getViNumber());
                txtVehicleBodyType.setText(vehicle.getBodyType().toString());
                txtVehicleEngineDispl.setText(String.valueOf(vehicle.getEngineDisplacement()));
                txtVehicleEnginePow.setText(String.valueOf(vehicle.getEnginePower()));
                txtVehicleFuelType.setText(vehicle.getFuelType().toString());
                txtVehicleYearOfProduction.setText(String.valueOf(vehicle.getYearOfProd()));
                txtVehiclePrice.setText(String.valueOf(vehicle.getPrice()) + " " + vehicle.getCurrency().toString());
                totalPrice = totalPrice.add(vehicle.getPrice());
                
                
                List<Equipment> equipments = (List<Equipment>) getResponse(Operation.EQUIPMENT_GET_ALL, null).getResult();
                populateCombo(comboItem, equipments, vehicle);
                
                purchaseOrderItems.forEach(item -> totalPrice = totalPrice.add(item.getEquipment().getPrice()));
                totalAmount(txtTotalPrice);
                
            } catch (Exception ex) {
                Logger.getLogger(PurchaseOrderEditController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void prepareForm(JLabel lblPurchaseOrderId, JTextField txtSalesPersonName, JTextField txtCompany, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
    JTextField txtCustomerName, JTextField txtCustomerCompany, JTextField txtCustomerAddress, JTextField txtCustomerPhone, JTextField txtCustomerEmail, LocalDate date) {
        PurchaseOrder purchaseOrder = getPurchaseOrder();
        Customer customer = purchaseOrder.getCustomer();
        Vehicle vehicle = purchaseOrder.getVehicle();
        User salesPerson = purchaseOrder.getSalesPerson();
        date = purchaseOrder.getDate();

        lblPurchaseOrderId.setText(String.valueOf(purchaseOrder.getPurchaseOrderNum()));
        txtSalesPersonName.setText(salesPerson.getFirstName() + " " + salesPerson.getLastName());
        txtCompany.setText(vehicle.getBusinessUnit().getName());
        txtAddress.setText(vehicle.getBusinessUnit().getAddress() + ", " + vehicle.getBusinessUnit().getCity());
        txtPhone.setText(vehicle.getBusinessUnit().getPhone());
        txtEmail.setText(vehicle.getBusinessUnit().getEmail());

        txtCustomerName.setText(customer.getName());
        txtCustomerCompany.setText(customer.getCompanyName());
        txtCustomerAddress.setText(customer.getAddress());
        txtCustomerPhone.setText(customer.getPhone());
        txtCustomerEmail.setText(customer.getEmail());

    }

    public static void prepareVehicleInfoFields(JTextField txtVehicleBrand, JTextField txtVehicleModel, JTextField txtVehicleVinNumber, JTextField txtVehicleBodyType, JTextField txtVehicleEngineDispl,
    JTextField txtVehicleEnginePow, JTextField txtVehicleFuelType, JTextField txtVehicleYearOfProduction, JTextField txtVehiclePrice, JComboBox comboItem,
    List<PurchaseOrderItem> purchaseOrderItems, JLabel lblEngineDisplJLabel, JLabel lblPurchaseDate, LocalDate date, JTable tblItems, JTextField txtTotalPrice) throws NumberFormatException {
        try {
            prepareDefaultValues();
            PurchaseOrder purchaseOrder = getPurchaseOrder();
            Vehicle vehicle = purchaseOrder.getVehicle();
            txtVehicleBrand.setText(vehicle.getModel().getBrandName());
            txtVehicleModel.setText(vehicle.getModel().getName());
            txtVehicleVinNumber.setText(vehicle.getViNumber());
            txtVehicleBodyType.setText(vehicle.getBodyType().toString());
            txtVehicleEngineDispl.setText(String.valueOf(vehicle.getEngineDisplacement()));
            txtVehicleEnginePow.setText(String.valueOf(vehicle.getEnginePower()));
            txtVehicleFuelType.setText(vehicle.getFuelType().toString());
            txtVehicleYearOfProduction.setText(String.valueOf(vehicle.getYearOfProd()));
            txtVehiclePrice.setText(String.valueOf(vehicle.getPrice()) + " " + vehicle.getCurrency());
            purchaseOrder.getPurchaseOrderItems().forEach(item -> purchaseOrderItems.add(item));
            currency = vehicle.getCurrency();
            defaultItemListSize = purchaseOrder.getPurchaseOrderItems().size();
            setEngineLblName(vehicle, lblEngineDisplJLabel);
            date = purchaseOrder.getDate();
            
            if (purchaseOrderItems.size() >= 1) {
                counter += Long.valueOf(String.valueOf(purchaseOrderItems.size()));
            }
            
            totalPrice = vehicle.getPrice();
            purchaseOrder.getPurchaseOrderItems().forEach(item -> totalPrice = totalPrice.add(item.getEquipment().getPrice().multiply(BigDecimal.valueOf(item.getQuantity().longValue()))));
            
            setLabelDate(date, lblPurchaseDate);
            totalAmount(txtTotalPrice);
            
            List<Equipment> equipments = (List<Equipment>) getResponse(Operation.EQUIPMENT_GET_ALL, null).getResult();

            populateCombo(comboItem, equipments, vehicle);
            fillTable(tblItems, purchaseOrderItems);
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addItem(JComboBox comboItem, JSpinner spinnerQuantity, List<PurchaseOrderItem> purchaseOrderItems, JButton btnDeleteItem, JTable tblItems, JTextField txtTotalPrice, JDialog dialog) {
        if (comboItem.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(dialog, "Niste izabrali opremu!");
            return;
        }

        Equipment equipment = (Equipment) comboItem.getSelectedItem();
        Integer quantity = (Integer) spinnerQuantity.getValue();

        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setEquipment(equipment);
        purchaseOrderItem.setQuantity(quantity);
        purchaseOrderItem.setOrdinalNum(counter++);
        purchaseOrderItems.add(purchaseOrderItem);

        totalPrice = totalPrice.add(equipment.getPrice().multiply(BigDecimal.valueOf(quantity.longValue())));

        buttonDeleteItemSetStatus(purchaseOrderItems, btnDeleteItem);

        spinnerQuantity.setValue(1);

        fillTable(tblItems, purchaseOrderItems);
        totalAmount(txtTotalPrice);
    }

    public static void deleteItem(JTable tblItems, List<PurchaseOrderItem> purchaseOrderItems, JButton btnDeleteItem, JTextField txtTotalPrice) {
        int[] selectedRows = tblItems.getSelectedRows();

        for (int selectedRow : selectedRows) {
            Long ordinalNum = (Long) tblItems.getValueAt(selectedRow, 0);
            BigDecimal price = (BigDecimal) tblItems.getValueAt(selectedRow, 3);

            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
            purchaseOrderItem.setOrdinalNum(ordinalNum);

            totalPrice = totalPrice.subtract(price);

            purchaseOrderItems.remove(purchaseOrderItem);
            swapItemOrdinalNumber(ordinalNum, purchaseOrderItems);
            counter--;
        }

        buttonDeleteItemSetStatus(purchaseOrderItems, btnDeleteItem);

        fillTable(tblItems, purchaseOrderItems);
        totalAmount(txtTotalPrice);
    }
    
    public static void prepareDefaultValues(){
        counter = 1L;
        defaultItemListSize = 0;
    }

    public static void populateCombo(JComboBox comboItem, List<Equipment> equipments, Vehicle vehicle) {
        try {

            comboItem.removeAll();

            List<Equipment> equipmentsByBrand = equipments.stream()
            .filter(equipment -> equipment.getBrand().getBrandName().equals(vehicle.getModel().getBrandName()))
            .collect(Collectors.toList());
            equipmentsByBrand.forEach(comboItem::addItem);

        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void swapItemOrdinalNumber(Long ordinalNum, List<PurchaseOrderItem> purchaseOrderItems) {
        if (purchaseOrderItems.isEmpty()) {
            return;
        }

        Long temp = ordinalNum;
        for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
            if (purchaseOrderItem.getOrdinalNum() > ordinalNum) {
                purchaseOrderItem.setOrdinalNum(temp++);
            }
        }
    }

    private static void fillTable(JTable tblItems, List<PurchaseOrderItem> purchaseOrderItems) {
        try {
            tblItems.setModel(new PurchaseOrderItemTableModel(purchaseOrderItems));
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderEditForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setLabelDate(LocalDate date, JLabel lblPurchaseDate) {
        lblPurchaseDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    private static void totalAmount(JTextField txtTotalPrice) {
        txtTotalPrice.setText(String.valueOf(totalPrice) + " " + currency.toString());
    }

    private static void buttonDeleteItemSetStatus(List<PurchaseOrderItem> purchaseOrderItems, JButton btnDeleteItem) {
        if (!purchaseOrderItems.isEmpty()) {
            btnDeleteItem.setEnabled(true);
        } else {
            btnDeleteItem.setEnabled(false);
        }
    }

    public static int confirmDialog(String message, JDialog dialog) {
        String[] options = {"Da", "Ne", "Odustani"};
        int answer = JOptionPane.showOptionDialog(dialog, message, "Upozorenje!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        return answer;
    }

    public static void setEngineLblName(Vehicle vehicle, JLabel lblEngineDisplacement) {
        if (vehicle.getFuelType() == FuelType.ELEKTRICNIPOGON) {
            lblEngineDisplacement.setText("Kapacitet baterije:");
        } else {
            lblEngineDisplacement.setText("Zapremina motora:");
        }
    }
    
    private static Response getResponse(Operation operation, Object argument) throws Exception {
        Request request = new Request(operation, argument);
        Communication.getInstance().getSender().writeObject(request);
        Response response = (Response) Communication.getInstance().getReceiver().readObject();

        if (response.getException() != null) {
            throw response.getException();
        }

        return response;
    }
    
    public static PurchaseOrder getPurchaseOrder(){
        return ApplicationSession.getInstance().getPurchaseOrder();
    }

}
