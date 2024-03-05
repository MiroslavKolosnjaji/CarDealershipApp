package cardealershipapp.client.util.configuration;

import cardealershipapp.client.ui.vehicle.filter.Filter;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * @author Miroslav Kološnjaji
 */
public class FilterConfigurationUtils {

    private final static FilterConfigurationUtils INSTANCE = new FilterConfigurationUtils();
    private final Properties PROPERTIES = new Properties();

    private FilterConfigurationUtils() {
        try {
            PROPERTIES.load(new FileInputStream("CarDealershipAppClient/filterConfiguration.properties"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static FilterConfigurationUtils getInstance() {
        return INSTANCE;
    }


    public Filter selectFilterProperty(String filter) throws Exception {
        String className = PROPERTIES.getProperty(filter);
        Class<?> c = Class.forName(className);
        Constructor<?> constructor = c.getConstructor();
        return (Filter) constructor.newInstance();
    }


}
