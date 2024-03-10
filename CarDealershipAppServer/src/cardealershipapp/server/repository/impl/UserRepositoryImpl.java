package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Gender;
import cardealershipapp.common.domain.User;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.query.SqlQueries;
import cardealershipapp.server.util.ExceptionUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class UserRepositoryImpl implements Repository<User, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(User user) throws RepositoryException {
        try {
            paramsQueue.addAll(List.of(user.getFirstName(),
                    user.getLastName(),
                    user.getDateOfBirth(),
                    user.getGender().toString(),
                    user.getResidence().getId()));
            db.executeSqlUpdate(SqlQueries.Users.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom dodavanja korisnika u bazu!");
        }
    }

    @Override
    public void update(User user) throws RepositoryException {

        try {

            paramsQueue.addAll(List.of(user.getFirstName(),
                    user.getLastName(),
                    user.getDateOfBirth(),
                    user.getGender().toString(),
                    user.getResidence().getId(),
                    user.getId()));
            db.executeSqlUpdate(SqlQueries.Users.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka korisnika u bazi!");
        }

    }

    @Override
    public void delete(User user) throws RepositoryException {
        try {

            paramsQueue.add(user.getId());
            db.executeSqlUpdate(SqlQueries.Users.DELETE_BY_ID, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja korisnika iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<User> users) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(users, SqlQueries.Users.DELETE_MULTIPLE_ID);
            users.forEach(user -> paramsQueue.add(user.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom brisanja vise korisnika iz baze!");
        }

    }


    @Override
    public List<User> getAll() throws RepositoryException {

        try {
            List<User> users = new ArrayList<>();
            PreparedStatement prepStat = db.getConnection().prepareStatement(SqlQueries.Users.SELECT_ALL);
            ResultSet rs = prepStat.executeQuery();

            while (rs.next()) {

                Long userId = rs.getLong("U.id");
                String firstName = rs.getString("U.FirstName");
                String lastName = rs.getString("U.LastName");
                LocalDate dateOfBirth = rs.getDate("U.DateOfBirth").toLocalDate();
                Gender gender = Gender.valueOf(rs.getString("U.Gender"));

                Long cityId = rs.getLong("U.id");
                Integer zipCode = rs.getInt("C.ZipCode");
                String cityName = rs.getString("C.CityName");

                City city = new City(cityId, zipCode, cityName);
                User user = new User(userId, firstName, lastName, dateOfBirth, gender, city);

                users.add(user);

            }

            rs.close();
            prepStat.close();
            return users;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja korisnika iz baze!");
        }

    }

    @Override
    public User findById(Long id) throws RepositoryException, EntityNotFoundException {
        try {
            PreparedStatement prepStat = db.getConnection().prepareStatement(SqlQueries.Users.SELECT_BY_ID);
            prepStat.setLong(1, id);

            ResultSet rs = prepStat.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(id);
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
                user.setGender(Gender.valueOf(rs.getString("Gender")));
                user.setResidence(new City(rs.getLong("CityId")));

                rs.close();
                prepStat.close();
                return user;
            }

            throw new EntityNotFoundException("Korisnik sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja korisnika po ID broju!");
        }
    }

    @Override
    public List<User> findByQuery(String query) throws RepositoryException {
        //TODO Implement this method if necessary
        throw new UnsupportedOperationException(ExceptionUtils.UNSUPPORTED_OPERATION_MESSAGE); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
