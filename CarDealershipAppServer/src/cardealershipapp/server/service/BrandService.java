package cardealershipapp.server.service;

import cardealershipapp.common.domain.Brand;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface BrandService {
    
    void add(Brand brand) throws Exception;

    void update(Brand brand) throws Exception;

    void delete(Brand brand) throws Exception;

    List<Brand> getAll() throws Exception;

    Brand findById(Long id) throws Exception;
}
