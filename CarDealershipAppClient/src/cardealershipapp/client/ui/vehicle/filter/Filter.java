package cardealershipapp.client.ui.vehicle.filter;

import cardealershipapp.common.domain.Vehicle;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface Filter {
    List<Vehicle> search(Criterium criterium) throws Exception;
}
