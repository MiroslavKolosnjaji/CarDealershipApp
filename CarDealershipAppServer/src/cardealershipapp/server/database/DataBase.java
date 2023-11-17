package cardealershipapp.server.database;

import cardealershipapp.server.util.DbUtil;
import java.sql.*;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class DataBase {

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

    public void disconnectFromDb() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new Exception("An error occurred while disconnecting from the database\n" + sqle.getMessage());
        }
    }

    public void confirmTransaction() throws Exception {
        try {
            connection.commit();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new Exception("An error occured while confirming transaction\n" + sqle.getMessage());
        }
    }

    public void cancelTransaction() throws Exception {
        try {
            connection.rollback();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new Exception("An error occured while canceling transaction\n" + sqle.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
    
    

}
