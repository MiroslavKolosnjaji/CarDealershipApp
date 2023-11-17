package cardealershipapp.client.ui.vehicle.filter.filters.yearto;

import cardealershipapp.client.ui.vehicle.filter.Criterium;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import cardealershipapp.common.domain.Vehicle;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterYearTo implements Filter {

   @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
        return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getYearOfProd() <= (Integer)criterium.getComboYearTo().getSelectedItem())                                                
                                        .collect(Collectors.toList());
    }
    
}
