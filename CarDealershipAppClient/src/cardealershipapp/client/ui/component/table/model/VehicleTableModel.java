package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.ui.component.table.VehicleTableImageLoader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class VehicleTableModel extends AbstractTableModel {

    private final List<Vehicle> vehicles;
    private static List<PurchaseOrder> purchaseOrders;

    public VehicleTableModel(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public int getRowCount() {
        if (vehicles == null) {
            return 0;
        }

        return vehicles.size();
    }

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vehicle vehicle = vehicles.get(rowIndex);
       
        switch (columnIndex) {
            case 0: return vehicle.getModel().getBrand().getBrandName();
            case 1: return vehicle.getModel().getName();
            case 2: return vehicle.getViNumber();
            case 3: return vehicle.getBodyType();
            case 4:
                if (vehicle.getFuelType().equals(FuelType.ELEKTRICNIPOGON)) {
                    return "Baterija: " + vehicle.getEngineDisplacement() + "kWh";
                }
                return vehicle.getEngineDisplacement();
            case 5: return vehicle.getEnginePower() + "/" + Math.round(vehicle.getEnginePower() * 1.34) + " kW/KS";
            case 6: return vehicle.getYearOfProd();
            case 7: return vehicle.getFuelType();
            case 8: return vehicle.getPrice() + " " + vehicle.getCurrency();
            case 9: return getImage(vehicle.getId());
        }

        return "N/A";
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Marka";
            case 1: return "Model";
            case 2: return "Broj sasije";
            case 3: return "Karoserija";
            case 4: return "Zapremina";
            case 5: return "Snaga motora";
            case 6: return "Godina";
            case 7: return "Vrsta goriva";
            case 8: return "Cena";
            case 9: return "*";
            
        }
        return "n/a";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 9) {
            return JLabel.class;
        }
        return Object.class;
    }

    public static void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        try {
            VehicleTableModel.purchaseOrders = purchaseOrders;
        } catch (Exception ex) {
            Logger.getLogger(VehicleTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JLabel getImage(Long id) {
        return VehicleTableImageLoader.getInstance().getImages(id, purchaseOrders);
    }

    
    
}
