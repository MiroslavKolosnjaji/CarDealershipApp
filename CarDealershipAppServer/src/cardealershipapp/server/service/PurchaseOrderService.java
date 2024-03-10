package cardealershipapp.server.service;

import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.server.exception.ServiceException;

import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderService extends ServiceCRUD<PurchaseOrder, Long> {

    void deleteMultiple(List<PurchaseOrder> purchaseOrders) throws ServiceException;
    Long getPurchaseOrderNumber(Long vehicleId) throws ServiceException;
}
