package cardealershipapp.client.ui.vehicle.ci;

import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.client.ui.vehicle.VehicleSearchForm;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.CarBodyType;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.MyTableCustomComponents;
import cardealershipapp.client.ui.component.table.model.VehicleTableModel;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderCreateForm;
import cardealershipapp.client.ui.validation.SelectRowException;
import cardealershipapp.client.ui.vehicle.filter.Criterium;
import cardealershipapp.client.ui.vehicle.sort.SortList;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Response;
import cardealershipapp.client.ui.vehicle.VehicleAddForm;
import cardealershipapp.client.ui.vehicle.VehicleEditForm;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.validation.InputValidationException;

import java.awt.Component;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import javax.swing.table.TableCellRenderer;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleSearchController implements Responsive {

    private List<Vehicle> mainVehicleList = new ArrayList<>();
    private VehicleSearchForm vehicleSearchForm;

    public VehicleSearchController(VehicleSearchForm vehicleSearchForm) {
        this.vehicleSearchForm = vehicleSearchForm;
    }

    private static int[] validateSelection(JTable tblCities) throws Exception {
        int[] selectedRows = tblCities.getSelectedRows();

        if (selectedRows.length == 0) {
            throw new SelectRowException("Niste selektovali red u tabeli!");
        }
        return selectedRows;
    }

    public void add(JDialog dialog) {
        JDialog vehicleAddDialog = new VehicleAddForm(null, true);
        vehicleAddDialog.setLocationRelativeTo(dialog);
        vehicleAddDialog.setVisible(true);
    }

    public void edit() {
        try {
            int[] selectedRows = validateSelection(vehicleSearchForm.getTblVehicles());

            if (selectedRows.length > 1) {
                throw new SelectRowException("Funkcija nije omogucena, selektovano je vise od jednog reda!");
            }

            String vin = (String) vehicleSearchForm.getTblVehicles().getValueAt(selectedRows[0], 2);
            Response response = getResponse(Operation.VEHICLE_GET_ALL, null);

            List<Vehicle> vehicles = ((List<Vehicle>) response.getResult()).stream().filter(vehicle -> vehicle.getViNumber().equals(vin)).collect(Collectors.toList());
            Vehicle vehicle = vehicles.get(0);
            ApplicationSession.getInstance().setVehicle(vehicle);

            JDialog vehicleEditDialog = new VehicleEditForm(null, true);
            vehicleEditDialog.setLocationRelativeTo(vehicleEditDialog);
            vehicleEditDialog.setVisible(true);

        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(vehicleSearchForm, sre.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void delete() {
        try {
            int[] selectedRows = validateSelection(vehicleSearchForm.getTblVehicles());
            int answer;
            String confirmMessage;

            if (selectedRows.length == 1) {
                String brand = (String) vehicleSearchForm.getTblVehicles().getValueAt(selectedRows[0], 0);
                String model = (String) vehicleSearchForm.getTblVehicles().getValueAt(selectedRows[0], 1);
                answer = option("Da li ste sigurni da zelite da obrisete vozilo " + brand.toUpperCase() + " " + model.toUpperCase(), vehicleSearchForm);
                confirmMessage = "Vozilo je uspesno obrisano!";
            } else {
                answer = option("Da li ste sigurni da zelite da obrisete selektovane redove?", vehicleSearchForm);
                confirmMessage = "Selektovana vozila su uspesno obrisana!";
            }

            if (answer == JOptionPane.YES_OPTION) {
                List<Vehicle> checkList = new ArrayList<>();
                if (selectedRows.length == 1) {
                    String vin = (String) vehicleSearchForm.getTblVehicles().getValueAt(selectedRows[0], 2);
                    Vehicle v = new Vehicle();
                    v.setViNumber(vin);
                    checkList.add(v);
                    checkBeforeDelete(checkList);
                    getResponse(Operation.VEHICLE_DELETE_BY_VIN, v);
                } else {

                    List<Vehicle> vehicles = new ArrayList<>();
                    for (int row : selectedRows) {
                        Vehicle v = new Vehicle();
                        v.setViNumber((String) vehicleSearchForm.getTblVehicles().getValueAt(row, 2));
                        vehicles.add(v);
                    }
                    getResponse(Operation.VEHICLE_DELETE_MULTIPLE_BY_VIN, vehicles);

                }

                JOptionPane.showMessageDialog(vehicleSearchForm, confirmMessage);

            }

        } catch (SelectRowException | InputValidationException val) {
            JOptionPane.showMessageDialog(vehicleSearchForm, val.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void addToPurchaseOrder() {

        try {

            int[] selectedRows = validateSelection(vehicleSearchForm.getTblVehicles());

            if (selectedRows.length > 1) {
                throw new SelectRowException("Dodavanje nije omoguceno, selektovano je vise od jednog reda!");
            }

            String vin = (String) vehicleSearchForm.getTblVehicles().getValueAt(selectedRows[0], 2);
            List<PurchaseOrder> po = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();

            for (PurchaseOrder purchaseOrder : po) {
                if (purchaseOrder.getVehicle().getViNumber().equals(vin)) {
                    throw new InputValidationException("Vozilo koje ste selektovali je prodato!");
                }
            }

            Response vehicleResponse = getResponse(Operation.VEHICLE_GET_ALL, null);
            List<Vehicle> vehicles = (List<Vehicle>) vehicleResponse.getResult();

            List<Vehicle> selectedVehicles = vehicles.stream()
                    .filter(selectedVehicle -> selectedVehicle.getViNumber().equals(vin))
                    .collect(Collectors.toList());
            ApplicationSession.getInstance().setOrderSelectedVehicle(selectedVehicles.get(0));
            if (ApplicationSession.getInstance().isPurchaseOrderFormIsOpen()) {
                vehicleSearchForm.dispose();
                return;
            }

            JDialog purchaseOrderDialog = new PurchaseOrderCreateForm(null, true);
            purchaseOrderDialog.setLocationRelativeTo(vehicleSearchForm);
            purchaseOrderDialog.setVisible(true);

        } catch (SelectRowException | InputValidationException val) {
            JOptionPane.showMessageDialog(vehicleSearchForm, val.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void search() {
        try {
            List<JComboBox> combos = Arrays.asList(vehicleSearchForm.getComboBrand(), vehicleSearchForm.getComboModel(),
                    vehicleSearchForm.getComboBodyType(), vehicleSearchForm.getComboFuelType(),
                    vehicleSearchForm.getComboYearFrom(), vehicleSearchForm.getComboYearTo(), vehicleSearchForm.getComboCity());

            boolean selected = checkComboIndex(combos);

            if (!selected && vehicleSearchForm.getComboSortList().getSelectedIndex() == 0) {
                fillTable();
                return;
            } else if (!selected && vehicleSearchForm.getComboSortList().getSelectedIndex() > 0) {
                fillTable();
                sortMainList();
                return;
            }

            List<Vehicle> allVehicles = (List<Vehicle>) getResponse(Operation.VEHICLE_GET_ALL, null).getResult();

            Criterium criterium = new Criterium(allVehicles, vehicleSearchForm.getComboBrand(), vehicleSearchForm.getComboModel(),
                    vehicleSearchForm.getComboBodyType(), vehicleSearchForm.getComboFuelType(),
                    vehicleSearchForm.getComboYearFrom(), vehicleSearchForm.getComboYearTo(), vehicleSearchForm.getComboCity());
            mainVehicleList = criterium.getFilteredVehicles();

            if (vehicleSearchForm.getComboSortList().getSelectedIndex() > 0) {
                sortMainList();
            } else {
                vehicleSearchForm.getTblVehicles().setModel(new VehicleTableModel(mainVehicleList));
            }

            renderImage(vehicleSearchForm.getTblVehicles());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, "Doslo je do greske prilikom pretrage!!", "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void infoAdrea() {
        try {
            if (vehicleSearchForm.getTblVehicles().getSelectedRow() != -1) {
                vehicleSearchForm.getStringBuilder().delete(0, vehicleSearchForm.getStringBuilder().length());
                int selectedRow = vehicleSearchForm.getTblVehicles().getSelectedRow();
                String vin = (String) vehicleSearchForm.getTblVehicles().getValueAt(selectedRow, 2);

                List<Vehicle> allVehicles = (List<Vehicle>) getResponse(Operation.VEHICLE_GET_ALL, null).getResult();
                List<Vehicle> infoVehicles = allVehicles.stream().filter(vehicle -> vehicle.getViNumber().equals(vin)).collect(Collectors.toList());

                if (infoVehicles == null || infoVehicles.isEmpty()) {
                    return;
                }

                Vehicle vehicle = infoVehicles.get(0);
                BusinessUnit bu = (BusinessUnit) getResponse(Operation.BUSINESSUNIT_FIND_BY_ID, new BusinessUnit(vehicle.getBusinessUnit().getId())).getResult();
                City c = (City) getResponse(Operation.CITY_FIND_BY_ID, new City(bu.getCity().getId())).getResult();

                if (bu.getEmail() == null) {
                    bu.setEmail("");
                }
                vehicleSearchForm.getTxtAreaBusinessUnitInfo().setText(vehicleSearchForm.getStringBuilder().append("\t======").append(bu.getName()).append("======").append("\n")
                        .append("Maticni broj: ").append(bu.getCompanyRegId()).append("\n")
                        .append("PIB: ").append(bu.getTaxId()).append("\n")
                        .append("Adresa: ").append(bu.getAddress()).append(", ").append(c.getName()).append("\n")
                        .append("Kontakt Telefon: ").append(bu.getPhone()).append("\n")
                        .append("Email: ").append(bu.getEmail()).toString());
            } else {
                vehicleSearchForm.getStringBuilder().delete(0, vehicleSearchForm.getStringBuilder().length());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void fillTable() {
        try {
            List<Vehicle> allVehicles = (List<Vehicle>) getResponse(Operation.VEHICLE_GET_ALL, null).getResult();

            mainVehicleList = allVehicles.stream().sorted(Comparator.comparing(vehicle -> vehicle.getModel().getName())).collect(Collectors.toList());
            vehicleSearchForm.getTblVehicles().setModel(new VehicleTableModel(mainVehicleList));
            renderImage(vehicleSearchForm.getTblVehicles());
            MyTableCustomComponents.setTblHeader(vehicleSearchForm.getTblVehicles());
            MyTableCustomComponents.centerCellText(vehicleSearchForm.getTblVehicles());

        } catch (SocketException soe) {
            JOptionPane.showMessageDialog(vehicleSearchForm, soe.getMessage(), "Upozorenje!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void fillCombo() {

        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            List<City> cities = (List<City>) getResponse(Operation.CITY_GET_ALL, null).getResult();

            brands.stream().sorted(Comparator.comparing(Brand::getBrandName)).forEach(vehicleSearchForm.getComboBrand()::addItem);
            Arrays.asList(CarBodyType.values()).forEach(vehicleSearchForm.getComboBodyType()::addItem);
            Arrays.asList(FuelType.values()).forEach(vehicleSearchForm.getComboFuelType()::addItem);
            cities.stream().sorted(Comparator.comparing(City::getName)).forEach(vehicleSearchForm.getComboCity()::addItem);

            LocalDate ld = LocalDate.now();
            int currentYear = ld.getYear();
            List<Integer> years = new ArrayList<>();
            for (int i = currentYear; i >= (currentYear - 10); i--) {
                years.add(i);
            }

            years.forEach(vehicleSearchForm.getComboYearFrom()::addItem);
            years.forEach(vehicleSearchForm.getComboYearTo()::addItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateComboModel() {
        try {
            vehicleSearchForm.getComboModel().removeAllItems();

            if (vehicleSearchForm.getComboBrand().getSelectedIndex() == 0) {
                vehicleSearchForm.getComboModel().addItem("Model");
            } else {
                Brand brand = (Brand) vehicleSearchForm.getComboBrand().getSelectedItem();
                vehicleSearchForm.getComboModel().addItem("Svi modeli");
                List<Model> allModels = (List<Model>) getResponse(Operation.MODEL_GET_ALL, null).getResult();
                allModels.stream().sorted(Comparator.comparing(Model::getName))
                        .filter(model -> model.getBrand().equals(brand)).forEach(vehicleSearchForm.getComboModel()::addItem);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void sortMainList() {
        SortList sortList = new SortList(mainVehicleList, vehicleSearchForm.getComboSortList());
        vehicleSearchForm.getTblVehicles().setModel(new VehicleTableModel(sortList.getSortedVehicles()));
        renderImage(vehicleSearchForm.getTblVehicles());
        vehicleSearchForm.setFiltered(true);
    }

    public void loadPurchaseOrders() {
        try {
            List<PurchaseOrder> po = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();
            VehicleTableModel.setPurchaseOrders(po);
        } catch (SocketException soe) {
            JOptionPane.showMessageDialog(vehicleSearchForm, soe.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(VehicleSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filterTable() {
        if (!vehicleSearchForm.isFiltered()) {
            fillTable();
        } else {
            sortMainList();
        }
    }


    private void renderImage(JTable tblVehicles) {

        TableCellRenderer tblCellRenderer = (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) -> (Component) value;

        tblVehicles.getColumn("*").setCellRenderer(tblCellRenderer);
        tblVehicles.getColumnModel().getColumn(9).setMaxWidth(34);
    }

    private boolean checkComboIndex(List<JComboBox> combos) {
        for (JComboBox c : combos) {
            if (c.getSelectedIndex() > 0) {
                return true;
            }
        }
        return false;
    }

    private void checkBeforeDelete(List<Vehicle> vehicleList) throws Exception {
        List<PurchaseOrder> purchaseOrders = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();

        for (PurchaseOrder purchaseOrder : purchaseOrders) {

            for (Vehicle v : vehicleList) {
                if (v.getViNumber().equals(purchaseOrder.getVehicle().getViNumber())) {
                    throw new InputValidationException("Vozila koja su prodata nije moguce obrisati iz baze!");
                }

            }
        }
    }

    private int option(String message, JDialog dialog) {
        String[] options = {"Da", "Ne"};
        return JOptionPane.showOptionDialog(dialog, message, "Paznja!",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

}
