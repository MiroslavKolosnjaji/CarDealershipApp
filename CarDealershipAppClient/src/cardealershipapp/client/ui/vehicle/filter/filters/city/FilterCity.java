package cardealershipapp.client.ui.vehicle.filter.filters.city;

import cardealershipapp.client.ui.vehicle.filter.Criterium;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import cardealershipapp.common.domain.Vehicle;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterCity implements Filter{
    
    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
        return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getBusinessUnit().getCity().equals(criterium.getComboCity().getSelectedItem()))                                                
                                        .collect(Collectors.toList());
    }
    
}
