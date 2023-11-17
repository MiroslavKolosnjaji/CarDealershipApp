package cardealershipapp.client.ui.vehicle.sort.sortby;

import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.ui.vehicle.sort.Sort;
import cardealershipapp.client.ui.vehicle.sort.SortList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class SortByYearIncreasing implements Sort{

    @Override
    public List<Vehicle> sortedVehicleList(SortList sortList) throws Exception {
        return sortList.getSortedVehicles().stream()
                                           .sorted(Comparator.comparing(Vehicle::getYearOfProd))
                                           .collect(Collectors.toList());
    }

    
}
