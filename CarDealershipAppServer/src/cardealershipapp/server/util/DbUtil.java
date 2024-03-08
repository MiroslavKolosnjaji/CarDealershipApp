package cardealershipapp.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class DbUtil{
    
    private static final DbUtil instance = new DbUtil();
    private Properties properties;

    private DbUtil() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("CarDealershipAppServer/db.properties"));
        } catch (IOException ex) {
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public static DbUtil getInstance(){
        return instance;
    }
    
    public String getUrl(){
        String currentDb = properties.getProperty("current_db");
        return properties.getProperty(currentDb + "_" + "url");
    }
    
    public String getUser(){
        String currentDb = properties.getProperty("current_db");
        return properties.getProperty(currentDb + "_" + "user");
    }
    
    public String getPassword(){
        String currentDb = properties.getProperty("current_db");
        return properties.getProperty(currentDb + "_" + "password" );
    }
}
