package cardealershipapp.server.service;

import cardealershipapp.common.domain.Vehicle;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface VehicleService {
    
    void add(Vehicle vehicle) throws Exception;

    void update(Vehicle vehicle) throws Exception;

    void delete(Vehicle vehicle) throws Exception;
    
    void deleteByVin(Vehicle vehicle) throws Exception;
    
    void deleteMultipleByVin(List<Vehicle> vehicles) throws Exception;

    List<Vehicle> getAll() throws Exception;

    Vehicle findById(Long id) throws Exception;
    
}
