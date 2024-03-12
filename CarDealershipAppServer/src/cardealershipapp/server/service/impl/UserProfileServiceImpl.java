package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.util.ExceptionUtils;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


/**
 * @author Miroslav Kološnjaji
 */
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);
    private final Repository<UserProfile, String> userProfileRepository;
    private final Repository<User, Long> userRepository;

    public UserProfileServiceImpl(Repository userProfileRepository, Repository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserProfile logIn(String email, String password) throws ServiceException {

        try {
            String query = "SELECT Email, UserId FROM user_profile WHERE Email = '" + email + "' AND `Password` = '" + password + "'";
            List<UserProfile> userProfiles = userProfileRepository.findByQuery(query);

            if (userProfiles.isEmpty()) {
                throw new InputValidationException("Korisnik ne postoji u sistemu!");
            }

            UserProfile userProfile = userProfiles.get(0);
            User user = userRepository.findById(userProfile.getUser().getId());

            if (user == null) {
                throw new InputValidationException("Ne mogu da povezem korisnika sa ovim profilom!");
            }

            userProfile.setUser(user);
            DataBase.getInstance().confirmTransaction();

            return userProfiles.get(0);

        } catch (EntityNotFoundException | InputValidationException e) {
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
