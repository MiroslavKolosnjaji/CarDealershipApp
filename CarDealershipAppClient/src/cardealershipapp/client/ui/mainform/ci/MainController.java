package cardealershipapp.client.ui.mainform.ci;

import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.login.LoginForm;
import cardealershipapp.client.ui.mainform.MainForm;
import cardealershipapp.client.ui.model.ModelSearchForm;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderCreateForm;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderSearchForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.client.ui.vehicle.VehicleSearchForm;
import cardealershipapp.client.util.ControllerUtils;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Miroslav Kološnjaji
 */
public class MainController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final MainForm mainForm;

    public MainController(MainForm mainForm) {
        this.mainForm = mainForm;
    }

    public void getVehicleSearchForm() {
        JDialog dialog = new VehicleSearchForm(mainForm, true);
        dialog.setLocationRelativeTo(mainForm);
        dialog.setVisible(true);
    }

    public void getModelSearchForm() {
        JDialog modelDialog = new ModelSearchForm(mainForm, true);
        modelDialog.setLocationRelativeTo(mainForm);
        modelDialog.setVisible(true);
    }

    public void getPurchaseOrderCreateForm() {
        JDialog purchaseDialog = new PurchaseOrderCreateForm(mainForm, true);
        purchaseDialog.setLocationRelativeTo(mainForm);
        purchaseDialog.setVisible(true);
    }

    public void getPurchaseOrderSearchForm() {
        JDialog purchaseSearchDialog = new PurchaseOrderSearchForm(mainForm, true);
        purchaseSearchDialog.setLocationRelativeTo(mainForm);
        purchaseSearchDialog.setVisible(true);
    }

    public void logIn() {
        if (mainForm.getMenuLogin().isEnabled()) {
            JDialog loginDialog = new LoginForm(mainForm, true);
            loginDialog.setLocationRelativeTo(mainForm);
            loginDialog.setVisible(true);
        }
    }

    public void logOff() {

        if (ControllerUtils.logoutOption(mainForm) != JOptionPane.YES_OPTION)
            return;

        try {

            getResponse(Operation.LOGOUT, null);

            ApplicationSession.getInstance().setLoggedUser(null);
            mainForm.getMenuProfile().setEnabled(false);
            mainForm.getMenuPurchase().setEnabled(false);
            mainForm.getMenuControlPanel().setEnabled(false);
            mainForm.getMenuLogin().setEnabled(true);
            mainForm.getLblLoggedUser().setText("");

        } catch (ServiceException se) {
            log.warn("MainController (logOff) metoda: " + se.getClass().getSimpleName() + " : " + se.getMessage());
            ControllerUtils.showWarningMessageDialog(mainForm, se.getMessage());
        } catch (SocketException soe) {
            log.error("Došlo je do greške prilikom komunikacije socketa: " + soe.getClass().getSimpleName() + " : " + soe.getMessage());
            ControllerUtils.showErrorMessageDialog(mainForm, "Dogodila se greška prilikom logout-a: " + soe.getMessage());
            System.exit(0);
        } catch (Exception ex) {
            log.error("Desila se greška u logOff metodi: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ControllerUtils.showErrorMessageDialog(mainForm, "Desila se neočekivana greška prilikom logout-a: " + ex.getMessage());
        }

    }

    public void availableOptions() {
        if (ApplicationSession.getInstance().getLoggedUser() != null) {
            mainForm.getMenuProfile().setEnabled(true);
            mainForm.getMenuPurchase().setEnabled(true);
            mainForm.getMenuControlPanel().setEnabled(true);
            mainForm.getMenuLogin().setEnabled(false);
            mainForm.getLblLoggedUser().setText("Korisnik: " + ApplicationSession.getInstance().getLoggedUser().getUser().getFirstName() + " "
                    + ApplicationSession.getInstance().getLoggedUser().getUser().getLastName());
        }
    }

    public void closingApplication() {

        if (ControllerUtils.exitOption(mainForm) != JOptionPane.YES_OPTION)
            return;

        try {
            getResponse(Operation.EXIT, null);
            System.exit(0);
        } catch (ServiceException se) {
            log.warn("MainController (closingApplication) metoda: " + se.getClass().getSimpleName() + " : " + se.getMessage());
            ControllerUtils.showWarningMessageDialog(mainForm, se.getMessage());
        } catch (SocketException soe) {
            log.error("Došlo je do greške prilikom komunikacije socketa: " + soe.getClass().getSimpleName() + " : " + soe.getMessage());
            ControllerUtils.showErrorMessageDialog(mainForm, "Dogodila se greška prilikom zatvaranja aplikacije: " + soe.getMessage());
            System.exit(0);
        } catch (Exception ex) {
            log.error("Desila se greška u logOff metodi: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ControllerUtils.showErrorMessageDialog(mainForm, "Desila se neočekivana greška prilikom zatvaranja aplikacije: " + ex.getMessage());
        }

    }


}
