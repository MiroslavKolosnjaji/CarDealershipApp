package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.User;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.UserService;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class UserServiceImpl implements UserService{

    private final Repository userRepository;

    public UserServiceImpl(Repository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    
    @Override
    public void add(User user) throws Exception {
        userRepository.add(user);
    }

    @Override
    public void update(User user) throws Exception {
       userRepository.update(user);
    }

    @Override
    public void delete(User user) throws Exception {
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() throws Exception {
       return userRepository.getAll();
    }

    @Override
    public User findById(Long id) throws Exception {
       return (User) userRepository.findById(id);
    }
    
}
