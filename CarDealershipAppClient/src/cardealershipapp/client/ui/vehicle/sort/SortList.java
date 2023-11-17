package cardealershipapp.client.ui.vehicle.sort;

import cardealershipapp.common.domain.Vehicle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class SortList {

    private final Sort sort;
    private List<Vehicle> sortedVehicles;

    public SortList(List<Vehicle> sortedVehicles, JComboBox comboSortList) {
        this.sortedVehicles = sortedVehicles;
        sort = SortFactory.sorting(comboSortList);
        sortBySelectedOption();
    }

    private void sortBySelectedOption() {
        try {
            sortedVehicles = sort.sortedVehicleList(this);
        } catch (Exception ex) {
            Logger.getLogger(SortList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Vehicle> getSortedVehicles() {
        return sortedVehicles;
    }
}
