package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.server.repository.PurchaseOrderItemRepository;
import cardealershipapp.server.service.PurchaseOrderItemService;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public PurchaseOrderItemServiceImpl(PurchaseOrderItemRepository purchaseOrderItemRepository) {
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
    }

    @Override
    public void addItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception {
        purchaseOrderItemRepository.addItems(purchaseOrderItems);
    }

    @Override
    public void updateItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception {
       purchaseOrderItemRepository.updateItems(purchaseOrderItems);
    }

    @Override
    public void delete(PurchaseOrderItem purchaseOrderItem) throws Exception {
       purchaseOrderItemRepository.deleteItem(purchaseOrderItem);
    }

    @Override
    public PurchaseOrderItem findById(Long id) throws Exception {
       return purchaseOrderItemRepository.findItemById(id);
    }

    @Override
    public List<PurchaseOrderItem> getAll() throws Exception {
        return purchaseOrderItemRepository.getAllItems();
    }

}
