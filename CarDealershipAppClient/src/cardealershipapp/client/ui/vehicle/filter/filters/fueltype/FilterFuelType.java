package cardealershipapp.client.ui.vehicle.filter.filters.fueltype;

import cardealershipapp.client.ui.vehicle.filter.Criterium;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import cardealershipapp.common.domain.Vehicle;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterFuelType implements Filter{
    
    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
        return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getFuelType().equals(criterium.getComboFuelType().getSelectedItem()))                                                
                                        .collect(Collectors.toList());
    }
    
}
