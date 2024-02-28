package cardealershipapp.client.ui.mainform.ci;

import cardealershipapp.client.communication.Communication;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.login.LoginForm;
import cardealershipapp.client.ui.mainform.MainForm;
import cardealershipapp.client.ui.model.ModelSearchForm;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderCreateForm;
import cardealershipapp.client.ui.purchaseorder.PurchaseOrderSearchForm;
import cardealershipapp.client.ui.vehicle.VehicleSearchForm;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Miroslav Kološnjaji
 */
public class MainController {

    private MainForm mainForm;

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

        int answer = option("Da li ste sigurni da zelite da se izlogujete?", "Odjavljivanje", mainForm);

        if (answer == JOptionPane.YES_OPTION) {
            try {

                getResponse(Operation.LOGOUT, null);

                ApplicationSession.getInstance().setLoggedUser(null);
                mainForm.getMenuProfile().setEnabled(false);
                mainForm.getMenuPurchase().setEnabled(false);
                mainForm.getMenuControlPanel().setEnabled(false);
                mainForm.getMenuLogin().setEnabled(true);
                mainForm.getLblLoggedUser().setText("");

            } catch (SocketException soe) {
                JOptionPane.showMessageDialog(mainForm, soe.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } catch (Exception ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
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

        int answer = option("Aplikacija ce biti prekinuta! Da li zelite da nastavite dalje?", "Izlaz", mainForm);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                getResponse(Operation.EXIT, null);
                System.exit(0);
            } catch (SocketException se) {
                System.out.println("Server is not connected. EXIT");
                System.exit(0);
            } catch (Exception ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    private int option(String message, String title, JFrame frame) {
        String[] options = {"Da", "Ne", "Odustani"};
        int answer = JOptionPane.showOptionDialog(frame, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, EXIT_ON_CLOSE);
        return answer;
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
