package cardealershipapp.client.ui.vehicle.filter;

import cardealershipapp.common.domain.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Criterium {

    private final JComboBox comboBrand;
    private final JComboBox comboModel;
    private final JComboBox comboBodyType;
    private final JComboBox comboYearFrom;
    private final JComboBox comboYearTo;
    private final JComboBox comboFuelType;
    private final JComboBox comboCity;
    private Filter filter;
    private final List<Vehicle> allVehicles;
    private List<Vehicle> filteredVehicles;

    public Criterium(List<Vehicle> allVehicles, JComboBox comboBrand, JComboBox comboModel, JComboBox comboBodyType, JComboBox comboYearFrom, JComboBox comboYearTo, JComboBox comboFuelType, JComboBox comboCity) {
        this.allVehicles = allVehicles;
        this.comboBrand = comboBrand;
        this.comboModel = comboModel;
        this.comboBodyType = comboBodyType;
        this.comboYearFrom = comboYearFrom;
        this.comboYearTo = comboYearTo;
        this.comboFuelType = comboFuelType;
        this.comboCity = comboCity;
        filteredVehicles = new ArrayList<>();
        try {
            filter = FilterFactoryMethod.getInstance().filterList(comboBrand, comboModel, comboBodyType, comboYearFrom, comboYearTo, comboFuelType, comboCity);
        } catch (Exception ex) {
            Logger.getLogger(Criterium.class.getName()).log(Level.SEVERE, null, ex);
        }
        setFilterSettings();
    }

    private void setFilterSettings() {
        try {
            if (filter == null) {
                filteredVehicles = allVehicles;
                JOptionPane.showMessageDialog(null, "Criteria not supported yet!", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            filteredVehicles = filter.search(this);

        } catch (Exception ex) {
            Logger.getLogger(Criterium.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Vehicle> getAllVehicles() {
        return allVehicles;
    }

    public JComboBox getComboBrand() {
        return comboBrand;
    }

    public JComboBox getComboModel() {
        return comboModel;
    }

    public JComboBox getComboBodyType() {
        return comboBodyType;
    }

    public JComboBox getComboYearFrom() {
        return comboYearFrom;
    }

    public JComboBox getComboYearTo() {
        return comboYearTo;
    }

    public JComboBox getComboFuelType() {
        return comboFuelType;
    }

    public JComboBox getComboCity() {
        return comboCity;
    }

    public List<Vehicle> getFilteredVehicles() {
        return filteredVehicles;
    }

}
