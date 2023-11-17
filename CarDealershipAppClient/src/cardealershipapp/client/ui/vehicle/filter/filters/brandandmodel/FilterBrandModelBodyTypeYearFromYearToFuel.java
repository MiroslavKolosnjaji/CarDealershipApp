package cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel;

import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.client.ui.vehicle.filter.Criterium;
import java.util.List;
import cardealershipapp.client.ui.vehicle.filter.Filter;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterBrandModelBodyTypeYearFromYearToFuel implements Filter{

    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception {
       return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getModel().getBrand().equals(criterium.getComboBrand().getSelectedItem()) &&
                                                vehicle.getModel().equals(criterium.getComboModel().getSelectedItem())  &&
                                                vehicle.getBodyType().equals(criterium.getComboBodyType().getSelectedItem()) &&
                                               (vehicle.getYearOfProd() >= (Integer) criterium.getComboYearFrom().getSelectedItem() &&
                                                vehicle.getYearOfProd() <= (Integer) criterium.getComboYearTo().getSelectedItem()) &&
                                                vehicle.getFuelType().equals(criterium.getComboFuelType().getSelectedItem()))
                                        .collect(Collectors.toList());
    }
 
}
