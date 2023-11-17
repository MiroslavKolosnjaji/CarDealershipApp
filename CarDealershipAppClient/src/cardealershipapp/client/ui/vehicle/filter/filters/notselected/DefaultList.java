package cardealershipapp.client.ui.vehicle.filter.filters.notselected;

import cardealershipapp.client.ui.vehicle.filter.Criterium;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import cardealershipapp.common.domain.Vehicle;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class DefaultList implements Filter {

    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
        return criterium.getAllVehicles();
    }
    
}
