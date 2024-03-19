package cardealershipapp.client.ui.purchaseorder.controller;

import cardealershipapp.client.ui.response.Responsive;
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
import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderCreateController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderCreateController.class);
    private final PurchaseOrderCreateForm purchaseOrderCreateForm;
    private Vehicle vehicleSession;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Currency currency = Currency.EUR;
    private Long counter = 1L;

    public PurchaseOrderCreateController(PurchaseOrderCreateForm purchaseOrderCreateForm) {
        this.purchaseOrderCreateForm = purchaseOrderCreateForm;
    }

    private static void fillTable(JTable tblItems, List<PurchaseOrderItem> purchaseOrderItems) {
        tblItems.setModel(new PurchaseOrderItemTableModel(purchaseOrderItems));
    }

    public void save() {
        try {
            String customerName = purchaseOrderCreateForm.getTxtCustomerName().getText().trim();
            String customerCompany = purchaseOrderCreateForm.getTxtCustomerCompany().getText().trim();
            String customerAddress = purchaseOrderCreateForm.getTxtCustomerAddress().getText().trim();
            String customerPhone = purchaseOrderCreateForm.getTxtCustomerPhone().getText().trim();
            String customerEmail = purchaseOrderCreateForm.getTxtCustomerEmail().getText().trim();

            validateInput();

            Vehicle vehicle = ApplicationSession.getInstance().getOrderSelectedVehicle();
            User salesPerson = ApplicationSession.getInstance().getLoggedUser().getUser();

            Customer customer = new Customer(null, customerName, customerCompany, customerAddress, customerPhone, customerEmail);

            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setDate(LocalDate.now());
            purchaseOrder.setCustomer(customer);
            purchaseOrder.setVehicle(vehicle);
            purchaseOrder.setSalesPerson(salesPerson);
            purchaseOrder.setPurchaseOrderItems(purchaseOrderCreateForm.getPurchaseOrderItems());

            getResponse(Operation.PURCHASE_ORDER_ADD, purchaseOrder);

            JOptionPane.showMessageDialog(purchaseOrderCreateForm, "Porudžbenica je uspešno kreirana","", JOptionPane.INFORMATION_MESSAGE);
            closeForm(purchaseOrderCreateForm);

        } catch (InputValidationException | ServiceException e) {
            log.warn("PurchaseOrderCreateController (save) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderCreateForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom čuvanja porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderCreateForm,"Došlo je do neočekivane greške prilikom čuvanja porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void activeForm() {

        UserProfile user = ApplicationSession.getInstance().getLoggedUser();
        purchaseOrderCreateForm.getTxtSalesPersonName().setText(user.getUser().getFirstName() + " " + user.getUser().getLastName());

        if (ApplicationSession.getInstance().getOrderSelectedVehicle() == null)
            return;

        Vehicle vehicle = ApplicationSession.getInstance().getOrderSelectedVehicle();
        vehicleSession = vehicle;

        if (vehicle.getFuelType() == FuelType.ELEKTRICNIPOGON) {
            purchaseOrderCreateForm.getLblEngineDisplacement().setText("Kapacitet baterije:");
        } else {
            purchaseOrderCreateForm.getLblEngineDisplacement().setText("Zapremina motora:");
        }

        purchaseOrderCreateForm.getTxtCompany().setText(vehicle.getBusinessUnit().getName());
        purchaseOrderCreateForm.getTxtAddress().setText(vehicle.getBusinessUnit().getAddress());
        purchaseOrderCreateForm.getTxtPhone().setText(vehicle.getBusinessUnit().getPhone());
        purchaseOrderCreateForm.getTxtEmail().setText(vehicle.getBusinessUnit().getEmail());

    }

    public void populateVehicleInfoFields() {

        if (ApplicationSession.getInstance().getOrderSelectedVehicle() == null)
            return;

        try {
            Vehicle vehicle = vehicleSession;
            purchaseOrderCreateForm.getTxtVehicleBrand().setText(vehicle.getModel().getBrandName());
            purchaseOrderCreateForm.getTxtVehicleModel().setText(vehicle.getModel().getName());
            purchaseOrderCreateForm.getTxtVehicleVinNumber().setText(vehicle.getViNumber());
            purchaseOrderCreateForm.getTxtVehicleBodyType().setText(vehicle.getBodyType().toString());
            purchaseOrderCreateForm.getTxtVehicleEngineDispl().setText(String.valueOf(vehicle.getEngineDisplacement()));
            purchaseOrderCreateForm.getTxtVehicleEnginePow().setText(String.valueOf(vehicle.getEnginePower()));
            purchaseOrderCreateForm.getTxtVehicleFuelType().setText(vehicle.getFuelType().toString());
            purchaseOrderCreateForm.getTxtVehicleYearOfProduction().setText(String.valueOf(vehicle.getYearOfProd()));
            purchaseOrderCreateForm.getTxtVehiclePrice().setText(vehicle.getPrice().toString());
            System.out.println("Vehicle price: " + purchaseOrderCreateForm.getTxtVehiclePrice().getText());
            totalPrice = vehicle.getPrice();
            currency = vehicle.getCurrency();

            Response equipmentResponse = getResponse(Operation.EQUIPMENT_GET_ALL, null);
            List<Equipment> equipments = (List<Equipment>) equipmentResponse.getResult();
            populateCombo(vehicle, purchaseOrderCreateForm.getComboItem(), equipments);
            totalAmount(purchaseOrderCreateForm.getTxtTotalPrice());
        } catch (ServiceException e) {
            log.warn("PurchaseOrderCreateController (populateVehicleInfoFields) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderCreateForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom popunjavanja polja porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderCreateForm,"Došlo je do neočekivane greške prilikom popunjavanja polja porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validateInput() throws InputValidationException {

        String phoneNumber = purchaseOrderCreateForm.getTxtCustomerPhone().getText().trim();

        if (purchaseOrderCreateForm.getTxtCustomerName().getText().trim().isEmpty()) {
            throw new InputValidationException("Polje Ime i prezime nije popunjeno!");
        } else if (purchaseOrderCreateForm.getTxtCustomerName().getText().trim().isEmpty()) {
            throw new InputValidationException("Polje Adresa nije popunjeno!");
        }

        if (phoneNumber.isEmpty()) {
            throw new InputValidationException("Polje Telefon nije popunjeno!");
        }
        if (phoneNumber.length() != 11) {
            throw new InputValidationException("Duzina broja telefona zajedno sa (/) i (-) treba da bude 11");
        } else if (!phoneNumber.substring(3, 4).equals("/") || !phoneNumber.substring(7, 8).equals("-")) {
            throw new InputValidationException("Broj telefona mora biti bbb/bbb-bbb formata");
        }

    }

    public void addItem() {
        if (purchaseOrderCreateForm.getComboItem().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(purchaseOrderCreateForm, "Niste izabrali opremu!");
            return;
        }

        Equipment equipment = (Equipment) purchaseOrderCreateForm.getComboItem().getSelectedItem();
        Integer quantity = (Integer) purchaseOrderCreateForm.getSpinnerQuantity().getValue();

        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setEquipment(equipment);
        purchaseOrderItem.setQuantity(quantity);
        purchaseOrderItem.setOrdinalNum(counter++);
        purchaseOrderCreateForm.getPurchaseOrderItems().add(purchaseOrderItem);

        totalPrice = totalPrice.add(equipment.getPrice().multiply(BigDecimal.valueOf(quantity.longValue())));

        if (!purchaseOrderCreateForm.getPurchaseOrderItems().isEmpty()) {
            purchaseOrderCreateForm.getBtnDeleteItem().setEnabled(true);
        }

        purchaseOrderCreateForm.getSpinnerQuantity().setValue(1);

        fillTable(purchaseOrderCreateForm.getTblItems(), purchaseOrderCreateForm.getPurchaseOrderItems());
        totalAmount(purchaseOrderCreateForm.getTxtTotalPrice());
    }

    public void deleteItem() {
        int[] selectedRows = purchaseOrderCreateForm.getTblItems().getSelectedRows();

        for (int selectedRow : selectedRows) {
            Long ordinalNum = (Long) purchaseOrderCreateForm.getTblItems().getValueAt(selectedRow, 0);
            BigDecimal price = (BigDecimal) purchaseOrderCreateForm.getTblItems().getValueAt(selectedRow, 3);

            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
            purchaseOrderItem.setOrdinalNum(ordinalNum);

            totalPrice = totalPrice.subtract(price);

            purchaseOrderCreateForm.getPurchaseOrderItems().remove(purchaseOrderItem);
            swapItemOrdinalNumber(ordinalNum, purchaseOrderCreateForm.getPurchaseOrderItems());
            counter--;
        }

        if (purchaseOrderCreateForm.getPurchaseOrderItems().isEmpty()) {
            purchaseOrderCreateForm.getBtnDeleteItem().setEnabled(false);
        }

        fillTable(purchaseOrderCreateForm.getTblItems(), purchaseOrderCreateForm.getPurchaseOrderItems());
        totalAmount(purchaseOrderCreateForm.getTxtTotalPrice());
    }

    private void populateCombo(Vehicle vehicle, JComboBox comboItem, List<Equipment> equipments) {
        try {

            comboItem.removeAll();

            List<Equipment> equipmentsByBrand = equipments.stream()
                    .filter(equipment -> equipment.getBrand().getBrandName().equals(vehicle.getModel().getBrandName()))
                    .collect(Collectors.toList());
            equipmentsByBrand.forEach(comboItem::addItem);

        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom učitavanja opreme u combobox: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderCreateForm, "Došlo je do neočekivane greške prilikom učitavanja opreme u combobox: " + ex.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void totalAmount(JTextField txtTotalPrice) {
        txtTotalPrice.setText(totalPrice + " " + currency.toString());
    }

    public void setLabeldate() {
        purchaseOrderCreateForm.getLblPurchaseDate().setText(purchaseOrderCreateForm.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    private void swapItemOrdinalNumber(Long ordinalNum, List<PurchaseOrderItem> purchaseOrderItems) {
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

    public int confirmDialog(String message, JDialog dialog) {
        String[] options = {"Da", "Ne", "Odustani"};
        int answer = JOptionPane.showOptionDialog(dialog, message, "Upozorenje!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        return answer;
    }

    public void closeForm(JDialog dialog) {
        ApplicationSession.getInstance().setOrderSelectedVehicle(null);
        ApplicationSession.getInstance().setPurchaseOrderFormIsOpen(false);
        dialog.dispose();
    }

}
