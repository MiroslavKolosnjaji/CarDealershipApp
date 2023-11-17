package cardealershipapp.client.ui.component.table.model;

import cardealershipapp.common.domain.PurchaseOrder;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderTableModel extends AbstractTableModel {

    private final List<PurchaseOrder> purchaseOrders;
    
    public PurchaseOrderTableModel(List<PurchaseOrder> purchaseOrders){
        this.purchaseOrders = purchaseOrders;
    }
    
    @Override
    public int getRowCount() {
       if(purchaseOrders == null){
           return 0;
       }
       return purchaseOrders.size();
    }

    @Override
    public int getColumnCount() {
        return 5; 
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PurchaseOrder purchaseOrder = purchaseOrders.get(rowIndex);
        
        switch(columnIndex){
            case 0: return purchaseOrder.getPurchaseOrderNum();
            case 1: return purchaseOrder.getCustomer().getName();
            case 2: return purchaseOrder.getVehicle().getModel().getBrandName() + " " + purchaseOrder.getVehicle().getModel().getName();
            case 3: return purchaseOrder.getVehicle().getViNumber();
            case 4: return purchaseOrder.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
        
        return "N/A";
    }

    @Override
    public String getColumnName(int column) {
        
         switch(column){
            case 0: return "Broj porudzbenice";
            case 1: return "Kupac";
            case 2: return "Vozilo";
            case 3: return "Broj sasije";
            case 4: return "Datum";
        }
        return "N/A";
    }
    
    
    
}
