package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.server.exception.*;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.VehicleService;
import cardealershipapp.server.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author Miroslav Kološnjaji
 */
public class VehicleServiceImpl implements VehicleService {

    private static final Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);
    private Repository<Vehicle, Long> vehicleRepository;

    public VehicleServiceImpl(Repository<Vehicle, Long> vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void save(Vehicle vehicle) throws ServiceException {
        try {

            vehicleRepository.save(vehicle);
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Vehicle vehicle) throws ServiceException {
        try {

            vehicleRepository.update(vehicle);
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public void delete(Vehicle vehicle) throws ServiceException {
        try {

            vehicleRepository.delete(vehicle);
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteMultiple(List<Vehicle> listItems) throws ServiceException {
        //TODO Implement this method if necessary
        throw new UnsupportedOperationException(ExceptionUtils.UNSUPPORTED_OPERATION_MESSAGE);
    }

    @Override
    public void deleteByVin(Vehicle vehicle) throws ServiceException {
        try {
            String query = "SELECT Id, ViNumber, BodyType, EngDIspl, EngPowerKW, YearOfProd, FuelType, Price, Currency, ModelId, BusinessUId FROM vehicle WHERE ViNumber = '" + vehicle.getViNumber() + "'";
            List<Vehicle> vehicles = vehicleRepository.findByQuery(query);
            if (vehicles != null) {
                delete(vehicles.get(0));
            }
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteMultipleByVin(List<Vehicle> vehicles) throws ServiceException {
        try {
            String query = generateQueryByListSize(vehicles);
            List<Vehicle> vehiclesToDelete = vehicleRepository.findByQuery(query);
            vehicleRepository.deleteMultiple(vehiclesToDelete);
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }


    @Override
    public List<Vehicle> getAll() throws ServiceException {
        try {

            List<Vehicle> vehicles = vehicleRepository.getAll();
            DataBase.getInstance().confirmTransaction();
            return vehicles;

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public Vehicle findById(Long id) throws ServiceException {
        try {

            Vehicle vehicle = vehicleRepository.findById(id);
            DataBase.getInstance().confirmTransaction();
            return vehicle;

        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
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
