package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.User;
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
public class UserServiceImpl implements ServiceCRUD<User, Long> {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final Repository<User, Long> userRepository;

    public UserServiceImpl(Repository<User, Long> userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void save(User user) throws ServiceException {
        try {

            userRepository.save(user);
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
    public void update(User user) throws ServiceException {
        try {

            userRepository.update(user);
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
    public void delete(User user) throws ServiceException {
        try {

            userRepository.delete(user);
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
    public void deleteMultiple(List<User> listItems) throws ServiceException {
        //TODO Implement this method if necessary
        throw new UnsupportedOperationException(ExceptionUtils.UNSUPPORTED_OPERATION_MESSAGE);
    }

    @Override
    public List<User> getAll() throws ServiceException {
        try {

            List<User> users = userRepository.getAll();
            DataBase.getInstance().confirmTransaction();
            return users;

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
    public User findById(Long id) throws ServiceException {
        try {
            User user = userRepository.findById(id);
            DataBase.getInstance().confirmTransaction();
            return user;

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
