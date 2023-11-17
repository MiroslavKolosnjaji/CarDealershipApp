package cardealershipapp.client.ui.purchaseorder.ci;

import cardealershipapp.client.communication.Communication;
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
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderCreateForm;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
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
public class PurchaseOrderCreateController {

    private static Vehicle vehicleSession;
    private static BigDecimal totalPrice = BigDecimal.ZERO;
    private static Currency currency = Currency.EUR;
    private static Long counter = 1L;

    public static void add(JTextField txtCustomerName, JTextField txtCustomerCompany, JTextField txtCustomerAddress, JTextField txtCustomerPhone, JTextField txtCustomerEmail,
    List<PurchaseOrderItem> purchaseOrderItems, JDialog dialog) {
        try {
            String customerName = txtCustomerName.getText().trim();
            String customerCompany = txtCustomerCompany.getText().trim();
            String customerAddress = txtCustomerAddress.getText().trim();
            String customerPhone = txtCustomerPhone.getText().trim();
            String customerEmail = txtCustomerEmail.getText().trim();

            validateInput(txtCustomerName, txtCustomerAddress, txtCustomerPhone);

            Vehicle vehicle = ApplicationSession.getInstance().getOrderSelectedVehicle();
            User salesPerson = ApplicationSession.getInstance().getLoggedUser().getUser();

            Customer customer = new Customer(null, customerName, customerCompany, customerAddress, customerPhone, customerEmail);
            getResponse(Operation.CUSTOMER_ADD, customer);

            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setDate(LocalDate.now());
            purchaseOrder.setCustomer(customer);
            purchaseOrder.setVehicle(vehicle);
            purchaseOrder.setSalesPerson(salesPerson);
            purchaseOrder.setPurchaseOrderItems(purchaseOrderItems);

            getResponse(Operation.PURCHASE_ORDER_ADD, purchaseOrder);
           

            JOptionPane.showMessageDialog(dialog, "Porudzbenica je uspesno kreirana");
            closeForm(dialog);

        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(dialog, ive.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Upozorenje!", JOptionPane.WARNING_MESSAGE);
        }

    }

    public static void activeForm(JTextField txtSalesPersonName, JTextField txtCompany, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
    JLabel lblEngineDisplacement, JComboBox comboItem) {

        UserProfile user = ApplicationSession.getInstance().getLoggedUser();
        txtSalesPersonName.setText(user.getUser().getFirstName() + " " + user.getUser().getLastName());

        if (ApplicationSession.getInstance().getOrderSelectedVehicle() != null) {
            Vehicle vehicle = ApplicationSession.getInstance().getOrderSelectedVehicle();
            vehicleSession = vehicle;

            if (vehicle.getFuelType() == FuelType.ELEKTRICNIPOGON) {
                lblEngineDisplacement.setText("Kapacitet baterije:");
            } else {
                lblEngineDisplacement.setText("Zapremina motora:");
            }

            txtCompany.setText(vehicle.getBusinessUnit().getName());
            txtAddress.setText(vehicle.getBusinessUnit().getAddress());
            txtPhone.setText(vehicle.getBusinessUnit().getPhone());
            txtEmail.setText(vehicle.getBusinessUnit().getEmail());
        }
    }

    public static void populateVehicleInfoFields(JTextField txtVehicleBrand, JTextField txtVehicleModel, JTextField txtVehicleVinNumber, JTextField txtVehicleBodyType,
    JTextField txtVehicleEngineDispl, JTextField txtVehicleEnginePow, JTextField txtVehicleFuelType, JTextField txtVehicleYearOfProduction, JTextField txtVehiclePrice, JTextField txtTotalPrice, JComboBox comboItem) {

        if (ApplicationSession.getInstance().getOrderSelectedVehicle() != null) {
            try {
                Vehicle vehicle = vehicleSession;
                txtVehicleBrand.setText(vehicle.getModel().getBrandName());
                txtVehicleModel.setText(vehicle.getModel().getName());
                txtVehicleVinNumber.setText(vehicle.getViNumber());
                txtVehicleBodyType.setText(vehicle.getBodyType().toString());
                txtVehicleEngineDispl.setText(String.valueOf(vehicle.getEngineDisplacement()));
                txtVehicleEnginePow.setText(String.valueOf(vehicle.getEnginePower()));
                txtVehicleFuelType.setText(vehicle.getFuelType().toString());
                txtVehicleYearOfProduction.setText(String.valueOf(vehicle.getYearOfProd()));
                txtVehiclePrice.setText(String.valueOf(vehicle.getPrice()) + " " + vehicle.getCurrency().toString());
                totalPrice = vehicle.getPrice();
                currency = vehicle.getCurrency();
                
                Response equipmentResponse = getResponse(Operation.EQUIPMENT_GET_ALL, null);
                List<Equipment> equipments = (List<Equipment>) equipmentResponse.getResult();
                populateCombo(vehicle, comboItem, equipments);
                totalAmount(txtTotalPrice);
            } catch (Exception ex) {
                Logger.getLogger(PurchaseOrderCreateController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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

    public static void addItem(JTable tblItems, JComboBox comboItem, List<PurchaseOrderItem> purchaseOrderItems, JTextField txtTotalPrice, JSpinner spinnerQuantity, JButton btnDeleteItem, JDialog dialog) {
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

        if (!purchaseOrderItems.isEmpty()) {
            btnDeleteItem.setEnabled(true);
        }

        spinnerQuantity.setValue(1);

        fillTable(tblItems, purchaseOrderItems);
        totalAmount(txtTotalPrice);
    }

    public static void deleteItem(JTable tblItems, List<PurchaseOrderItem> purchaseOrderItems, JTextField txtTotalPrice, JButton btnDeleteItem) {
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

        if (purchaseOrderItems.isEmpty()) {
            btnDeleteItem.setEnabled(false);
        }

        fillTable(tblItems, purchaseOrderItems);
        totalAmount(txtTotalPrice);
    }

    private static void populateCombo(Vehicle vehicle, JComboBox comboItem, List<Equipment> equipments) {
        try {

            comboItem.removeAll();

            List<Equipment> equipmentsByBrand = equipments.stream()
            .filter(equipment -> equipment.getBrand().getBrandName().equals(vehicle.getModel().getBrandName()))
            .collect(Collectors.toList());
            equipmentsByBrand.forEach(comboItem::addItem);

        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderCreateForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void fillTable(JTable tblItems, List<PurchaseOrderItem> purchaseOrderItems) {
        tblItems.setModel(new PurchaseOrderItemTableModel(purchaseOrderItems));
    }

    public static void totalAmount(JTextField txtTotalPrice) {
        txtTotalPrice.setText(String.valueOf(totalPrice) + " " + currency.toString());
    }

    public static void setLabeldate(JLabel lblPurchaseDate, LocalDate date) {
        lblPurchaseDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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

    public static int confirmDialog(String message, JDialog dialog) {
        String[] options = {"Da", "Ne", "Odustani"};
        int answer = JOptionPane.showOptionDialog(dialog, message, "Upozorenje!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        return answer;
    }

    public static void closeForm(JDialog dialog) {
        ApplicationSession.getInstance().setOrderSelectedVehicle(null);
        ApplicationSession.getInstance().setPurchaseOrderFormIsOpen(false);
        dialog.dispose();
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
}
