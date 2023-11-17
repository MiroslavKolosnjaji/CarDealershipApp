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
public class FilterFuelTypeCity implements Filter{
    
    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
        return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getFuelType().equals(criterium.getComboFuelType().getSelectedItem()) &&
                                                vehicle.getBusinessUnit().getCity().equals(criterium.getComboCity().getSelectedItem()))                                                
                                        .collect(Collectors.toList());
    }
    
}
