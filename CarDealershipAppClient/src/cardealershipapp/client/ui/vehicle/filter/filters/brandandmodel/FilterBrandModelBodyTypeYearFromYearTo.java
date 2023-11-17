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
public class FilterBrandModelBodyTypeYearFromYearTo implements Filter {

    @Override
    public List<Vehicle> search(Criterium criterium) throws Exception { 
       return criterium.getAllVehicles().stream()
                                        .filter(vehicle -> vehicle.getModel().getBrand().equals(criterium.getComboBrand().getSelectedItem()) &&
                                                vehicle.getModel().equals(criterium.getComboModel().getSelectedItem())  &&
                                                vehicle.getBodyType().equals(criterium.getComboBodyType().getSelectedItem()) &&
                                                (vehicle.getYearOfProd() >= criterium.getComboYearFrom().getSelectedIndex() &&
                                                vehicle.getYearOfProd() <= criterium.getComboYearTo().getSelectedIndex()))
                                        .collect(Collectors.toList());
    }


}
