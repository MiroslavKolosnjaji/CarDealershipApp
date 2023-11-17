package cardealershipapp.server.repository.impl;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.Equipment;
import cardealershipapp.common.domain.PurchaseOrder;
import java.sql.*;
import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.server.repository.PurchaseOrderItemRepository;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderItemRepositoryImpl implements PurchaseOrderItemRepository {

    private final DataBase db = DataBase.getInstance();

    @Override
    public void addItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception {
        try {

            String query = "INSERT INTO purchase_order_item(PONumber, OrdinalNumber, EquipmentId, Quantity) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);

            for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {

                preparedStatement.setLong(1, purchaseOrderItem.getPurchaseOrder().getPurchaseOrderNum());
                preparedStatement.setLong(2, purchaseOrderItem.getOrdinalNum());
                preparedStatement.setLong(3, purchaseOrderItem.getEquipment().getId());
                preparedStatement.setInt(4, purchaseOrderItem.getQuantity());
                preparedStatement.executeUpdate();

            }

            preparedStatement.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new Exception("Dogodila se greska prilikom unosa stavki u bazu!\n" + sqle.getMessage());
        }

    }

    @Override
    public void updateItems(List<PurchaseOrderItem> purchaseOrderItems) throws Exception {
        try {
            String query = "UPDATE purchase_order_item SET EquipmentId = ?, Quantity = ? WHERE PONumber = ? AND OrdinalNumber = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);

            for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {

                preparedStatement.setLong(1, purchaseOrderItem.getEquipment().getId());
                preparedStatement.setInt(2, purchaseOrderItem.getQuantity());
                preparedStatement.setLong(3, purchaseOrderItem.getPurchaseOrder().getPurchaseOrderNum());
                preparedStatement.setLong(4, purchaseOrderItem.getOrdinalNum());
                preparedStatement.executeUpdate();

            }

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom azuriranja podataka stavke!\n" + sqle.getMessage());
        }
    }

    @Override
    public void deleteItem(PurchaseOrderItem purchaseOrderItem) throws Exception {
        try {

            String query = "DELETE FROM purchase_order_item WHERE PONumber = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, purchaseOrderItem.getPurchaseOrder().getPurchaseOrderNum());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            db.confirmTransaction();
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom brisanja stavke iz baze!\n" + sqle.getMessage());
        }
    }

    @Override
    public PurchaseOrderItem findItemById(Long id) throws Exception {
        try {
            String query = "SELECT OrdinalNumber, EquipmentId, Quantity FROM purchase_order_item WHERE PONumber = ?";
            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
                purchaseOrderItem.setPurchaseOrder(new PurchaseOrder(id));
                purchaseOrderItem.setOrdinalNum(rs.getLong("OrdinalNumber"));
                purchaseOrderItem.setEquipment(new Equipment(rs.getLong("EquipmentId")));
                purchaseOrderItem.setQuantity(rs.getInt("Quantity"));

                rs.close();
                preparedStatement.close();
                db.confirmTransaction();
                return purchaseOrderItem;
            }

            throw new Exception("Ne postoji stavka sa ovim brojem narudzbenice (id)!");

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom pretrage stavke po id-ju!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<PurchaseOrderItem> findItemByQuery(String query) throws Exception {
        try {

            List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
                purchaseOrderItem.setPurchaseOrder(new PurchaseOrder(rs.getLong("PONumber")));
                purchaseOrderItem.setOrdinalNum(rs.getLong("OrdinalNumber"));
                purchaseOrderItem.setEquipment(new Equipment(rs.getLong("EquipmentId")));
                purchaseOrderItem.setQuantity(rs.getInt("Quantity"));

                purchaseOrderItems.add(purchaseOrderItem);
            }
            rs.close();
            preparedStatement.close();
            db.confirmTransaction();
            return purchaseOrderItems;

        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom pretrazivanja stavki!\n" + sqle.getMessage());
        }
    }

    @Override
    public List<PurchaseOrderItem> getAllItems() throws Exception {

        try {

            List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();

            String query = """
                           SELECT PI.PONumber, PI.OrdinalNumber, PI.Quantity, PI.EquipmentId, E.Name, E.Price, E.Currency, E.BrandId, B.BrandName FROM purchase_order_item `PI` JOIN
                           Equipment E ON PI.EquipmentId = E.Id JOIN Brand B ON E.BrandId = B.Id""";
            Statement statement = db.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
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
                purchaseOrderItems.add(purchaseOrderItem);

            }

            rs.close();
            statement.close();
            db.confirmTransaction();
            return purchaseOrderItems;
        } catch (SQLException sqle) {
            db.cancelTransaction();
            sqle.printStackTrace();
            throw new Exception("Doslo je do greske prilikom ucitavanja stavki!\n" + sqle.getMessage());
        }
    }

}
