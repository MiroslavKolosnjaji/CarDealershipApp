package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.BusinessUnit;
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
public class BusinessUnitServiceImpl implements ServiceCRUD<BusinessUnit, Long> {

    private static final Logger log = LoggerFactory.getLogger(BusinessUnitServiceImpl.class);
    private final Repository<BusinessUnit, Long> businessUnitRepository;

    public BusinessUnitServiceImpl(Repository<BusinessUnit, Long> businessUnitRepository) {
        this.businessUnitRepository = businessUnitRepository;
    }

    @Override
    public void save(BusinessUnit businessUnit) throws ServiceException {
        try {

            String query = "SELECT `Name`, CompanyRegNum, TaxId, Address, Phone, Email, CityId FROM business_unit";
            List<BusinessUnit> businessUnits = businessUnitRepository.findByQuery(query);
            if (businessUnits.isEmpty()) {
                businessUnitRepository.save(businessUnit);
                DataBase.getInstance().confirmTransaction();
            } else {
                throw new InputValidationException("Poslovna jedinica vec postoji u sistemu!");
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
    public void update(BusinessUnit businessUnit) throws ServiceException {
        try {

            businessUnitRepository.update(businessUnit);
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
    public void delete(BusinessUnit businessUnit) throws ServiceException {
        try {

            businessUnitRepository.delete(businessUnit);
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
    public void deleteMultiple(List<BusinessUnit> listItems) throws ServiceException {
        //TODO Implement this method if necessary
        throw new UnsupportedOperationException(ExceptionUtils.UNSUPPORTED_OPERATION_MESSAGE);
    }

    @Override
    public List<BusinessUnit> getAll() throws ServiceException {
        try {
            List<BusinessUnit> businessUnits = businessUnitRepository.getAll();
            DataBase.getInstance().confirmTransaction();
            return businessUnits;

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
    public BusinessUnit findById(Long id) throws ServiceException {
        try {

            BusinessUnit businessUnit = businessUnitRepository.findById(id);
            DataBase.getInstance().confirmTransaction();
            return businessUnit;

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
