package cardealershipapp.client.ui.vehicle.ci;

import cardealershipapp.client.communication.Communication;
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
import cardealershipapp.common.transfer.Request;
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
import javax.swing.JTextArea;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class VehicleSearchController {

    private static List<Vehicle> mainVehicleList = new ArrayList<>();

    public static void add(JDialog dialog) {
        JDialog vehicleAddDialog = new VehicleAddForm(null, true);
        vehicleAddDialog.setLocationRelativeTo(dialog);
        vehicleAddDialog.setVisible(true);
    }

    public static void edit(JTable tblVehicles, JDialog dialog) {
        try {
            int[] selectedRows = validateSelection(tblVehicles);

            if (selectedRows.length > 1) {
                throw new SelectRowException("Funkcija nije omogucena, selektovano je vise od jednog reda!");
            }

            String vin = (String) tblVehicles.getValueAt(selectedRows[0], 2);
            Response response = getResponse(Operation.VEHICLE_GET_ALL, null);

            List<Vehicle> vehicles = ((List<Vehicle>) response.getResult()).stream().filter(vehicle -> vehicle.getViNumber().equals(vin)).collect(Collectors.toList());
            Vehicle vehicle = vehicles.get(0);
            ApplicationSession.getInstance().setVehicle(vehicle);

            JDialog vehicleEditDialog = new VehicleEditForm(null, true);
            vehicleEditDialog.setLocationRelativeTo(dialog);
            vehicleEditDialog.setVisible(true);

        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(dialog, sre.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void delete(JTable tblVehicles, JDialog dialog) {
        try {
            int[] selectedRows = validateSelection(tblVehicles);
            int answer;
            String confirmMessage = "";

            if (selectedRows.length == 1) {
                String brand = (String) tblVehicles.getValueAt(selectedRows[0], 0);
                String model = (String) tblVehicles.getValueAt(selectedRows[0], 1);
                answer = option("Da li ste sigurni da zelite da obrisete vozilo " + brand.toUpperCase() + " " + model.toUpperCase(), dialog);
                confirmMessage = "Vozilo je uspesno obrisano!";
            } else {
                answer = option("Da li ste sigurni da zelite da obrisete selektovane redove?", dialog);
                confirmMessage = "Selektovana vozila su uspesno obrisana!";
            }

            if (answer == JOptionPane.YES_OPTION) {
                List<Vehicle> checkList = new ArrayList<>();
                if (selectedRows.length == 1) {
                    String vin = (String) tblVehicles.getValueAt(selectedRows[0], 2);
                    Vehicle v = new Vehicle();
                    v.setViNumber(vin);
                    checkList.add(v);
                    checkBeforeDelete(checkList);
                    getResponse(Operation.VEHICLE_DELETE_BY_VIN, v);
                } else {

                    List<Vehicle> vehicles = new ArrayList<>();
                    for (int row : selectedRows) {
                        Vehicle v = new Vehicle();
                        v.setViNumber((String) tblVehicles.getValueAt(row, 2));
                        vehicles.add(v);
                    }
                    getResponse(Operation.VEHICLE_DELETE_MULTIPLE_BY_VIN, vehicles);

                }

                JOptionPane.showMessageDialog(dialog, confirmMessage);

            }

        } catch (SelectRowException | InputValidationException val) {
            JOptionPane.showMessageDialog(dialog, val.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void checkBeforeDelete(List<Vehicle> vehicleList) throws Exception {
        List<PurchaseOrder> purchaseOrders = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();

        for (PurchaseOrder purchaseOrder : purchaseOrders) {

            for (Vehicle v : vehicleList) {
                if (v.getViNumber().equals(purchaseOrder.getVehicle().getViNumber())) {
                    throw new InputValidationException("Vozila koja su prodata nije moguce obrisati iz baze!");
                }

            }
        }
    }

    public static void addToPurchaseOrder(JTable tblVehicles, JDialog dialog) {

        try {

            int[] selectedRows = validateSelection(tblVehicles);

            if (selectedRows.length > 1) {
                throw new SelectRowException("Dodavanje nije omoguceno, selektovano je vise od jednog reda!");
            }

            String vin = (String) tblVehicles.getValueAt(selectedRows[0], 2);
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
                dialog.dispose();
                return;
            }

            JDialog purchaseOrderDialog = new PurchaseOrderCreateForm(null, true);
            purchaseOrderDialog.setLocationRelativeTo(dialog);
            purchaseOrderDialog.setVisible(true);

        } catch (SelectRowException | InputValidationException val) {
            JOptionPane.showMessageDialog(dialog, val.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static int option(String message, JDialog dialog) {
        String[] options = {"Da", "Ne"};
        return JOptionPane.showOptionDialog(dialog, message, "Paznja!",
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

    private static int[] validateSelection(JTable tblCities) throws Exception {
        int[] selectedRows = tblCities.getSelectedRows();

        if (selectedRows.length == 0) {
            throw new SelectRowException("Niste selektovali red u tabeli!");
        }
        return selectedRows;
    }

    public static void search(JTable tblVehicles, JComboBox comboBrand, JComboBox comboModel, JComboBox comboBodyType,
    JComboBox comboYearFrom, JComboBox comboYearTo, JComboBox comboFuelType, JComboBox comboCity, JComboBox comboSortList, JDialog dialog) {
        try {
            List<JComboBox> combos = Arrays.asList(comboBrand, comboModel, comboBodyType, comboFuelType, comboYearFrom, comboYearTo, comboCity);
            boolean selected = checkComboIndex(combos);

            if (!selected && comboSortList.getSelectedIndex() == 0) {
                fillTable(tblVehicles, dialog);
                return;
            } else if (!selected && comboSortList.getSelectedIndex() > 0) {
                fillTable(tblVehicles, dialog);
                sortMainList(tblVehicles, comboSortList);
                return;
            }

            List<Vehicle> allVehicles = (List<Vehicle>) getResponse(Operation.VEHICLE_GET_ALL, null).getResult();

            Criterium criterium = new Criterium(allVehicles, comboBrand, comboModel, comboBodyType, comboYearFrom, comboYearTo, comboFuelType, comboCity);
            mainVehicleList = criterium.getFilteredVehicles();
            
            if (comboSortList.getSelectedIndex() > 0) {
                sortMainList(tblVehicles, comboSortList);
            } else {
                tblVehicles.setModel(new VehicleTableModel(mainVehicleList));
            }

            renderImage(tblVehicles);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Doslo je do greske prilikom pretrage!!", "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void infoAdrea(JTextArea txtAreaBusinessUnitInfo, JTable tblVehicles, StringBuilder stringBuilder, JDialog dialog) {
        try {
            if (tblVehicles.getSelectedRow() != -1) {
                stringBuilder.delete(0, stringBuilder.length());
                int selectedRow = tblVehicles.getSelectedRow();
                String vin = (String) tblVehicles.getValueAt(selectedRow, 2);

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
                txtAreaBusinessUnitInfo.setText(stringBuilder.append("\t======").append(bu.getName()).append("======").append("\n")
                .append("Maticni broj: ").append(bu.getCompanyRegId()).append("\n")
                .append("PIB: ").append(bu.getTaxId()).append("\n")
                .append("Adresa: ").append(bu.getAddress()).append(", ").append(c.getName()).append("\n")
                .append("Kontakt Telefon: ").append(bu.getPhone()).append("\n")
                .append("Email: ").append(bu.getEmail()).toString());
            } else {
                stringBuilder.delete(0, stringBuilder.length());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void fillTable(JTable tblVehicles, JDialog dialog) {
        try {
            List<Vehicle> allVehicles = (List<Vehicle>) getResponse(Operation.VEHICLE_GET_ALL, null).getResult();

            mainVehicleList = allVehicles.stream().sorted(Comparator.comparing(vehicle -> vehicle.getModel().getName())).collect(Collectors.toList());
            tblVehicles.setModel(new VehicleTableModel(mainVehicleList));
            renderImage(tblVehicles);
            MyTableCustomComponents.setTblHeader(tblVehicles);
            MyTableCustomComponents.centerCellText(tblVehicles);

        } catch (SocketException soe) {
            JOptionPane.showMessageDialog(dialog, soe.getMessage(), "Upozorenje!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void fillCombo(JComboBox comboBrand, JComboBox comboBodyType, JComboBox comboFuelType, JComboBox comboCity,
    JComboBox comboYearFrom, JComboBox comboYearTo, JDialog dialog) {

        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            List<City> cities = (List<City>) getResponse(Operation.CITY_GET_ALL, null).getResult();

            brands.stream().sorted(Comparator.comparing(Brand::getBrandName)).forEach(comboBrand::addItem);
            Arrays.asList(CarBodyType.values()).forEach(comboBodyType::addItem);
            Arrays.asList(FuelType.values()).forEach(comboFuelType::addItem);
            cities.stream().sorted(Comparator.comparing(City::getName)).forEach(comboCity::addItem);

            LocalDate ld = LocalDate.now();
            int currentYear = ld.getYear();
            List<Integer> years = new ArrayList<>();
            for (int i = currentYear; i >= (currentYear - 10); i--) {
                years.add(i);
            }

            years.forEach(comboYearFrom::addItem);
            years.forEach(comboYearTo::addItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void updateComboModel(JComboBox comboBrand, JComboBox comboModel, JDialog dialog) {
        try {
            comboModel.removeAllItems();

            if (comboBrand.getSelectedIndex() == 0) {
                comboModel.addItem("Model");
            } else {
                Brand brand = (Brand) comboBrand.getSelectedItem();
                comboModel.addItem("Svi modeli");
                List<Model> allModels = (List<Model>) getResponse(Operation.MODEL_GET_ALL, null).getResult();
                allModels.stream().sorted(Comparator.comparing(Model::getName))
                .filter(model -> model.getBrand().equals(brand)).forEach(comboModel::addItem);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void sortMainList(JTable tblVehicles, JComboBox comboSortList) {
        SortList sortList = new SortList(mainVehicleList, comboSortList);
        tblVehicles.setModel(new VehicleTableModel(sortList.getSortedVehicles()));
        renderImage(tblVehicles);
    }

    public static void loadPurchaseOrders(JDialog dialog) {
        try {
            List<PurchaseOrder> po = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();
            VehicleTableModel.setPurchaseOrders(po);
        }catch (SocketException soe) {
            JOptionPane.showMessageDialog(dialog, soe.getMessage(),"Warning", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        catch (Exception ex) {
            Logger.getLogger(VehicleSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void renderImage(JTable tblVehicles) {

        TableCellRenderer tblCellRenderer = (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) -> {
            return (Component) value;
        };
        
        tblVehicles.getColumn("*").setCellRenderer(tblCellRenderer);
        tblVehicles.getColumnModel().getColumn(9).setMaxWidth(34);
    }

    private static boolean checkComboIndex(List<JComboBox> combos) {
        for (JComboBox c : combos) {
            if (c.getSelectedIndex() > 0) {
                return true;
            }
        }
        return false;
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
