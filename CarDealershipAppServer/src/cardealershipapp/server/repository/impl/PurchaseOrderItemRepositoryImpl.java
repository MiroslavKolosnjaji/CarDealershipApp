package cardealershipapp.server.repository.impl;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.Equipment;
import cardealershipapp.common.domain.PurchaseOrder;

import java.sql.*;

import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.server.exception.DatabaseException;
import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;
import cardealershipapp.server.repository.PurchaseOrderItemRepository;
import cardealershipapp.server.repository.query.SqlQueries;
import cardealershipapp.server.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderItemRepositoryImpl implements PurchaseOrderItemRepository {

    public static final Logger log = LoggerFactory.getLogger(PurchaseOrderItemRepositoryImpl.class);
    private final DataBase db = DataBase.getInstance();
    private final Queue<Object> paramsQueue = new ArrayDeque<>();

    @Override
    public void saveItems(List<PurchaseOrderItem> purchaseOrderItems) throws RepositoryException {
        try {

            for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
                paramsQueue.addAll(List.of(purchaseOrderItem.getPurchaseOrder().getPurchaseOrderNum(),
                        purchaseOrderItem.getOrdinalNum(),
                        purchaseOrderItem.getEquipment().getId(),
                        purchaseOrderItem.getQuantity()));
                db.executeSqlUpdate(SqlQueries.PurchaseOrderItems.INSERT, paramsQueue);
            }

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom unosa '" + purchaseOrderItems.size() + "' stavki u bazu podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Dogodila se greska prilikom unosa stavki u bazu!");
        }

    }

    @Override
    public void updateItems(List<PurchaseOrderItem> purchaseOrderItems) throws RepositoryException {
        try {

            for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
                paramsQueue.addAll(List.of(purchaseOrderItem.getEquipment().getId(),
                        purchaseOrderItem.getQuantity(),
                        purchaseOrderItem.getPurchaseOrder().getPurchaseOrderNum(),
                        purchaseOrderItem.getOrdinalNum()));
                db.executeSqlUpdate(SqlQueries.PurchaseOrderItems.UPDATE, paramsQueue);
            }


        } catch (DatabaseException dbe) {
            log.error("Greška prilikom ažuriranja '" + purchaseOrderItems.size() + "' stavki u bazi podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom azuriranja podataka stavke!");
        }
    }

    @Override
    public void deleteItem(PurchaseOrderItem purchaseOrderItem) throws RepositoryException {
        try {

            paramsQueue.add(purchaseOrderItem.getPurchaseOrder().getPurchaseOrderNum());
            db.executeSqlUpdate(SqlQueries.PurchaseOrderItems.DELETE_BY_ID, paramsQueue);

        } catch (DatabaseException dbe) {
            log.error("Greška prilikom brisanja stavke '" + purchaseOrderItem.getEquipment().getName() + "' iz baze podataka: " + dbe.getClass().getSimpleName() + ": " + dbe.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom brisanja stavke iz baze!");
        }
    }

    @Override
    public PurchaseOrderItem findItemById(Long id) throws RepositoryException, EntityNotFoundException {
        try {

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(SqlQueries.PurchaseOrderItems.SELECT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                PurchaseOrderItem purchaseOrderItem = getPurchaseOrderItemFromResultSet(rs);

                rs.close();
                preparedStatement.close();

                return purchaseOrderItem;
            }

            throw new EntityNotFoundException("Ova stavka ne postoji u narudzbenici!");

        } catch (SQLException sqle) {
            log.error("Greška prilikom pretraživanja stavke po ID '" + id + "' u bazi podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrage stavke po ID broju!");
        }
    }

    @Override
    public List<PurchaseOrderItem> findItemByQuery(String query) throws RepositoryException {
        try {

            List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                PurchaseOrderItem purchaseOrderItem = getPurchaseOrderItemFromResultSet(rs);

                purchaseOrderItems.add(purchaseOrderItem);
            }
            rs.close();
            preparedStatement.close();
            return purchaseOrderItems;

        } catch (SQLException sqle) {
            log.error(ExceptionUtils.DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE + query + " u metodi findByQeury klase: " + this.getClass().getSimpleName() + " : " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom pretrazivanja stavki  po upitu!");
        }
    }

    @Override
    public List<PurchaseOrderItem> getAllItems() throws RepositoryException {

        try {

            List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();

            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(SqlQueries.PurchaseOrderItems.SELECT_ALL);

            while (rs.next()) {
                purchaseOrderItems.add(getFullPurchaseOrderItemFromResultSet(rs));
            }

            rs.close();
            statement.close();
            return purchaseOrderItems;

        } catch (SQLException sqle) {
            log.error("Greška prilikom učitavanja stavki iz baze podataka: " + sqle.getClass().getSimpleName() + ": " + sqle.getMessage());
            throw new RepositoryException("Doslo je do greske prilikom ucitavanja stavki!");
        }
    }

    private PurchaseOrderItem getFullPurchaseOrderItemFromResultSet(ResultSet rs) throws SQLException {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setPurchaseOrder(new PurchaseOrder(rs.getLong("PI.PONumber")));
        purchaseOrderItem.setOrdinalNum(rs.getLong("PI.OrdinalNumber"));
        purchaseOrderItem.setQuantity(rs.getInt("PI.Quantity"));

        Brand brand = new Brand();
        brand.setId(rs.getLong("E.BrandId"));
        brand.setBrandName(rs.getString("B.BrandName"));

        Equipment equipment = new Equipment();
        equipment.setId(rs.getLong("PI.EquipmentId"));
        equipment.setBrand(brand);
        equipment.setName(rs.getString("E.Name"));
        equipment.setPrice(rs.getBigDecimal("E.Price"));
        equipment.setCurrency(Currency.valueOf(rs.getString("E.Currency")));

        purchaseOrderItem.setEquipment(equipment);
        return purchaseOrderItem;
    }


    private PurchaseOrderItem getPurchaseOrderItemFromResultSet(ResultSet rs) throws SQLException {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setPurchaseOrder(new PurchaseOrder(rs.getLong("PONumber")));
        purchaseOrderItem.setOrdinalNum(rs.getLong("OrdinalNumber"));
        purchaseOrderItem.setEquipment(new Equipment(rs.getLong("EquipmentId")));
        purchaseOrderItem.setQuantity(rs.getInt("Quantity"));
        return purchaseOrderItem;
    }

}
