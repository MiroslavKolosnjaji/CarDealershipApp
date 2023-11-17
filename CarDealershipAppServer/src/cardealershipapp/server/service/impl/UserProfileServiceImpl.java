package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.UserProfileService;
import java.util.List;


/**
 *
 * @author Miroslav Kološnjaji
 */
public class UserProfileServiceImpl implements UserProfileService {

    private final Repository<UserProfile, String> userProfileRepository;
    private final Repository<User, Long> userRepository;

    public UserProfileServiceImpl(Repository userProfileRepository, Repository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserProfile logIn(String email, String password) throws Exception {

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

        return userProfiles.get(0);
    }


}
