package cardealershipapp.server.service;

import cardealershipapp.common.domain.Model;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface ModelService {
    
    void add(Model model) throws Exception;

    void update(Model model) throws Exception;

    void delete(Model model) throws Exception;
    
    void deleteMultiple(List<Model> models) throws Exception;

    List<Model> getAll() throws Exception;

    Model findById(Long id) throws Exception;
}
