package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Gender;
import cardealershipapp.common.domain.User;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.repository.Repository;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class UserRepositoryImpl implements Repository<User, Long> {

    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void add(User user) throws Exception {
        try {
            String query = "INSERT INTO `user`(FirstName, LastName, DateOfBirth, Gender, CityId) VALUES(?,?,?,?,?)";
            paramsQueue.addAll(List.of(user.getFirstName(),
                    user.getLastName(),
                    user.getDateOfBirth(),
                    user.getGender().toString(),
                    user.getResidence().getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom dodavanja korisnika u bazu!\n" + dbe.getMessage());
        }
    }

    @Override
    public void update(User user) throws Exception {

        try {
            String query = "UPDATE `user` SET FirstName = ?, LastName = ?, DateOfBirth = ?, Gender = ?, CityId = ? WHERE Id = ?";
            paramsQueue.addAll(List.of(user.getFirstName(),
                    user.getLastName(),
                    user.getDateOfBirth(),
                    user.getGender().toString(),
                    user.getResidence().getId(),
                    user.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka korisnika u bazi!\n" + dbe.getMessage());
        }

    }

    @Override
    public void delete(User user) throws Exception {
        try {
            String query = "DELETE FROM `user` WHERE id= ?";
            paramsQueue.add(user.getId());
            db.executeSqlUpdate(query, paramsQueue);
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja korisnika iz baze!\n" + dbe.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<User> users) throws Exception {
        try {

            String query = db.generateDeleteMultiQuery(users,"user");
            users.forEach(user -> paramsQueue.add(user.getId()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }



    @Override
    public List<User> getAll() throws Exception {

        try {
            List<User> users = new ArrayList<>();
            String query = "SELECT U.Id, U.FirstName, U.LastName, U.DateOfBirth, U.Gender, U.CityId, C.ZipCode ,C.CityName FROM `user` U JOIN city C ON U.CityId = C.id";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
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
            db.confirmTransaction();
            return users;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja korisnika iz baze!\n" + sqle.getMessage());
        }

    }

    @Override
    public User findById(Long id) throws Exception {
        try {
            String query = "SELECT FirstName, LastName, DateOfBirth, Gender, CityId FROM user WHERE Id = ?";
            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
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
                db.confirmTransaction();
                return user;
            }

            throw new Exception("Korisnik sa ovim Id brojem ne postoji!");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom pretrazivanja korisnika po id-u!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<User> findByQuery(String query) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
