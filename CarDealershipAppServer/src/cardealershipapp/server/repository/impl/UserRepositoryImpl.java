package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Gender;
import cardealershipapp.common.domain.User;
import cardealershipapp.server.repository.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class UserRepositoryImpl implements Repository<User, Long> {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void add(User user) throws Exception {
        try {
            String query = "INSERT INTO `user`(FirstName, LastName, DateOfBirth, Gender, CityId) VALUES(?,?,?,?,?)";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepStat.setString(1, user.getFirstName());
            prepStat.setString(2, user.getLastName());
            prepStat.setObject(3, user.getDateOfBirth());
            prepStat.setString(4, user.getGender().toString());
            prepStat.setObject(5, user.getResidence().getId());

            prepStat.executeUpdate();

            ResultSet rsId = prepStat.getGeneratedKeys();
            if (rsId.next()) {
                user.setId(rsId.getLong(1));
            }
            rsId.close();
            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom dodavanja korisnika u bazu!\n" + sqle.getMessage());
        }
    }

    @Override
    public void update(User user) throws Exception {

        try {
            String query = "UPDATE `user` SET FirstName = ?, LastName = ?, DateOfBirth = ?, Gender = ?, CityId = ? WHERE Id = ?";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setString(1, user.getFirstName());
            prepStat.setString(2, user.getLastName());
            prepStat.setObject(3, user.getDateOfBirth());
            prepStat.setString(4, user.getGender().toString());
            prepStat.setObject(5, user.getResidence().getId());
            prepStat.setLong(6, user.getId());

            prepStat.executeUpdate();

            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka korisnika u bazi!\n" + sqle.getMessage());
        }

    }

    @Override
    public void delete(User user) throws Exception {
        try {
            String query = "DELETE FROM `user` WHERE id= ?";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setLong(1, user.getId());
            prepStat.executeUpdate();

            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja korisnika iz baze!\n" + sqle.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<User> users) throws Exception {
        try {

            String query = generateDeleteMultiQuery(users);
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            
            int counter = 1;
            for (User user : users) {
                preparedStatement.setLong(counter++, user.getId());
            }
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            db.confirmTransaction();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }

    private String generateDeleteMultiQuery(List<User> users) {
        StringBuffer bufferedQuery = new StringBuffer("DELETE FROM user WHERE Id IN(");

        for (int i = 0; i < users.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
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
