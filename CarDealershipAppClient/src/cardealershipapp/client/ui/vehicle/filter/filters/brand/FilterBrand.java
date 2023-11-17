package cardealershipapp.client.ui.vehicle.filter.filters.brand;

import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.ui.vehicle.filter.Criterium;
import java.util.List;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterBrand implements Filter {

    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
        return criterium.getAllVehicles().stream()
                                         .filter(vehicle -> vehicle.getModel().getBrand().equals(criterium.getComboBrand().getSelectedItem()))
                                         .collect(Collectors.toList());

    }

}
