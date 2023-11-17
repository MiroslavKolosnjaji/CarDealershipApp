package cardealershipapp.server.repository;

import cardealershipapp.common.domain.PurchaseOrderItem;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderItemRepository {

    void addItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception;

    void updateItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception;

    void deleteItem(PurchaseOrderItem purchaseOrderItem) throws Exception;
    
    List<PurchaseOrderItem> getAllItems() throws Exception;

    PurchaseOrderItem findItemById(Long id) throws Exception;    
    
    List<PurchaseOrderItem> findItemByQuery(String query) throws Exception;

}
