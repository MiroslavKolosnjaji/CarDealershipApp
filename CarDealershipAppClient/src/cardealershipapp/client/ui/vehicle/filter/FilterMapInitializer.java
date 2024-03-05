package cardealershipapp.client.ui.vehicle.filter;

import cardealershipapp.client.util.configuration.FilterConfigurationUtils;

import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class FilterMapInitializer {


    public static Map<Key, Filter> getFilterMap() {
        try {
            Map<Key, Filter> stateMap = new HashMap<>();

            Queue<Key> keyQueue = FilterKeyFactory.prepareAndGetKeys();

            // Filter classes are loaded by index because of simplicity. Loading order follows keyQueue order.
            int index = 1;
            while (!keyQueue.isEmpty()) {
                stateMap.put(keyQueue.poll(), FilterConfigurationUtils.getInstance().selectFilterProperty(String.valueOf(index++)));
            }

            return stateMap;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
