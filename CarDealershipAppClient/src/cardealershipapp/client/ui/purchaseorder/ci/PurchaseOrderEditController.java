package cardealershipapp.client.ui.purchaseorder.ci;

import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
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
import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.common.domain.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderEditController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderCreateController.class);
    private final PurchaseOrderEditForm purchaseOrderEditForm;
    private Vehicle vehicleSession;
    private BigDecimal totalPrice;
    private Currency currency;
    private Long counter = 1L;
    private int defaultItemListSize = 0;

    public PurchaseOrderEditController(PurchaseOrderEditForm purchaseOrderEditForm) {
        this.purchaseOrderEditForm = purchaseOrderEditForm;
    }

    public void populateCombo(JComboBox comboItem, List<Equipment> equipments, Vehicle vehicle) {
        try {

            comboItem.removeAll();

            List<Equipment> equipmentsByBrand = equipments.stream()
                    .filter(equipment -> equipment.getBrand().getBrandName().equals(vehicle.getModel().getBrandName()))
                    .collect(Collectors.toList());

            equipmentsByBrand.forEach(comboItem::addItem);

        }  catch (Exception ex) {
            log.error("Neočekivana greška prilikom učitavanja modela u combobox: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderEditForm, "Došlo je do neočekivane greške prilikom učitavanja modela u combobox: " + ex.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void prepareForm() {
        PurchaseOrder purchaseOrder = getPurchaseOrder();
        Customer customer = purchaseOrder.getCustomer();
        Vehicle vehicle = purchaseOrder.getVehicle();
        User salesPerson = purchaseOrder.getSalesPerson();
        purchaseOrderEditForm.setDate(purchaseOrder.getDate());

        purchaseOrderEditForm.getLblPurchaseOrderID().setText(String.valueOf(purchaseOrder.getPurchaseOrderNum()));
        purchaseOrderEditForm.getTxtSalesPersonName().setText(salesPerson.getFirstName() + " " + salesPerson.getLastName());
        purchaseOrderEditForm.getTxtCompany().setText(vehicle.getBusinessUnit().getName());
        purchaseOrderEditForm.getTxtAddress().setText(vehicle.getBusinessUnit().getAddress() + ", " + vehicle.getBusinessUnit().getCity());
        purchaseOrderEditForm.getTxtPhone().setText(vehicle.getBusinessUnit().getPhone());
        purchaseOrderEditForm.getTxtEmail().setText(vehicle.getBusinessUnit().getEmail());

        purchaseOrderEditForm.getTxtCustomerName().setText(customer.getName());
        purchaseOrderEditForm.getTxtCustomerCompany().setText(customer.getCompanyName());
        purchaseOrderEditForm.getTxtCustomerAddress().setText(customer.getAddress());
        purchaseOrderEditForm.getTxtCustomerPhone().setText(customer.getPhone());
        purchaseOrderEditForm.getTxtCustomerEmail().setText(customer.getEmail());

    }

    public void prepareVehicleInfoFields() throws NumberFormatException {
        try {
            prepareDefaultValues();
            PurchaseOrder purchaseOrder = getPurchaseOrder();
            Vehicle vehicle = purchaseOrder.getVehicle();
            purchaseOrderEditForm.getTxtVehicleBrand().setText(vehicle.getModel().getBrandName());
            purchaseOrderEditForm.getTxtVehicleModel().setText(vehicle.getModel().getName());
            purchaseOrderEditForm.getTxtVehicleVinNumber().setText(vehicle.getViNumber());
            purchaseOrderEditForm.getTxtVehicleBodyType().setText(vehicle.getBodyType().toString());
            purchaseOrderEditForm.getTxtVehicleEngineDispl().setText(String.valueOf(vehicle.getEngineDisplacement()));
            purchaseOrderEditForm.getTxtVehicleEnginePow().setText(String.valueOf(vehicle.getEnginePower()));
            purchaseOrderEditForm.getTxtVehicleFuelType().setText(vehicle.getFuelType().toString());
            purchaseOrderEditForm.getTxtVehicleYearOfProduction().setText(String.valueOf(vehicle.getYearOfProd()));
            purchaseOrderEditForm.getTxtVehiclePrice().setText(vehicle.getPrice() + " " + vehicle.getCurrency());
            purchaseOrder.getPurchaseOrderItems().forEach(item -> purchaseOrderEditForm.getPurchaseOrderItems().add(item));
            currency = vehicle.getCurrency();
            defaultItemListSize = purchaseOrder.getPurchaseOrderItems().size();
            setEngineLblName(vehicle, purchaseOrderEditForm.getLblEngineDisplacement());
            purchaseOrderEditForm.setDate(purchaseOrder.getDate());

            if (!purchaseOrderEditForm.getPurchaseOrderItems().isEmpty()) {
                counter += Long.parseLong(String.valueOf(purchaseOrderEditForm.getPurchaseOrderItems().size()));
            }

            totalPrice = vehicle.getPrice();
            purchaseOrder.getPurchaseOrderItems().forEach(item -> totalPrice = totalPrice.add(item.getEquipment().getPrice().multiply(BigDecimal.valueOf(item.getQuantity().longValue()))));

            setLabelDate();
            totalAmount(purchaseOrderEditForm.getTxtTotalPrice());

            List<Equipment> equipments = (List<Equipment>) getResponse(Operation.EQUIPMENT_GET_ALL, null).getResult();

            populateCombo(purchaseOrderEditForm.getComboItem(), equipments, vehicle);
            fillTable(purchaseOrderEditForm.getTblItems(), purchaseOrderEditForm.getPurchaseOrderItems());
        } catch (ServiceException e) {
            log.warn("PurchaseOrderEditController (prepareVehicleInfoFields) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderEditForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom popunjavanja polja porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderEditForm,"Došlo je do neočekivane greške prilikom popunjavanja polja porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addItem() {
        if (purchaseOrderEditForm.getComboItem().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(purchaseOrderEditForm, "Niste izabrali opremu!");
            return;
        }

        Equipment equipment = (Equipment) purchaseOrderEditForm.getComboItem().getSelectedItem();
        Integer quantity = (Integer) purchaseOrderEditForm.getSpinnerQuantity().getValue();

        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setEquipment(equipment);
        purchaseOrderItem.setQuantity(quantity);
        purchaseOrderItem.setOrdinalNum(counter++);
        purchaseOrderEditForm.getPurchaseOrderItems().add(purchaseOrderItem);

        totalPrice = totalPrice.add(equipment.getPrice().multiply(BigDecimal.valueOf(quantity.longValue())));

        buttonDeleteItemSetStatus(purchaseOrderEditForm.getPurchaseOrderItems(), purchaseOrderEditForm.getBtnDeleteItem());

        purchaseOrderEditForm.getSpinnerQuantity().setValue(1);

        fillTable(purchaseOrderEditForm.getTblItems(), purchaseOrderEditForm.getPurchaseOrderItems());
        totalAmount(purchaseOrderEditForm.getTxtTotalPrice());
    }

    public void deleteItem() {
        int[] selectedRows = purchaseOrderEditForm.getTblItems().getSelectedRows();

        for (int selectedRow : selectedRows) {
            Long ordinalNum = (Long) purchaseOrderEditForm.getTblItems().getValueAt(selectedRow, 0);
            BigDecimal price = (BigDecimal) purchaseOrderEditForm.getTblItems().getValueAt(selectedRow, 3);

            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
            purchaseOrderItem.setOrdinalNum(ordinalNum);

            totalPrice = totalPrice.subtract(price);

            purchaseOrderEditForm.getPurchaseOrderItems().remove(purchaseOrderItem);
            swapItemOrdinalNumber(ordinalNum, purchaseOrderEditForm.getPurchaseOrderItems());
            counter--;
        }

        buttonDeleteItemSetStatus(purchaseOrderEditForm.getPurchaseOrderItems(), purchaseOrderEditForm.getBtnDeleteItem());

        fillTable(purchaseOrderEditForm.getTblItems(), purchaseOrderEditForm.getPurchaseOrderItems());
        totalAmount(purchaseOrderEditForm.getTxtTotalPrice());
    }

    public void prepareDefaultValues() {
        counter = 1L;
        defaultItemListSize = 0;
    }

    public void setLabelDate() {
        purchaseOrderEditForm.getLblPurchaseDate().setText(purchaseOrderEditForm.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public int confirmDialog(String message, JDialog dialog) {
        String[] options = {"Da", "Ne", "Odustani"};
        int answer = JOptionPane.showOptionDialog(dialog, message, "Upozorenje!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        return answer;
    }

    public void setEngineLblName(Vehicle vehicle, JLabel lblEngineDisplacement) {
        if (vehicle.getFuelType() == FuelType.ELEKTRICNIPOGON) {
            lblEngineDisplacement.setText("Kapacitet baterije:");
        } else {
            lblEngineDisplacement.setText("Zapremina motora:");
        }
    }

    public PurchaseOrder getPurchaseOrder() {
        return ApplicationSession.getInstance().getPurchaseOrder();
    }

    public void update() {
        try {

            int answer = confirmDialog("Podaci porudzbenice ce biti promenjeni! Da li se slazete?", purchaseOrderEditForm);

            if (answer == JOptionPane.NO_OPTION) {
                return;
            }

            String customerName = purchaseOrderEditForm.getTxtCustomerName().getText().trim();
            String customerCompany = purchaseOrderEditForm.getTxtCustomerCompany().getText().trim();
            String customerAddress = purchaseOrderEditForm.getTxtCustomerAddress().getText().trim();
            String customerPhone = purchaseOrderEditForm.getTxtCustomerPhone().getText().trim();
            String customerEmail = purchaseOrderEditForm.getTxtCustomerEmail().getText().trim();
            Long customerId = ApplicationSession.getInstance().getPurchaseOrder().getCustomer().getId();

            validateInput();

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

            for (PurchaseOrderItem purchaseOrderItem : purchaseOrderEditForm.getPurchaseOrderItems()) {
                purchaseOrderItem.setPurchaseOrder(purchaseOrder);
            }
            purchaseOrder.setPurchaseOrderItems(purchaseOrderEditForm.getPurchaseOrderItems());

            if (purchaseOrder.getPurchaseOrderItems().size() == defaultItemListSize) {
                getResponse(Operation.PURCHASE_ORDER_ITEM_UPDATE, purchaseOrder.getPurchaseOrderItems());
            } else {
                PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(new PurchaseOrder(purchaseOrder.getPurchaseOrderNum()));
                getResponse(Operation.PURCHASE_ORDER_ITEM_DELETE, purchaseOrderItem);
                getResponse(Operation.PURCHASE_ORDER_ITEM_ADD, purchaseOrder.getPurchaseOrderItems());
            }

            getResponse(Operation.PURCHASE_ORDER_UPDATE, purchaseOrder);
            JOptionPane.showMessageDialog(purchaseOrderEditForm, "Porudzbenica je uspesno azurirana");

            purchaseOrderEditForm.dispose();
            ApplicationSession.getInstance().setPurchaseOrder(purchaseOrder);
            showDetails();
        } catch (InputValidationException | ServiceException e) {
            log.warn("PurchaseOrderEditController (update) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderEditForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom ažuriranja porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderEditForm,"Došlo je do neočekivane greške prilikom ažuriranja porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void activeForm() {
        if (ApplicationSession.getInstance().getOrderSelectedVehicle() != null) {
            UserProfile user = ApplicationSession.getInstance().getLoggedUser();
            purchaseOrderEditForm.getTxtSalesPersonName().setText(user.getUser().getFirstName() + " " + user.getUser().getLastName());

            Vehicle vehicle = ApplicationSession.getInstance().getOrderSelectedVehicle();
            vehicleSession = vehicle;

            purchaseOrderEditForm.getTxtCompany().setText(vehicle.getBusinessUnit().getName());
            purchaseOrderEditForm.getTxtAddress().setText(vehicle.getBusinessUnit().getAddress());
            purchaseOrderEditForm.getTxtPhone().setText(vehicle.getBusinessUnit().getPhone());
            purchaseOrderEditForm.getTxtEmail().setText(vehicle.getBusinessUnit().getEmail());

        }
    }

    public void activeFormVehicleInfoFields() {
        if (ApplicationSession.getInstance().getOrderSelectedVehicle() == null)
            return;

        try {
            Vehicle vehicle = vehicleSession;

            totalPrice = BigDecimal.ZERO;
            setEngineLblName(vehicle, purchaseOrderEditForm.getLblEngineDisplacement());

            purchaseOrderEditForm.getTxtVehicleBrand().setText(vehicle.getModel().getBrandName());
            purchaseOrderEditForm.getTxtVehicleModel().setText(vehicle.getModel().getName());
            purchaseOrderEditForm.getTxtVehicleVinNumber().setText(vehicle.getViNumber());
            purchaseOrderEditForm.getTxtVehicleBodyType().setText(vehicle.getBodyType().toString());
            purchaseOrderEditForm.getTxtVehicleEngineDispl().setText(String.valueOf(vehicle.getEngineDisplacement()));
            purchaseOrderEditForm.getTxtVehicleEnginePow().setText(String.valueOf(vehicle.getEnginePower()));
            purchaseOrderEditForm.getTxtVehicleFuelType().setText(vehicle.getFuelType().toString());
            purchaseOrderEditForm.getTxtVehicleYearOfProduction().setText(String.valueOf(vehicle.getYearOfProd()));
            purchaseOrderEditForm.getTxtVehiclePrice().setText(String.valueOf(vehicle.getPrice()) + " " + vehicle.getCurrency().toString());
            totalPrice = totalPrice.add(vehicle.getPrice());


            List<Equipment> equipments = (List<Equipment>) getResponse(Operation.EQUIPMENT_GET_ALL, null).getResult();
            populateCombo(purchaseOrderEditForm.getComboItem(), equipments, vehicle);

            purchaseOrderEditForm.getPurchaseOrderItems().forEach(item -> totalPrice = totalPrice.add(item.getEquipment().getPrice()));
            totalAmount(purchaseOrderEditForm.getTxtTotalPrice());

        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom ažuriranja polja porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderEditForm,"Došlo je do neočekivane greške prilikom ažuriranja polja porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void showDetails() {
        JDialog details = new PurchaseOrderDetailsForm(null, true);
        details.setLocationRelativeTo(null);
        details.setVisible(true);
    }

    private void validateInput() throws InputValidationException {

        String phoneNumber = purchaseOrderEditForm.getTxtCustomerPhone().getText().trim();

        if (purchaseOrderEditForm.getTxtCustomerName().getText().trim().isEmpty()) {
            throw new InputValidationException("Polje Ime i prezime nije popunjeno!");
        } else if (purchaseOrderEditForm.getTxtCustomerAddress().getText().trim().isEmpty()) {
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

    private void fillTable(JTable tblItems, List<PurchaseOrderItem> purchaseOrderItems) {
        try {
            tblItems.setModel(new PurchaseOrderItemTableModel(purchaseOrderItems));
        }  catch (Exception ex) {
            log.error("Neočekivana greška prilikom popunjavanja tabele stavki: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderEditForm,"Došlo je do neočekivane greške prilikom popunjavanja tabele stavki: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void totalAmount(JTextField txtTotalPrice) {
        txtTotalPrice.setText(totalPrice + " " + currency.toString());
    }

    private void buttonDeleteItemSetStatus(List<PurchaseOrderItem> purchaseOrderItems, JButton btnDeleteItem) {
        if (!purchaseOrderItems.isEmpty()) {
            btnDeleteItem.setEnabled(true);
        } else {
            btnDeleteItem.setEnabled(false);
        }
    }


}
