package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.ExtendedRepository;
import cardealershipapp.server.repository.query.SqlQueries;
import cardealershipapp.server.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class CustomerRepositoryImpl implements ExtendedRepository<Customer, Long> {

    public static final Logger log = LoggerFactory.getLogger(CityRepositoryImpl.class);
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(Customer customer) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(customer.getName(),
                    customer.getCompanyName(),
                    customer.getAddress(),
                    customer.getPhone(),
                    customer.getEmail()));
            db.executeSqlUpdate(SqlQueries.Customers.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa kupca '" + customer.getName() + "' u bazu podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom dodavanja kupca u bazu!");
        }
    }

    @Override
    public Customer saveAndReturn(Customer customer) throws RepositoryException {
        try {
            paramsQueue.addAll(List.of(customer.getName(),
                    customer.getCompanyName(),
                    customer.getAddress(),
                    customer.getPhone(),
                    customer.getEmail()));

            Long id = db.executeSqlUpdateAndGenerateKey(SqlQueries.Customers.INSERT, paramsQueue);
            customer.setId(id);
            return customer;

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa kupca '" + customer.getName() + "' u bazu podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom dodavanja kupca u bazu!");
        }
    }

    @Override
    public void update(Customer customer) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(customer.getName(),
                    customer.getCompanyName(),
                    customer.getAddress(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getId()));
            db.executeSqlUpdate(SqlQueries.Customers.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom ažuriranja kupca '" + customer.getName() + "' u bazi podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka kupca!");
        }
    }

    @Override
    public void delete(Customer customer) throws RepositoryException {
        try {

            paramsQueue.add(customer.getId());
            db.executeSqlUpdate(SqlQueries.Customers.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja kupca '" + customer.getName() + "' iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja kupca iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<Customer> customers) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(customers, SqlQueries.Customers.DELETE_MULTIPLE_ID);
            customers.forEach(customer -> paramsQueue.add(customer.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja  '" + customers.size() + "' kupaca iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja vise kupaca iz baze!");
        }

    }


    @Override
    public List<Customer> getAll() throws RepositoryException {
        try {

            List<Customer> customers = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.Customers.SELECT_ALL);

            while (rs.next()) {
                Long customerId = rs.getLong("Id");
                String name = rs.getString("Name");
                String companyName = rs.getString("CompanyName");
                String address = rs.getString("Address");
                String phone = rs.getString("Phone");
                String email = rs.getString("Email");

                Customer customer = new Customer(customerId, name, companyName, address, phone, email);
                customers.add(customer);
            }

            rs.close();
            statement.close();
            return customers;

        } catch (SQLException sqle) {
            log.error("Greška prilikom učitavanja kupaca iz baze podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja kupaca iz baze!");
        }
    }

    @Override
    public Customer findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(SqlQueries.Customers.SELECT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("Id"));
                customer.setName(rs.getString("Name"));
                customer.setCompanyName(rs.getString("CompanyName"));
                customer.setAddress(rs.getString("Address"));
                customer.setPhone(rs.getString("Phone"));
                customer.setEmail(rs.getString("Email"));

                rs.close();
                preparedStatement.close();
                return customer;
            }

            throw new EntityNotFoundException("Kupac ne postoji!");

        } catch (SQLException sqle) {
            log.error("Greška prilikom pretraživanja kupca po ID '" + id + "'  u bazi podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrage kupca po ID broju!");
        }
    }

    @Override
    public List<Customer> findByQuery(String query) throws RepositoryException {
        try {

            List<Customer> customers = new ArrayList<>();
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("Id"));
                customer.setName(rs.getString("Name"));
                customer.setCompanyName(rs.getString("CompanyName"));
                customer.setAddress(rs.getString("Address"));
                customer.setPhone(rs.getString("Phone"));
                customer.setEmail(rs.getString("Email"));

                customers.add(customer);
            }

            rs.close();
            statement.close();
            return customers;

        } catch (SQLException sqle) {
            log.error(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + query + " u metodi findByQeury klase: " +this.getClass().getSimpleName()+ " : " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja kupca po upitu!");
        }
    }

}
