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
 *
 * @author Miroslav Kološnjaji
 */
public class MainController {
    
    
    public static void getVehicleSearchForm(JFrame frame){
        JDialog dialog = new VehicleSearchForm(frame, true);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
    public static void getModelSearchForm(JFrame frame){
        JDialog modelDialog = new ModelSearchForm(frame, true);
        modelDialog.setLocationRelativeTo(frame);
        modelDialog.setVisible(true);
    }
    
    public static void getPurchaseOrderCreateForm(JFrame frame){
        JDialog purchaseDialog = new PurchaseOrderCreateForm(frame, true);
        purchaseDialog.setLocationRelativeTo(frame);
        purchaseDialog.setVisible(true);
    }
    
    public static void getPurchaseOrderSearchForm(JFrame frame){
        JDialog purchaseSearchDialog = new PurchaseOrderSearchForm(frame, true);
        purchaseSearchDialog.setLocationRelativeTo(frame);
        purchaseSearchDialog.setVisible(true);
    }
    
    public static void logIn(JMenu menuLogin, JFrame frame){
         if (menuLogin.isEnabled()) {
            JDialog loginDialog = new LoginForm(frame, true);
            loginDialog.setLocationRelativeTo(frame);
            loginDialog.setVisible(true);
        }
    }
    
    public static void logOff(JMenu menuProfile, JMenu menuPurchase, JMenu menuControlPanel, JMenu menuLogin, JLabel lblLoggedUser, JFrame frame){
        
        int answer = option("Da li ste sigurni da zelite da se izlogujete?", "Odjavljivanje", frame);
        
        if (answer == JOptionPane.YES_OPTION) {
            try {
                
                getResponse(Operation.LOGOUT, null);
                
                ApplicationSession.getInstance().setLoggedUser(null);
                menuProfile.setEnabled(false);
                menuPurchase.setEnabled(false);
                menuControlPanel.setEnabled(false);
                menuLogin.setEnabled(true);
                lblLoggedUser.setText("");
                
            } catch (SocketException soe) {
                JOptionPane.showMessageDialog(frame, soe.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } catch (Exception ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void availableOptions(JMenu menuProfile, JMenu menuPurchase, JMenu menuControlPanel, JMenu menuLogin, JLabel lblLoggedUser){
          if (ApplicationSession.getInstance().getLoggedUser() != null) {
            menuProfile.setEnabled(true);
            menuPurchase.setEnabled(true);
            menuControlPanel.setEnabled(true);
            menuLogin.setEnabled(false);
            lblLoggedUser.setText("Korisnik: " + ApplicationSession.getInstance().getLoggedUser().getUser().getFirstName() + " "
            + ApplicationSession.getInstance().getLoggedUser().getUser().getLastName());
        }
    }
    
    public static void closingApplication(JFrame frame){
         
        int answer = option("Aplikacija ce biti prekinuta! Da li zelite da nastavite dalje?", "Izlaz", frame); 
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
    
    
    private static int option(String message, String title, JFrame frame){
         String[] options = {"Da", "Ne", "Odustani"};
        int answer = JOptionPane.showOptionDialog(frame, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, EXIT_ON_CLOSE);
        return answer;
    }
    
    private static Response getResponse(Operation operation, Object argument) throws Exception {
        Request request = new Request(operation, argument);
        Communication.getInstance().getSender().writeObject(request);
        Response response = (Response) Communication.getInstance().getReceiver().readObject();

        if (response.getException() != null) {
            throw response.getException();
        }

        return response;
    }
}
