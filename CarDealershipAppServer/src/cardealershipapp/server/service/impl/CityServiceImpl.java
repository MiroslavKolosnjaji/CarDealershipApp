package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.City;
import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.util.ExceptionUtils;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.ServiceCRUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author Miroslav Kološnjaji
 */
public class CityServiceImpl implements ServiceCRUD<City, Long> {

    private static final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);
    private final Repository<City, Long> cityRepository;

    public CityServiceImpl(Repository<City, Long> cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void save(City city) throws ServiceException {
        try {
            String query = "SELECT ZipCode, CityName FROM city WHERE ZipCode = '" + city.getZipCode() + "' AND CityName = '" + city.getName() + "'";
            List<City> cities = cityRepository.findByQuery(query);

            if (cities.isEmpty()) {
                cityRepository.save(city);
                DataBase.getInstance().confirmTransaction();
            } else {
                throw new InputValidationException("Grad vec postoji u sistemu!");
            }

        } catch (InputValidationException e) {
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

    @Override
    public void update(City city) throws ServiceException {
        try {

            cityRepository.update(city);
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
    public void delete(City city) throws ServiceException {
        try {

            cityRepository.delete(city);
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
    public void deleteMultiple(List<City> listItems) throws ServiceException {
        //TODO Implement this method if necessary
        throw new UnsupportedOperationException(ExceptionUtils.UNSUPPORTED_OPERATION_MESSAGE);
    }

    @Override
    public List<City> getAll() throws ServiceException {
        try {

            List<City> cities = cityRepository.getAll();
            DataBase.getInstance().confirmTransaction();
            return cities;

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
    public City findById(Long id) throws ServiceException {

        try {

            City city = cityRepository.findById(id);
            DataBase.getInstance().confirmTransaction();
            return city;

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

}
