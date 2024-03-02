package cardealershipapp.client.ui.vehicle.filter;

import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyType;
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyTypeFuelType;
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyTypeYearFrom;
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyTypeYearTo;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.*;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.*;
import cardealershipapp.client.ui.vehicle.filter.filters.city.FilterCity;
import cardealershipapp.client.ui.vehicle.filter.filters.fueltype.FilterFuelType;
import cardealershipapp.client.ui.vehicle.filter.filters.fueltype.FilterFuelTypeCity;
import cardealershipapp.client.ui.vehicle.filter.filters.notselected.DefaultList;
import cardealershipapp.client.ui.vehicle.filter.filters.yearfrom.FilterYearFrom;
import cardealershipapp.client.ui.vehicle.filter.filters.yearfrom.FilterYearFromCity;
import cardealershipapp.client.ui.vehicle.filter.filters.yearfrom.FilterYearFromFuelType;
import cardealershipapp.client.ui.vehicle.filter.filters.yearfrom.FilterYearFromYearTo;
import cardealershipapp.client.ui.vehicle.filter.filters.yearto.FilterYearTo;
import cardealershipapp.client.ui.vehicle.filter.filters.yearto.FilterYearToCity;
import cardealershipapp.client.ui.vehicle.filter.filters.yearto.FilterYearToFuelType;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * @author Miroslav Kološnjaji
 */
public class FilterMapInitializer {

    public static Map<Key, Filter> getFilterMap() {

        Map<Key, Filter> stateMap = new HashMap<>();

        Queue<Key> keyQueue = FilterKeyFactory.prepareAndGetKeys();

        stateMap.put(keyQueue.poll(), new DefaultList());
        stateMap.put(keyQueue.poll(), new FilterBrand());
        stateMap.put(keyQueue.poll(), new FilterBrandModel());
        stateMap.put(keyQueue.poll(), new FilterBrandBodyType());
        stateMap.put(keyQueue.poll(), new FilterBrandYearFrom());
        stateMap.put(keyQueue.poll(), new FilterBrandYearTo());
        stateMap.put(keyQueue.poll(), new FilterBrandFuelType());
        stateMap.put(keyQueue.poll(), new FilterBrandCity());
        stateMap.put(keyQueue.poll(), new FilterBrandFuelTypeCity());
        stateMap.put(keyQueue.poll(), new FilterBrandModelBodyType());
        stateMap.put(keyQueue.poll(), new FilterBrandModelFuelType());
        stateMap.put(keyQueue.poll(), new FilterBrandModelCity());
        stateMap.put(keyQueue.poll(), new FilterBrandModelFuelTypeCity());
        stateMap.put(keyQueue.poll(), new FilterBrandModelBodyTypeYearFrom());
        stateMap.put(keyQueue.poll(), new FilterBrandModelBodyTypeYearFromYearTo());
        stateMap.put(keyQueue.poll(), new FilterBrandModelBodyTypeYearFromYearToFuel());
        stateMap.put(keyQueue.poll(), new FilterBrandModelBodyTypeYearFromYearToFuelAndCity());
        stateMap.put(keyQueue.poll(), new FilterBrandModelBodyTypeYearFromYearToFuelAndCity());
        stateMap.put(keyQueue.poll(), new FilterBodyType());
        stateMap.put(keyQueue.poll(), new FilterBodyTypeYearFrom());
        stateMap.put(keyQueue.poll(), new FilterBodyTypeYearTo());
        stateMap.put(keyQueue.poll(), new FilterBodyTypeFuelType());
        stateMap.put(keyQueue.poll(), new FilterYearFrom());
        stateMap.put(keyQueue.poll(), new FilterYearFromYearTo());
        stateMap.put(keyQueue.poll(), new FilterYearFromFuelType());
        stateMap.put(keyQueue.poll(), new FilterYearFromCity());
        stateMap.put(keyQueue.poll(), new FilterYearTo());
        stateMap.put(keyQueue.poll(), new FilterYearToFuelType());
        stateMap.put(keyQueue.poll(), new FilterYearToCity());
        stateMap.put(keyQueue.poll(), new FilterFuelType());
        stateMap.put(keyQueue.poll(), new FilterFuelTypeCity());
        stateMap.put(keyQueue.poll(), new FilterCity());

        return stateMap;
    }
}
