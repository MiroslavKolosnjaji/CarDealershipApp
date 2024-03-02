package cardealershipapp.client.ui.vehicle.filter;

import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrandYearTo;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrandCity;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelBodyTypeYearFrom;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelFuelType;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelCity;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrandFuelTypeCity;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrandBodyType;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelBodyTypeYearFromYearTo;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrand;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelBodyTypeYearFromYearToFuelAndCity;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelBodyType;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrandYearFrom;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelFuelTypeCity;
import cardealershipapp.client.ui.vehicle.filter.filters.brandandmodel.FilterBrandModelBodyTypeYearFromYearToFuel;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrandModel;
import cardealershipapp.client.ui.vehicle.filter.filters.brand.FilterBrandFuelType;
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyType;
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyTypeFuelType;
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyTypeYearFrom;
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyTypeYearTo;
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

import javax.swing.*;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
public class FilterFactoryMethod {

    private static final FilterFactoryMethod INSTANCE = new FilterFactoryMethod();
    private final Map<Key, Filter> stateMap = new HashMap<>();

    private FilterFactoryMethod() {
        prepareKeys();
    }


    public static FilterFactoryMethod getInstance() {
        return INSTANCE;
    }


    public Filter filterList(JComboBox comboBrand, JComboBox comboModel, JComboBox comboBodyType,
                             JComboBox comboYearFrom, JComboBox comboYearTo, JComboBox comboFuelType, JComboBox comboCity) throws Exception {
        Filter filter = null;
        final int BRAND = comboBrand.getSelectedIndex();
        final int MODEL = comboModel.getSelectedIndex();
        final int BODYTYPE = comboBodyType.getSelectedIndex();
        final int YEARFROM = comboYearFrom.getSelectedIndex();
        final int YEARTO = comboYearTo.getSelectedIndex();
        final int FUELTYPE = comboFuelType.getSelectedIndex();
        final int CITY = comboCity.getSelectedIndex();


        Integer[] comboIndexes = new Integer[]{BRAND, MODEL, BODYTYPE, YEARFROM, YEARTO, FUELTYPE, CITY};


        filter = stateMap.get(generatekey(comboIndexes));


        return filter;
    }


    private void prepareKeys() {
        /**
         * There are all combobox indexes from integer array, and we will use that indexes to get specific filter state for vehicle list
         *  TODO -- 1
         *  TODO -- 1,2
         *  TODO -- 1,3
         *  TODO -- 1,4
         *  TODO -- 1,5
         *  TODO -- 1,6
         *  TODO -- 1,7
         *  TODO -- 1,6,7
         *  TODO -- 1,2,3
         *  TODO -- 1,2,6
         *  TODO -- 1,2,7
         *  TODO -- 1,2,6,7
         *  TODO -- 1,2,3,4
         *  TODO -- 1,2,3,4,5
         *  TODO -- 1,2,3,4,5,6
         *  TODO -- 1,2,3,4,5,6,7
         *  TODO -- 3
         *  TODO -- 3,4
         *  TODO -- 3,5
         *  TODO -- 3,6
         *  TODO -- 3,7
         *  TODO -- 4
         *  TODO -- 4,5
         *  TODO -- 4,6
         *  TODO -- 4,7
         *  TODO -- 5
         *  TODO -- 5,6
         *  TODO -- 5,7
         *  TODO -- 6
         *  TODO -- 6,7
         *  TODO -- 7
         */

        Queue<Key> keyQueue = new ArrayDeque<>();

        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 3, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{1, 0, 0, 0, 0, 6, 7}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{1, 2, 0, 0, 0, 6, 7}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 5, 6, 0}));
        keyQueue.add(new Key(new Integer[]{1, 2, 3, 4, 5, 6, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 3, 0, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 0, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 4, 0, 0, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 5, 0, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 5, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 5, 0, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 6, 0}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 6, 7}));
        keyQueue.add(new Key(new Integer[]{0, 0, 0, 0, 0, 0, 7}));

        populateMap(keyQueue);
    }

    private void populateMap(Queue<Key> keyQueue) {

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
    }

    private Key generatekey(Integer[] comboIndexes) {
        Integer[] generatedKey = new Integer[7];

        for (int i = 0; i < comboIndexes.length; i++) {
            if (comboIndexes[i] > 0)
                generatedKey[i] = i + 1;
            else
                generatedKey[i] = 0;

        }
        return new Key(generatedKey);
    }


}
