package cardealershipapp.server.service;

import cardealershipapp.common.domain.PurchaseOrderItem;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderItemService {
    
    void addItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception;

    void updateItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception;

    void delete(PurchaseOrderItem purchaseOrderItem) throws Exception;
    
    PurchaseOrderItem findById(Long id) throws Exception;    
    
    List<PurchaseOrderItem> getAll() throws Exception;
   
}
