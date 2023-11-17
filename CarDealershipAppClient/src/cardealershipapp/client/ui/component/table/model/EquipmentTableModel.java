package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.Equipment;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class EquipmentTableModel extends AbstractTableModel {

    private List<Equipment> equipments;

    public EquipmentTableModel(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    @Override
    public int getRowCount() {
        if (equipments == null) {
            return 0;
        }
        return equipments.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Equipment equipment = equipments.get(rowIndex);

        switch (columnIndex) {
            case 0: return equipment.getName();
            case 1: return equipment.getPrice() + " " + equipment.getCurrency().toString();
        }
        return "N/A";
    }

    @Override
    public String getColumnName(int column) {
         switch (column) {
            case 0: return "Naziv";
            case 1: return "Cena";
        }
        return "N/A";
    }
    
    

}
