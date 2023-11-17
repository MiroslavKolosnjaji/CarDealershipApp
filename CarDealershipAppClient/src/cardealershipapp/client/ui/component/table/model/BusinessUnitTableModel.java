package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.BusinessUnit;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class BusinessUnitTableModel extends AbstractTableModel {

    private List<BusinessUnit> businessUnits;

    public BusinessUnitTableModel(List<BusinessUnit> businessUnits) {
        this.businessUnits = businessUnits;
    }

    @Override
    public int getRowCount() {
        if (businessUnits == null) {
            return 0;
        }

        return businessUnits.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BusinessUnit businessUnit = businessUnits.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return businessUnit.getId();
            case 1:
                return businessUnit.getName();
            case 2:
                return businessUnit.getCompanyRegId();
            case 3:
                return businessUnit.getTaxId();
            case 4:
                return businessUnit.getAddress();
            case 5:
                return businessUnit.getPhone();
            case 6:
                return businessUnit.getEmail();
            case 7:
                return businessUnit.getCity().getName();
            default:
                break;
        }

        return "N/A";
    }

    @Override
    public String getColumnName(int column) {

        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Naziv";
            case 2:
                return "Maticni broj";
            case 3:
                return "PIB";
            case 4:
                return "Adresa";
            case 5:
                return "Kontakt telefon";
            case 6:
                return "Email";
            case 7:
                return "Grad";
            default:
                break;
        }

        return "N/A";
    }

}
