package cardealershipapp.server.service;

import cardealershipapp.common.domain.PurchaseOrder;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderService {

    void add(PurchaseOrder purchaseOrder) throws Exception;

    void update(PurchaseOrder purchaseOrder) throws Exception;

    void delete(PurchaseOrder purchaseOrder) throws Exception;
    
    void deleteMultiple(List<PurchaseOrder> purchaseOrders) throws Exception;

    List<PurchaseOrder> getAll() throws Exception;

    PurchaseOrder findById(Long id) throws Exception;

    Long getPurchaseOrderNumber(Long vehicleId) throws Exception;
}
