package cardealershipapp.server.service;

import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.server.exception.ServiceException;

import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderItemService extends ServiceCRUD<PurchaseOrderItem, Long> {
    
    void addItems(List<PurchaseOrderItem> purchaseOrderItems) throws ServiceException;

    void updateItems(List<PurchaseOrderItem> purchaseOrderItems) throws ServiceException;

    void delete(PurchaseOrderItem purchaseOrderItem) throws ServiceException;

   
}
