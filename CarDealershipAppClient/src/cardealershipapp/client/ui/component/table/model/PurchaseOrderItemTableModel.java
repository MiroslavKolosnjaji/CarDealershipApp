package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.PurchaseOrderItem;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderItemTableModel extends AbstractTableModel {

    private final List<PurchaseOrderItem> purchaseOrderItems;

    public PurchaseOrderItemTableModel(List<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    @Override
    public int getRowCount() {
        if (purchaseOrderItems == null) {
            return 0;
        }
        return purchaseOrderItems.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PurchaseOrderItem purchaseOrderItem = purchaseOrderItems.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return purchaseOrderItem.getOrdinalNum();
            case 1: return purchaseOrderItem.getEquipment().getName();
            case 2: return purchaseOrderItem.getQuantity();
            case 3: return purchaseOrderItem.getEquipment().getPrice().multiply(BigDecimal.valueOf(purchaseOrderItem.getQuantity().longValue()));
            case 4: return  purchaseOrderItem.getEquipment().getCurrency().toString();
        }
        return "N/A";
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Redni broj";
            case 1: return "Naziv";
            case 2: return "Kolicina";
            case 3: return "Cena";
            case 4: return "Valuta";
        }
        return "N/A";
    }
    
    

}
