package cardealershipapp.server.service;

import cardealershipapp.common.domain.UserProfile;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface UserProfileService {
    
    UserProfile logIn(String email, String password) throws Exception;
}
