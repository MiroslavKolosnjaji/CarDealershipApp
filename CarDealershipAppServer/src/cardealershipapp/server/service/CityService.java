package cardealershipapp.server.service;

import cardealershipapp.common.domain.City;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface CityService {
    
     void add(City city) throws Exception;

    void update(City city) throws Exception;

    void delete(City city) throws Exception;

    List<City> getAll() throws Exception;

    City findById(Long id) throws Exception;
}

