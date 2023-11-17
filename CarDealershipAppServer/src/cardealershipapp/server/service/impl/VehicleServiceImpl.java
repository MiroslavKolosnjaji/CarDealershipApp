package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.VehicleService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class VehicleServiceImpl implements VehicleService {

    private Repository vehicleRepository;

    public VehicleServiceImpl(Repository vehicleRepository) {
        try {
            this.vehicleRepository = vehicleRepository;
        } catch (Exception ex) {
            Logger.getLogger(VehicleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void add(Vehicle vehicle) throws Exception {
        vehicleRepository.add(vehicle);
    }

    @Override
    public void update(Vehicle vehicle) throws Exception {
        vehicleRepository.update(vehicle);
    }

    @Override
    public void delete(Vehicle vehicle) throws Exception {
            vehicleRepository.delete(vehicle);
    }
    
    @Override
    public void deleteByVin(Vehicle vehicle) throws Exception {
        String query = "SELECT Id, ViNumber, BodyType, EngDIspl, EngPowerKW, YearOfProd, FuelType, Price, Currency, ModelId, BusinessUId FROM vehicle WHERE ViNumber = '" + vehicle.getViNumber() + "'";
        List<Vehicle> vehicles = vehicleRepository.findByQuery(query);
        if (vehicles != null) {
            delete(vehicles.get(0));
        }
    }

    @Override
    public void deleteMultipleByVin(List<Vehicle> vehicles) throws Exception {
       String query = generateQueryByListSize(vehicles);
       List<Vehicle> vehiclesForDelete = vehicleRepository.findByQuery(query);
       vehicleRepository.deleteMultiple(vehiclesForDelete);
    }

    @Override
    public List<Vehicle> getAll() throws Exception {
        return vehicleRepository.getAll();
    }

    @Override
    public Vehicle findById(Long id) throws Exception {
        return (Vehicle) vehicleRepository.findById(id);
    }
    
    private String generateQueryByListSize(List<Vehicle> vehicles) {
        StringBuffer bufferedQuery = new StringBuffer("SELECT Id, ViNumber, BodyType, EngDIspl, EngPowerKW, YearOfProd, FuelType, Price, Currency, ModelId, BusinessUId FROM vehicle WHERE ViNumber IN(");

        for (int i = 0; i < vehicles.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("'").append(vehicles.get(i).getViNumber()).append("'");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
    }
}
