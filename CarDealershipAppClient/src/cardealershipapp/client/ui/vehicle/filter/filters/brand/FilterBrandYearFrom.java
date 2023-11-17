package cardealershipapp.client.ui.vehicle.filter.filters.brand;


import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.ui.vehicle.filter.Criterium;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterBrandYearFrom implements Filter {

    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
       return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getModel().getBrand().equals(criterium.getComboBrand().getSelectedItem()) &&
                                                vehicle.getYearOfProd() >= (Integer) criterium.getComboYearFrom().getSelectedItem())
                                        .collect(Collectors.toList());
    }

  
}
