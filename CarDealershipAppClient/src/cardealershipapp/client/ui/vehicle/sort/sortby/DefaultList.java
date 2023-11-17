package cardealershipapp.client.ui.vehicle.sort.sortby;

import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.ui.vehicle.sort.Sort;
import cardealershipapp.client.ui.vehicle.sort.SortList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class DefaultList implements Sort{

    @Override
    public List<Vehicle> sortedVehicleList(SortList sortList) throws Exception {
        return sortList.getSortedVehicles();
    }
    
}
