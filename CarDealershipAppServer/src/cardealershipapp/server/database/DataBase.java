package cardealershipapp.server.database;

import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.util.DbUtil;
import cardealershipapp.server.util.ExceptionUtils;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.List;
import java.util.Queue;

/**
 * @author Miroslav Kološnjaji
 */
public class DataBase implements DatabaseOperations {

    private static final DataBase INSTANCE = new DataBase();
    private Connection connection = null;

    private DataBase() {
    }

    public static DataBase getInstance() {
        return INSTANCE;
    }

    public void connectToDb() throws Exception {
        String url = DbUtil.getInstance().getUrl();
        String user = DbUtil.getInstance().getUser();
        String password = DbUtil.getInstance().getPassword();
        try {
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new Exception("An error occurred while connecting to the database\n" + sqle.getMessage());
        }
    }

    public void disconnectFromDb() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("An error occurred while disconnecting from the database\n" + sqle.getMessage());
        }
    }

    public void confirmTransaction() throws DatabaseException {
        try {
            connection.commit();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("An error occured while confirming transaction\n" + sqle.getMessage());
        }
    }

    public void cancelTransaction() throws DatabaseException {
        try {
            connection.rollback();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("An error occured while canceling transaction\n" + sqle.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }


    /**
     * @param query
     * @param params
     * @throws SQLException
     */
    @Override
    public void executeSqlUpdate(String query, Queue<Object> params) throws DatabaseException {
        try(PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {

            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + e.getMessage());

        }
    }

    @Override
    public Long executeSqlUpdateAndGenerateKey(String query, Queue<Object> params) throws DatabaseException {
        try(PreparedStatement preparedStatement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if(rs.next())
                return rs.getLong(1);
            else
                throw new DatabaseException("Nije pronadjen generisani kljuc nakon izvrsenja SQL upita: " + query);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + e.getMessage());
        }
    }

    /**
     * @param query
     * @param params
     * @throws SQLException
     */
    @Override
    public void executeSqlQuery(String query, Queue<Object> params) throws DatabaseException {

    }

    public <T> String generateDeleteMultiQuery(List<T> list, String query) {
        StringBuffer bufferedQuery = new StringBuffer(query);

        bufferedQuery.append("(");

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                bufferedQuery.append(",");
            }
            bufferedQuery.append("?");
        }
        bufferedQuery.append(")");

        return bufferedQuery.toString();
    }


}
