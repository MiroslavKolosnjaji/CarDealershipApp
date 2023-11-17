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
public class FilterBodyTypeYearTo implements Filter{

    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getBodyType().equals(criterium.getComboBodyType().getSelectedItem()) &&
                                                vehicle.getYearOfProd() <= (Integer) criterium.getComboYearTo().getSelectedItem())                                                
                                        .collect(Collectors.toList());    }
    
}
