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
    private final Map<Key, Filter> stateMap;

    private FilterFactoryMethod() {
        stateMap = FilterMapInitializer.getFilterMap();
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

        filter = stateMap.get(FilterKeyFactory.generatekey(comboIndexes));

        return filter;
    }


}
