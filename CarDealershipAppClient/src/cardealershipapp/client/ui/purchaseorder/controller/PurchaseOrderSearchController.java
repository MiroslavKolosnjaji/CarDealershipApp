package cardealershipapp.client.ui.purchaseorder.controller;

import cardealershipapp.client.ui.purchaseorder.PurchaseOrderDetailsForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.MyTableCustomComponents;
import cardealershipapp.client.ui.component.table.model.PurchaseOrderTableModel;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderEditForm;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderSearchForm;
import cardealershipapp.client.ui.exception.SelectRowException;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderSearchController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderCreateController.class);
    private final PurchaseOrderSearchForm purchaseOrderSearchForm;

    public PurchaseOrderSearchController(PurchaseOrderSearchForm purchaseOrderSearchForm) {
        this.purchaseOrderSearchForm = purchaseOrderSearchForm;
    }

    public void delete() {
        try {
            int[] selectedRows = validateSelection(purchaseOrderSearchForm.getTblPurchaseOrders());
            int answer;
            String confirmMessage = "";

            if (selectedRows.length == 1) {
                Long purchaseOrderNumber = (Long) purchaseOrderSearchForm.getTblPurchaseOrders().getValueAt(selectedRows[0], 0);

                answer = option("Da li ste sigurni da zelite da obrisete porudzbenicu " + purchaseOrderNumber, purchaseOrderSearchForm);
                confirmMessage = "Porudzbenica " + purchaseOrderNumber + " je uspesno obrisana!";
            } else {
                answer = option("Da li ste sigurni da zelite da obrisete selektovane porudzbenice?", purchaseOrderSearchForm);
                confirmMessage = "Porudzbenice su uspesno obrisane!";
            }

            List<PurchaseOrder> purchaseOrders = getPurchaseOrders(selectedRows, purchaseOrderSearchForm.getTblPurchaseOrders());

            if (answer == JOptionPane.YES_OPTION) {

                getResponse(Operation.PURCHASE_ORDER_DELETE_MULTI, purchaseOrders);

                List<Customer> customers = purchaseOrders.stream().map(PurchaseOrder::getCustomer).collect(Collectors.toList());
                getResponse(Operation.CUSTOMER_DELETE_MULTI, customers);

                JOptionPane.showMessageDialog(purchaseOrderSearchForm, confirmMessage);
            }

        } catch (SelectRowException | ServiceException e) {
            log.warn("PurchaseOrderSearchController (delete) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom brisanja porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm,"Došlo je do neočekivane greške prilikom brisanja porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void edit() {
        try {
            ApplicationSession.getInstance().setPurchaseOrder(getPurchaseOrder());
            JDialog dialogPurchaseEditForm = new PurchaseOrderEditForm(null, true);
            dialogPurchaseEditForm.setLocationRelativeTo(purchaseOrderSearchForm);
            dialogPurchaseEditForm.setVisible(true);
        } catch (SelectRowException | ServiceException e) {
            log.warn("PurchaseOrderSearchController (edit) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom otvaranja dialoga za editovanje porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm,"Došlo je do neočekivane greške prilikom otvaranja dialoga za editovanje porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void details() {
        try {
            ApplicationSession.getInstance().setPurchaseOrder(getPurchaseOrder());
            JDialog purchaseOrderDetail = new PurchaseOrderDetailsForm(null, true);
            purchaseOrderDetail.setLocationRelativeTo(purchaseOrderDetail);
            purchaseOrderDetail.setVisible(true);
        } catch (SelectRowException | ServiceException e) {
            log.warn("PurchaseOrderSearchController (details) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom otvaranja dialoga za detalje porudžbine: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm,"Došlo je do neočekivane greške prilikom otvaranja dialoga za detalje porudžbine: " + ex.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private PurchaseOrder getPurchaseOrder() throws Exception {

        int[] selectedRows = validateSelection(purchaseOrderSearchForm.getTblPurchaseOrders());

        if (selectedRows.length > 1) {
            throw new SelectRowException("Selektovano je vise od jednog reda!");
        }

        Long purchaseOrderId = (Long) purchaseOrderSearchForm.getTblPurchaseOrders().getValueAt(selectedRows[0], 0);

        List<PurchaseOrder> po = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();
        List<PurchaseOrderItem> poi = (List<PurchaseOrderItem>) getResponse(Operation.PURCHASE_ORDER_ITEM_GET_ALL, null).getResult();

        List<PurchaseOrder> purchaseOrders = po.stream()
                .filter(purchaseOrder -> purchaseOrder.getPurchaseOrderNum().equals(purchaseOrderId))
                .collect(Collectors.toList());

        List<PurchaseOrderItem> items = poi.stream()
                .sorted(Comparator.comparing(PurchaseOrderItem::getOrdinalNum))
                .filter(item -> item.getPurchaseOrder().getPurchaseOrderNum().equals(purchaseOrderId))
                .collect(Collectors.toList());

        purchaseOrders.get(0).setPurchaseOrderItems(items);
        return purchaseOrders.get(0);

    }

    private List<PurchaseOrder> getPurchaseOrders(int[] selectedRows, JTable tblPurchaseOrders) throws Exception {
        List<PurchaseOrder> pos = new ArrayList<>();

        for (int row : selectedRows) {
            PurchaseOrder p = (PurchaseOrder) getResponse(Operation.PURCHASE_ORDER_FIND_BY_ID, new PurchaseOrder((Long) tblPurchaseOrders.getValueAt(row, 0))).getResult();
            pos.add(p);
        }
        return pos;
    }

    private int[] validateSelection(JTable tblCities) throws Exception {
        int[] selectedRows = tblCities.getSelectedRows();

        if (selectedRows.length == 0) {
            throw new SelectRowException("Niste selektovali red u tabeli!");
        }
        return selectedRows;
    }

    private int option(String message, JDialog dialog) {
        String[] options = {"Da", "Ne"};
        return JOptionPane.showOptionDialog(dialog, message, "Paznja!",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

    public void fillTable() {
        try {

            List<PurchaseOrder> purchaseOrders = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();

            List<PurchaseOrder> ordersSortedByDate = purchaseOrders.stream().sorted(Comparator.comparing(PurchaseOrder::getDate).reversed()).collect(Collectors.toList());
            purchaseOrderSearchForm.getTblPurchaseOrders().setModel(new PurchaseOrderTableModel(ordersSortedByDate));
            MyTableCustomComponents.setTblHeader(purchaseOrderSearchForm.getTblPurchaseOrders());
            MyTableCustomComponents.centerCellText(purchaseOrderSearchForm.getTblPurchaseOrders());

        } catch (SocketException soe) {
            log.error("Došlo je do greške prilikom komunikacije socketa: " + soe.getClass().getSimpleName() + " : " + soe.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, soe.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        } catch (ServiceException se) {
            log.warn("PurchaseOrderSearchController (fillTable) metoda: " + se.getClass().getSimpleName() + " : " + se.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, se.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            log.error("Greška prilikom popunjavanja tabele: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, "Desila se neočekivana greška prilikom popunjavanja tabele: " + e.getMessage(), "Greška!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void filterByDate() {
        try {
            LocalDate date = purchaseOrderSearchForm.getdCDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<PurchaseOrder> purchaseOrders = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();

            List<PurchaseOrder> purchaseOrdersByDate = purchaseOrders.stream().filter(order -> order.getDate().equals(date)).collect(Collectors.toList());
            purchaseOrderSearchForm.getTblPurchaseOrders().setModel(new PurchaseOrderTableModel(purchaseOrdersByDate));

        } catch (ServiceException se) {
            log.warn("PurchaseOrderSearchController (filterByDate) metoda: " + se.getClass().getSimpleName() + " : " + se.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, se.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            log.error("Greška prilikom filtriranja po datumu: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, "Desila se neočekivana greška prilikom filtriranja po datumu: " + e.getMessage(), "Greška!", JOptionPane.WARNING_MESSAGE);
        }

    }
}
