package cardealershipapp.client.ui.login.ci;

import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.login.LoginForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.exception.InputValidationException;

import java.net.SocketException;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class LoginController implements Responsive {

    private LoginForm loginForm;

    public LoginController(LoginForm loginForm) {
        this.loginForm = loginForm;
    }

    public void logIn() {
        try {
            String email = loginForm.getTxtEmailAddress().getText().trim();
            String password = String.valueOf(loginForm.getTxtPassword().getPassword());

            validateInput(email, password);

            UserProfile userProfile = new UserProfile();
            userProfile.setEmail(email);
            userProfile.setPassword(password);

            UserProfile up = (UserProfile) getResponse(Operation.LOGIN, userProfile).getResult();

            ApplicationSession.getInstance().setLoggedUser(up);
            loginForm.dispose();

        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(loginForm, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (SocketException se) {
            JOptionPane.showMessageDialog(loginForm, se.getMessage(), "Upozorenje!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(loginForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void validateInput(String email, String password) throws InputValidationException {

        if (email.isEmpty() || email.isBlank()) {
            throw new InputValidationException("Polje email nije popunjeno!");
        }

        if (password.isEmpty() || password.isBlank()) {
            throw new InputValidationException("Polje sifra nije popunjeno!");
        }
    }
}
