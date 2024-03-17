package cardealershipapp.client.ui.login.ci;

import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.login.LoginForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.domain.UserProfile;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.exception.InputValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class LoginController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final LoginForm loginForm;

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

        } catch (InputValidationException | ServiceException e) {
            log.warn("LoginController (logIn) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(loginForm, e.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (SocketException se) {
            log.error("Došlo je do greške prilikom komunikacije socketa: " + se.getClass().getSimpleName() + " : " + se.getMessage());
            JOptionPane.showMessageDialog(loginForm, se.getMessage(), "Upozorenje!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            log.error("Desila se greska u logIn metodi: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(loginForm, "Desila se neočekivana greška prilikom logovanja!", " ", JOptionPane.ERROR_MESSAGE);
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
