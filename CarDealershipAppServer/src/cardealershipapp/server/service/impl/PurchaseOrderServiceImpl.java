package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.PurchaseOrderService;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final Repository<PurchaseOrder, Long> purchaseOrderRepository;

    public PurchaseOrderServiceImpl(Repository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    
    }

    @Override
    public void add(PurchaseOrder purchaseOrder) throws Exception {

        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.getAll().stream()
        .filter(order -> order.getVehicle().getViNumber().equals(purchaseOrder.getVehicle().getViNumber()))
        .collect(Collectors.toList());
        if (!purchaseOrders.isEmpty()) {
            throw new InputValidationException("Nije moguce kreirati porudzbinu! Ovo vozilo vec postoji u listi porudzbina!");
        }
        purchaseOrderRepository.add(purchaseOrder);
    }

    @Override
    public void update(PurchaseOrder purchaseOrder) throws Exception {
        purchaseOrderRepository.update(purchaseOrder);
    }

    @Override
    public void delete(PurchaseOrder purchaseOrder) throws Exception {
        purchaseOrderRepository.delete(purchaseOrder);
    }

    @Override
    public void deleteMultiple(List<PurchaseOrder> purchaseOrders) throws Exception {
        purchaseOrderRepository.deleteMultiple(purchaseOrders);
    }
    
    

    @Override
    public List<PurchaseOrder> getAll() throws Exception {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.getAll();
        return purchaseOrders;
    }

    @Override
    public PurchaseOrder findById(Long id) throws Exception {
        return purchaseOrderRepository.findById(id);
    }

    @Override
    public Long getPurchaseOrderNumber(Long vehicleId) throws Exception {
        String query = "SELECT PONumber, PurchaseDate, CustomerId, VehicleId, SalesPersonId FROM purchase_order WHERE VehicleId = " + vehicleId;
        List<PurchaseOrder> purchseOrders = purchaseOrderRepository.findByQuery(query);
        System.out.println("PurchaseOrder number is: " + purchseOrders.get(0).getPurchaseOrderNum());
        return purchseOrders.get(0).getPurchaseOrderNum();
    }

}
