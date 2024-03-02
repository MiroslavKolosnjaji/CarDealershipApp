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
import cardealershipapp.client.ui.vehicle.filter.filters.bodytype.FilterBodyTypeCity;
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
import cardealershipapp.common.domain.Model;

import javax.swing.*;
import java.util.*;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class FilterFactoryMethod {

    public static Filter filterList(JComboBox comboBrand, JComboBox comboModel, JComboBox comboBodyType,
    JComboBox comboYearFrom, JComboBox comboYearTo, JComboBox comboFuelType, JComboBox comboCity) throws Exception {
        Filter filter = null;
        final int BRAND = comboBrand.getSelectedIndex();
        final int MODEL = comboModel.getSelectedIndex();
        final int BODYTYPE=  comboBodyType.getSelectedIndex();
        final int YEARFROM = comboYearFrom.getSelectedIndex();
        final int YEARTO = comboYearTo.getSelectedIndex();
        final int FUELTYPE = comboFuelType.getSelectedIndex();
        final int CITY = comboCity.getSelectedIndex();

        if(BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            return filter = new DefaultList();
        }
        

    //CRITERIUMS    
       if (BRAND > 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrand();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrandModel();
            
        } else if (BRAND > 0 && MODEL == 0 && BODYTYPE > 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrandBodyType();
            
        } else if (BRAND > 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM > 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrandYearFrom();
            
        } else if (BRAND > 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO > 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrandYearTo();
            
        } else if (BRAND > 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE > 0 && CITY == 0) {
            filter = new FilterBrandFuelType();
            
        } else if (BRAND > 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY > 0) {
            filter = new FilterBrandCity();
            
        } else if (BRAND > 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE > 0 && CITY > 0) {
            filter = new FilterBrandFuelTypeCity();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE > 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrandModelBodyType();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE > 0 && CITY == 0) {
            filter = new FilterBrandModelFuelType();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY > 0) {
            filter = new FilterBrandModelCity();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE > 0 && CITY > 0) {
            filter = new FilterBrandModelFuelTypeCity();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE > 0 && YEARFROM > 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrandModelBodyTypeYearFrom();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE > 0 && YEARFROM > 0 && YEARTO > 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBrandModelBodyTypeYearFromYearTo();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE > 0 && YEARFROM > 0 && YEARTO > 0 && FUELTYPE > 0 && CITY == 0) {
            filter = new FilterBrandModelBodyTypeYearFromYearToFuel();
            
        } else if (BRAND > 0 && MODEL > 0 && BODYTYPE > 0 && YEARFROM > 0 && YEARTO > 0 && FUELTYPE > 0 && CITY > 0) {
            filter = new FilterBrandModelBodyTypeYearFromYearToFuelAndCity();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE > 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBodyType();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE > 0 && YEARFROM > 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBodyTypeYearFrom();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE > 0 && YEARFROM == 0 && YEARTO > 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterBodyTypeYearTo();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE > 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE > 0 && CITY == 0) {
            filter = new FilterBodyTypeFuelType();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE > 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY > 0) {
            filter = new FilterBodyTypeCity();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM > 0 && YEARTO == 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterYearFrom();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM > 0 && YEARTO > 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterYearFromYearTo();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM > 0 && YEARTO == 0 && FUELTYPE > 0 && CITY == 0) {
            filter = new FilterYearFromFuelType();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM > 0 && YEARTO == 0 && FUELTYPE == 0 && CITY > 0) {
            filter = new FilterYearFromCity();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO > 0 && FUELTYPE == 0 && CITY == 0) {
            filter = new FilterYearTo();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO > 0 && FUELTYPE > 0 && CITY == 0) {
            filter = new FilterYearToFuelType();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO > 0 && FUELTYPE == 0 && CITY > 0) {
            filter = new FilterYearToCity();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE > 0 && CITY == 0) {
            filter = new FilterFuelType();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE > 0 && CITY > 0) {
            filter = new FilterFuelTypeCity();
            
        } else if (BRAND == 0 && MODEL == 0 && BODYTYPE == 0 && YEARFROM == 0 && YEARTO == 0 && FUELTYPE == 0 && CITY > 0) {
            filter = new FilterCity();
            
        }
          return filter;
    }

}
