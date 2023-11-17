package cardealershipapp.client.ui.vehicle.sort;

import cardealershipapp.client.ui.vehicle.sort.sortby.DefaultList;
import cardealershipapp.client.ui.vehicle.sort.sortby.SortByYearIncreasing;
import cardealershipapp.client.ui.vehicle.sort.sortby.SortByYearDecreasing;
import cardealershipapp.client.ui.vehicle.sort.sortby.SortByPriceDecreasing;
import cardealershipapp.client.ui.vehicle.sort.sortby.SortByPriceIncreasing;
import javax.swing.JComboBox;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class SortFactory {

    public static Sort sorting(JComboBox comboSortList) {
        Sort sort = null;
        int index = comboSortList.getSelectedIndex();

        switch (index) {
            case 0 -> sort = new DefaultList();
            case 1 -> sort = new SortByPriceDecreasing();
            case 2 -> sort = new SortByPriceIncreasing();
            case 3 -> sort = new SortByYearDecreasing();
            case 4 -> sort = new SortByYearIncreasing();
            default -> {}
        }
        return sort;
    }
}
