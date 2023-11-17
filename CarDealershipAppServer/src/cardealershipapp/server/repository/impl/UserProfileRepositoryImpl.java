package cardealershipapp.server.repository.impl;

import java.sql.*;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Gender;
import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.server.repository.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class UserProfileRepositoryImpl implements Repository<UserProfile, String> {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void add(UserProfile userProfile) throws Exception {

        try {
            String query = "INSERT INTO user_profile(Email, Password, UserId) VALUES(?,?,?)";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setString(1, userProfile.getEmail());
            prepStat.setString(2, userProfile.getPassword());
            prepStat.setObject(3, userProfile.getUser());

            prepStat.executeUpdate();

            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom dodavanja novog profila u bazu!\n" + sqle.getMessage());

        }

    }

    @Override
    public void update(UserProfile userProfile) throws Exception {
        try {
            String query = "UPDATE user_profile SET Email = ?, Password = ?, UserId = ? WHERE Id = ?";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setString(1, userProfile.getEmail());
            prepStat.setString(2, userProfile.getPassword());
            prepStat.setObject(3, userProfile.getUser());
            prepStat.setString(4, userProfile.getEmail());

            prepStat.executeUpdate();

            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka profila u bazi!\n" + sqle.getMessage());

        }
    }

    @Override
    public void delete(UserProfile userProfile) throws Exception {
        try {
            String query = "DELETE FROM user_profile WHERE id = ?";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setString(1, userProfile.getEmail());

            prepStat.executeUpdate();

            prepStat.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Doslo je do greske prilikom brisanja profila iz baze!\n" + sqle.getMessage());
        }
    }
    
     @Override
    public void deleteMultiple(List<UserProfile> userProfiles) throws Exception {
        try {

            String query = generateDeleteMultiQuery(userProfiles);
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            
            int counter = 1;
            for (UserProfile userProfile : userProfiles) {
                preparedStatement.setString(counter++, userProfile.getEmail());
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

    private String generateDeleteMultiQuery(List<UserProfile> userProfiles) {
        StringBuffer bufferedQuery = new StringBuffer("DELETE FROM user_profile WHERE Id IN(");

        for (int i = 0; i < userProfiles.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
    }

    @Override
    public List<UserProfile> getAll() throws Exception {
        try {

            List<UserProfile> userProfiles = new ArrayList<>();

            String query = "UP.Email, UP.Password, UP.UserId, U.FirstName, U.LastName, U.DateOfBirth, U.Gender, U.CityId, C.ZipCode, C.CityName FROM user_profile UP JOIN `user` U ON UP.UserId = U.Id JOIN city C ON U.CityId = C.id";

            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            ResultSet rs = prepStat.executeQuery();

            while (rs.next()) {

                String emailAddress = rs.getString("UP.Email");
                String password = rs.getString("UP.Password");

                Long userId = rs.getLong("UP.UserId");
                String userFirstName = rs.getString("U.FirstName");
                String userLastName = rs.getString("U.LastName");
                LocalDate userBdDate = rs.getDate("U.DateOfBirth").toLocalDate();
                Gender gender = Gender.valueOf(rs.getString("U.Gender"));
                Long cityId = rs.getLong("U.CityId");
                Integer zipCode = rs.getInt("C.ZipCode");
                String cityName = rs.getString("C.CityName");

                City city = new City(cityId, zipCode, cityName);
                User user = new User(userId, userFirstName, userLastName, userBdDate, gender, city);
                UserProfile userProfile = new UserProfile(emailAddress, password, user);

                userProfiles.add(userProfile);

            }

            rs.close();
            prepStat.close();
            db.confirmTransaction();
            return userProfiles;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Dogodila se greska prilikom ucitavanja liste profila iz baze!\n" + sqle.getMessage());

        }
    }

    @Override
    public UserProfile findById(String id) throws Exception {
        try {
            String query = "SELECT Email, Password, UserId FROM user_profile WHERE Id = ?";
            PreparedStatement prepStat = db.getConnection().prepareStatement(query);
            prepStat.setString(1, id);
            ResultSet rs = prepStat.executeQuery();

            if (rs.next()) {
                UserProfile userProfile = new UserProfile();
                userProfile.setEmail(rs.getString("Email"));
                userProfile.setPassword(rs.getString("Password"));

                rs.close();
                prepStat.close();
                db.confirmTransaction();
                return userProfile;
            } else {
                throw new Exception("Korisnik sa ovom email adresom ne postoji!");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Dogodila se greska prilikom pretrazivanja korisnika po email adresi!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<UserProfile> findByQuery(String query) throws Exception {
        try {
            List<UserProfile> userProfiles = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String email = rs.getString("Email");
                User user = new User(rs.getLong("UserId"));

                UserProfile u = new UserProfile();
                u.setEmail(email);
                u.setUser(user);
                
                userProfiles.add(u);
            }
            rs.close();
            statement.close();
            db.confirmTransaction();

            return userProfiles;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            db.cancelTransaction();
            throw new Exception("Dogodila se greska prilikom provere da li korisnik postoji u bazi!\n" + sqle.getMessage());
        }
    }

}
