package cardealershipapp.client.ui.vehicle.filter.filters.bodytype;

import cardealershipapp.client.ui.vehicle.filter.Criterium;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import cardealershipapp.common.domain.Vehicle;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterBodyTypeFuelType implements Filter {

    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
        return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getBodyType().equals(criterium.getComboBodyType().getSelectedItem()) &&
                                                vehicle.getFuelType().equals(criterium.getComboFuelType().getSelectedItem()))                                                
                                        .collect(Collectors.toList());
    }
    
}
