package cardealershipapp.client.ui.model.ci;

import cardealershipapp.client.communication.Communication;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
import cardealershipapp.common.validation.InputValidationException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ModelAddController {

    public static void save(JTextField txtModelName, JComboBox comboBrands, JDialog dialog) {
        try {

            validateInput(txtModelName);

            String name = txtModelName.getText().trim();
            Brand brand = (Brand) comboBrands.getSelectedItem();

            Model model = new Model(null, name, brand);
            getResponse(Operation.MODEL_ADD, model);

            JOptionPane.showMessageDialog(dialog, "Model je uspesno unet!");
            txtModelName.setText("");

        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(dialog, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage());
        }
    }

    private static void validateInput(JTextField txtModelName) throws Exception {
        if (txtModelName.getText().trim().isEmpty() || txtModelName.getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli naziv modela!");
        }
    }

    public static void populateCombo(JComboBox comboBrands, JDialog dialog) {
        try {
            
            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(comboBrands::addItem);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
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
