package cardealershipapp.server.repository;

import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;

import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderItemRepository {

    void saveItems(List<PurchaseOrderItem> purchaseOrderItems) throws RepositoryException;

    void updateItems(List<PurchaseOrderItem> purchaseOrderItems) throws RepositoryException;

    void deleteItem(PurchaseOrderItem purchaseOrderItem) throws RepositoryException;
    
    List<PurchaseOrderItem> getAllItems() throws RepositoryException;

    PurchaseOrderItem findItemById(Long id) throws RepositoryException, EntityNotFoundException;
    
    List<PurchaseOrderItem> findItemByQuery(String query) throws RepositoryException;

}
