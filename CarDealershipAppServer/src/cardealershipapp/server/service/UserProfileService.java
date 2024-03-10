package cardealershipapp.server.service;

import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.server.exception.ServiceException;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface UserProfileService {
    
    UserProfile logIn(String email, String password) throws ServiceException;
}
