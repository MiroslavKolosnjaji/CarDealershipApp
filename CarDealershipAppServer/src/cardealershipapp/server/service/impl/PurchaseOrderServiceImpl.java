package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.server.database.DataBase;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.repository.PurchaseOrderItemRepository;
import cardealershipapp.server.repository.ExtendedRepository;
import cardealershipapp.server.util.ExceptionUtils;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.exception.ServiceException;
import cardealershipapp.server.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);
    private final ExtendedRepository<PurchaseOrder, Long> purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final ExtendedRepository<Customer, Long> customerRepository;

    public PurchaseOrderServiceImpl(ExtendedRepository purchaseOrderRepository, PurchaseOrderItemRepository purchaseOrderItemRepository, ExtendedRepository<Customer, Long> customerRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void save(PurchaseOrder purchaseOrder) throws ServiceException {
        try {

            List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.getAll().stream()
                    .filter(order -> order.getVehicle().getViNumber().equals(purchaseOrder.getVehicle().getViNumber()))
                    .collect(Collectors.toList());

            if (!purchaseOrders.isEmpty()) {
                throw new InputValidationException("Nije moguce kreirati porudzbinu! Ovo vozilo vec postoji u listi porudzbina!");
            }

            Customer customer = customerRepository.saveAndReturn(purchaseOrder.getCustomer());

            purchaseOrder.getCustomer().setId(customer.getId());

            PurchaseOrder order = purchaseOrderRepository.saveAndReturn(purchaseOrder);

            Optional<List<PurchaseOrderItem>> purchaseOrderItems = Optional.ofNullable(order.getPurchaseOrderItems());

            if (purchaseOrderItems.isPresent() && !purchaseOrderItems.get().isEmpty())
                purchaseOrderItemRepository.saveItems(order.getPurchaseOrderItems());

            DataBase.getInstance().confirmTransaction();

        } catch (InputValidationException e) {
            throw new ServiceException(e.getMessage());
        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public void update(PurchaseOrder purchaseOrder) throws ServiceException {
        try {

            purchaseOrderRepository.update(purchaseOrder);
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }

    }

    @Override
    public void delete(PurchaseOrder purchaseOrder) throws ServiceException {
        try {

            purchaseOrderRepository.delete(purchaseOrder);
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }

    }

    @Override
    public void deleteMultiple(List<PurchaseOrder> purchaseOrders) throws ServiceException {
        try {

            purchaseOrderRepository.deleteMultiple(purchaseOrders);
            DataBase.getInstance().confirmTransaction();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }

    }


    @Override
    public List<PurchaseOrder> getAll() throws ServiceException {
        try {

            List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.getAll();
            DataBase.getInstance().confirmTransaction();
            return purchaseOrders;

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public PurchaseOrder findById(Long id) throws ServiceException {
        try {

            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id);
            DataBase.getInstance().confirmTransaction();
            return purchaseOrder;

        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public Long getPurchaseOrderNumber(Long vehicleId) throws ServiceException {
        try {
            String query = "SELECT PONumber, PurchaseDate, CustomerId, VehicleId, SalesPersonId FROM purchase_order WHERE VehicleId = " + vehicleId;
            List<PurchaseOrder> purchseOrders = purchaseOrderRepository.findByQuery(query);
            DataBase.getInstance().confirmTransaction();

            System.out.println("PurchaseOrder number is: " + purchseOrders.get(0).getPurchaseOrderNum());
            return purchseOrders.get(0).getPurchaseOrderNum();

        } catch (DatabaseException e) {
            log.error(ExceptionUtils.DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE + e.getMessage());
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        } catch (RepositoryException e) {
            try {
                DataBase.getInstance().cancelTransaction();
            } catch (DatabaseException ex) {
                log.error(ExceptionUtils.DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE + ex.getMessage());
                throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
            }
            throw new ServiceException(ExceptionUtils.GENERIC_ERROR_MESSAGE);
        }
    }

}
