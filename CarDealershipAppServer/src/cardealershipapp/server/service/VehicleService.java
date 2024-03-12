package cardealershipapp.server.service;

import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.common.exception.ServiceException;

import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface VehicleService extends ServiceCRUD<Vehicle, Long> {
    void deleteByVin(Vehicle vehicle) throws ServiceException;
    void deleteMultipleByVin(List<Vehicle> vehicles) throws ServiceException;
    
}
