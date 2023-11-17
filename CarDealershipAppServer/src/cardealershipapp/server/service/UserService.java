package cardealershipapp.server.service;

import cardealershipapp.common.domain.User;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface UserService {
    
    void add(User user) throws Exception;

    void update(User user) throws Exception;

    void delete(User user) throws Exception;

    List<User> getAll() throws Exception;

    User findById(Long id) throws Exception;
    
}
