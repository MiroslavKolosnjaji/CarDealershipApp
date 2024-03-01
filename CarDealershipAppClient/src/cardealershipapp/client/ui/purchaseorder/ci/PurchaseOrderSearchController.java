package cardealershipapp.client.ui.purchaseorder.ci;

import cardealershipapp.client.ui.purchaseorder.PurchaseOrderDetailsForm;
import cardealershipapp.client.communication.Communication;
import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.common.domain.PurchaseOrderItem;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.MyTableCustomComponents;
import cardealershipapp.client.ui.component.table.model.PurchaseOrderTableModel;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderEditForm;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderSearchForm;
import cardealershipapp.client.ui.validation.SelectRowException;
import cardealershipapp.common.domain.Customer;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
import com.toedter.calendar.JDateChooser;

import java.net.SocketException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderSearchController {

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

        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, sre.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void edit() {
        try {
            ApplicationSession.getInstance().setPurchaseOrder(getPurchaseOrder());
            JDialog dialogPurchaseEditForm = new PurchaseOrderEditForm(null, true);
            dialogPurchaseEditForm.setLocationRelativeTo(purchaseOrderSearchForm);
            dialogPurchaseEditForm.setVisible(true);
        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, sre.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void details() {
        try {
            ApplicationSession.getInstance().setPurchaseOrder(getPurchaseOrder());
            JDialog purchaseOrderDetail = new PurchaseOrderDetailsForm(null, true);
            purchaseOrderDetail.setLocationRelativeTo(purchaseOrderDetail);
            purchaseOrderDetail.setVisible(true);
        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, sre.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderSearchController.class.getName()).log(Level.SEVERE, null, ex);
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
            JOptionPane.showMessageDialog(purchaseOrderSearchForm, soe.getMessage(), "Upozorenje!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderSearchForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filterByDate() {
        try {
            LocalDate date = purchaseOrderSearchForm.getdCDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<PurchaseOrder> purchaseOrders = (List<PurchaseOrder>) getResponse(Operation.PURCHASE_ORDER_GET_ALL, null).getResult();

            List<PurchaseOrder> purchaseOrdersByDate = purchaseOrders.stream().filter(order -> order.getDate().equals(date)).collect(Collectors.toList());
            purchaseOrderSearchForm.getTblPurchaseOrders().setModel(new PurchaseOrderTableModel(purchaseOrdersByDate));

        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Response getResponse(Operation operation, Object argument) throws Exception {
        Request request = new Request(operation, argument);
        Communication.getInstance().getSender().writeObject(request);
        Response response = (Response) Communication.getInstance().getReceiver().readObject();

        if (response.getException() != null) {
            throw response.getException();
        }
        return response;
    }
}
