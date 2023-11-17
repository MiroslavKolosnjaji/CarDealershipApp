package cardealershipapp.server.service;

import cardealershipapp.common.domain.Customer;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface CustomerService {
    
    void add(Customer customer) throws Exception;

    void update(Customer customer) throws Exception;

    void delete(Customer customer) throws Exception;
    
    void deleteMultiple(List<Customer> customers) throws Exception;

    List<Customer> getAll() throws Exception;

    Customer findById(Long id) throws Exception;
    
}
