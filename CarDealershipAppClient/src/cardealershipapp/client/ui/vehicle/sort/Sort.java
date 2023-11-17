package cardealershipapp.client.ui.vehicle.sort;

import cardealershipapp.common.domain.Vehicle;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface Sort {
    
    List<Vehicle> sortedVehicleList(SortList sortList) throws Exception;
    
}
