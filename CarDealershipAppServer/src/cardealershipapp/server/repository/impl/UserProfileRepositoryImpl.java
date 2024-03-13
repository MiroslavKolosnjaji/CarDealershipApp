package cardealershipapp.server.repository.impl;

import java.sql.*;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.City;
import cardealershipapp.common.domain.Gender;
import cardealershipapp.common.domain.User;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.repository.query.SqlQueries;
import cardealershipapp.server.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class UserProfileRepositoryImpl implements Repository<UserProfile, String> {

    public static final Logger log = LoggerFactory.getLogger(UserProfileRepositoryImpl.class);
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void save(UserProfile userProfile) throws RepositoryException {

        try {

            paramsQueue.addAll(List.of(userProfile.getEmail(),
                    userProfile.getPassword(),
                    userProfile.getUser()));
            db.executeSqlUpdate(SqlQueries.UserProfiles.INSERT, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa profila u bazu podataka za korisnika '" + userProfile.getUser().getFirstName().concat(" ").concat(userProfile.getUser().getLastName())  + "': " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom dodavanja novog profila u bazu!");

        }

    }

    @Override
    public void update(UserProfile userProfile) throws RepositoryException {
        try {

            paramsQueue.addAll(List.of(userProfile.getEmail(),
                    userProfile.getPassword(),
                    userProfile.getUser(),
                    userProfile.getEmail()));
            db.executeSqlUpdate(SqlQueries.UserProfiles.UPDATE, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom ažuriranja profila u bazi podataka za korisnika '" + userProfile.getUser().getFirstName().concat(" ").concat(userProfile.getUser().getLastName())  + "': " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka profila u bazi!");

        }
    }

    @Override
    public void delete(UserProfile userProfile) throws RepositoryException {
        try {

            paramsQueue.add(userProfile.getEmail());
            db.executeSqlUpdate(SqlQueries.UserProfiles.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja profila korisnika '" + userProfile.getUser().getFirstName().concat(" ").concat(userProfile.getUser().getLastName())  + "' u bazi podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja profila iz baze!");
        }
    }

    @Override
    public void deleteMultiple(List<UserProfile> userProfiles) throws RepositoryException {
        try {

            String query = db.generateDeleteMultiQuery(userProfiles, SqlQueries.UserProfiles.DELETE_MULTIPLE_ID);
            userProfiles.forEach(userProfile -> paramsQueue.add(userProfile.getEmail()));
            db.executeSqlUpdate(query, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja'" + userProfiles.size() + "' korisničkih profila u bazi podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja brendova iz baze!");
        }

    }


    @Override
    public List<UserProfile> getAll() throws RepositoryException {
        try {

            List<UserProfile> userProfiles = new ArrayList<>();
            PreparedStatement prepStat = db.getConnection().prepareStatement(SqlQueries.UserProfiles.SELECT_ALL);
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
            return userProfiles;

        } catch (SQLException sqle) {
            log.error("Greška prilikom učitavanja korisničkih profila iz baze podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Dogodila se greska prilikom ucitavanja liste profila iz baze!");

        }
    }

    @Override
    public UserProfile findById(String id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement prepStat = db.getConnection().prepareStatement(SqlQueries.UserProfiles.SELECT_BY_ID);
            prepStat.setString(1, id);
            ResultSet rs = prepStat.executeQuery();

            if (rs.next()) {
                UserProfile userProfile = new UserProfile();
                userProfile.setEmail(rs.getString("Email"));
                userProfile.setPassword(rs.getString("Password"));

                rs.close();
                prepStat.close();
                return userProfile;
            }

            throw new EntityNotFoundException("Korisnik sa ovom email adresom ne postoji!");


        } catch (SQLException sqle) {
            log.error("Greška prilikom pretraživanja korisničkog profila po ID '" + id + "'u bazi podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Dogodila se greska prilikom pretrazivanja korisnika po email adresi!");
        }
    }

    @Override
    public List<UserProfile> findByQuery(String query) throws RepositoryException {
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
            return userProfiles;

        } catch (SQLException sqle) {
            log.error(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + query + " u metodi findByQeury klase: " +this.getClass().getSimpleName()+ " : " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Dogodila se greska prilikom pretrage korisnika po upitu!");
        }
    }

}
