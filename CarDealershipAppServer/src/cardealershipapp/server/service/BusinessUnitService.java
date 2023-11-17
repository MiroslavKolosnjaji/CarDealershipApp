package cardealershipapp.server.service;

import cardealershipapp.common.domain.BusinessUnit;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface BusinessUnitService {
    
    void add(BusinessUnit businessUnit) throws Exception;

    void update(BusinessUnit businessUnit) throws Exception;

    void delete(BusinessUnit businessUnit) throws Exception;

    List<BusinessUnit> getAll() throws Exception;

    BusinessUnit findById(Long id) throws Exception;
    
}
