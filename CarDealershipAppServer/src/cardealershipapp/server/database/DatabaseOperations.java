package cardealershipapp.server.database;

import cardealershipapp.server.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Queue;

public interface DatabaseOperations{
    void executeSqlUpdate(String query, Queue<Object> params) throws DatabaseException;
    Long executeSqlUpdateAndGenerateKey(String query, Queue<Object> params) throws DatabaseException;
    void executeSqlQuery(String query, Queue<Object> params) throws DatabaseException;

    default void setParameters(PreparedStatement preparedStatement, Queue<Object> params) throws SQLException{

        int index = 1;

        while(!params.isEmpty()){
            preparedStatement.setObject(index++, params.poll());
        }

    }
}
