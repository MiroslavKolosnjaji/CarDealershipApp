package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.repository.Repository;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Miroslav Kološnjaji
 */
public class CustomerRepositoryImpl implements Repository<Customer, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void add(Customer customer) throws Exception {
        try {
            String query = "INSERT INTO customer(Name, CompanyName, Address, Phone, Email) VALUES(?,?,?,?,?)";
            paramsQueue.addAll(List.of(customer.getName(),
                    customer.getCompanyName(),
                    customer.getAddress(),
                    customer.getPhone(),
                    customer.getEmail()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja novog kupca u bazu!\n" + dbe.getMessage());
        }
    }

    @Override
    public void update(Customer customer) throws Exception {
        try {
            String query = "UPDATE customer SET Name = ?, CompanyName = ?, Address = ?, Phone = ?, Email = ? WHERE Id = ?";
            paramsQueue.addAll(List.of(customer.getName(),
                    customer.getCompanyName(),
                    customer.getAddress(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getId()));
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka kupca!\n" + dbe.getMessage());
        }
    }

    @Override
    public void delete(Customer customer) throws Exception {
        try {
            String query = "DELETE FROM customer WHERE Id = ?";
            paramsQueue.add(customer.getId());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja kupca iz baze!\n" + dbe.getMessage());
        }
    }

    @Override
    public void deleteMultiple(List<Customer> customers) throws Exception {
        try {

            String query = db.generateDeleteMultiQuery(customers, "customer");
            customers.forEach(customer -> paramsQueue.add(customer.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }


    @Override
    public List<Customer> getAll() throws Exception {
        try {

            List<Customer> customers = new ArrayList<>();

            String query = "SELECT Id, 'Name', CompanyName, Address, Phone, Email FROM customer";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

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
            db.confirmTransaction();
            return customers;

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom ucitavanja kupaca iz baze!\n" + sqle.getMessage());
        }
    }

    @Override
    public Customer findById(Long id) throws Exception {
        try {

            String query = "Select Id, Name, CompanyName, Address, Phone, Email FROM customer WHERE Id = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
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
                db.confirmTransaction();
                return customer;
            }

            throw new Exception("Kupac sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom pretrage kupca po id-u!n\n" + sqle.getMessage());
        }
    }

    @Override
    public List<Customer> findByQuery(String query) throws Exception {
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
            db.confirmTransaction();
            return customers;

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom pretrazivanja kupca!\n" + sqle.getMessage());
        }
    }

}
