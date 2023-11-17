package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.City;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class CityTableModel extends AbstractTableModel {

    private final List<City> cities;

    public CityTableModel(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public int getRowCount() {
        if (cities.isEmpty()) {
            return 0;
        }
        return cities.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        City city = cities.get(rowIndex);
        switch (columnIndex) {
            case 0: return city.getId();
            case 1: return city.getZipCode();
            case 2: return city.getName();
        }
        return "N/A";
    }

    @Override
    public String getColumnName(int column) {
         switch (column) {
            case 0: return "ID";
            case 1: return "PTT";
            case 2: return "GRAD";
        }
        
        return "N/A";
    }
    
    

}
