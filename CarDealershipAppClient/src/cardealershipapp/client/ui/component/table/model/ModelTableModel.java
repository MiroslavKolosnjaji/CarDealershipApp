package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.Model;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ModelTableModel extends AbstractTableModel {

    private final List<Model> models;

    public ModelTableModel(List<Model> models) {
        this.models = models;
    }

    @Override
    public int getRowCount() {
        if (models == null) {
            return 0;
        }
        return models.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Model model = models.get(rowIndex);

        switch (columnIndex) {
            case 0: return model.getId();
            case 1: return model.getName();
            case 2: return model.getBrand().getBrandName();
        }
        return "N/A";
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID";
            case 1: return "Model";
            case 2: return "Marka";
        }
        return "N/A";
    }

}
