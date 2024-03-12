package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Brand;
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
public class BrandServiceImpl implements ServiceCRUD<Brand, Long> {

    private static final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);
    private final Repository<Brand, Long> brandRepository;

    public BrandServiceImpl(Repository<Brand, Long> brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public void save(Brand brand) throws ServiceException {
        try {
            String query = "SELECT * FROM brand WHERE BrandName = '" + brand.getBrandName() + "'";
            List<Brand> b = brandRepository.findByQuery(query);
            if (b.isEmpty()) {
                brandRepository.save(brand);
                DataBase.getInstance().confirmTransaction();
            } else {
                throw new InputValidationException("Marka vec postoji u sistemu!");
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
    public void update(Brand brand) throws ServiceException {
        try {
            brandRepository.update(brand);
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
    public void delete(Brand brand) throws ServiceException {
        try {
            brandRepository.delete(brand);
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
    public void deleteMultiple(List<Brand> listItems) throws ServiceException {
        //TODO Implement this method if necessary
        throw new UnsupportedOperationException(ExceptionUtils.UNSUPPORTED_OPERATION_MESSAGE);
    }

    @Override
    public List<Brand> getAll() throws ServiceException {
        try {

            List<Brand> brands = brandRepository.getAll();
            DataBase.getInstance().confirmTransaction();

            return brands;

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
    public Brand findById(Long id) throws ServiceException {
        try {
            Brand brand = brandRepository.findById(id);
            DataBase.getInstance().confirmTransaction();

            return brand;

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
