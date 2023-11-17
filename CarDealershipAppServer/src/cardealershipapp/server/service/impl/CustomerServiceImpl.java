package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Customer;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.CustomerService;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class CustomerServiceImpl implements CustomerService{
    
   private final Repository<Customer, Long> customerRepository;

    public CustomerServiceImpl(Repository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void add(Customer customer) throws Exception {
        customerRepository.add(customer);
    }

    @Override
    public void update(Customer customer) throws Exception {
        customerRepository.update(customer);
    }

    @Override
    public void delete(Customer customer) throws Exception {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteMultiple(List<Customer> customers) throws Exception {
        customerRepository.deleteMultiple(customers);
    }

    @Override
    public List<Customer> getAll() throws Exception {
        return customerRepository.getAll();
    }

    @Override
    public Customer findById(Long id) throws Exception {
        return customerRepository.findById(id);
    }
   
   
    
}
