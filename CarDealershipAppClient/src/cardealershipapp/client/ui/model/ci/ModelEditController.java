package cardealershipapp.client.ui.model.ci;

import cardealershipapp.client.communication.Communication;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ModelEditController {

    public static void update(JTextField txtId, JTextField txtModelName, JComboBox comboBrands, JDialog dialog) {
        try {

            validateInput(txtModelName);

            Long modelId = Long.valueOf(txtId.getText().trim());
            Brand brand = (Brand) comboBrands.getSelectedItem();
            String name = txtModelName.getText().trim();

            Model model = new Model(modelId, name, brand);
            getResponse(Operation.MODEL_UPDATE, model);

            JOptionPane.showMessageDialog(dialog, "Podaci su uspesno promenjeni!");

        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(dialog, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Desila se greska, promena podataka modela nije uspela!");
        }
    }

    private static void validateInput(JTextField txtModelName) throws Exception {
        if (txtModelName.getText().trim().isEmpty() || txtModelName.getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli naziv modela!");
        }
    }

    public static void populateCombo(JComboBox comboBrands, JDialog dialog) {
        try {

            List<Brand> brands = (List<Brand>)  getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(comboBrands::addItem);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Doslo je do greske prilikom popunjavanja comboBox-a!", "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void prepareForm(JTextField txtId, JTextField txtModelName, JComboBox comboBrands, JDialog dialog) {

        try {
            Model model = ApplicationSession.getInstance().getModel();
            txtId.setText(model.getId().toString());
            txtModelName.setText(model.getName());

            Brand brand = (Brand) getResponse(Operation.BRAND_FIND_BY_ID, model.getBrand()).getResult();
            comboBrands.setSelectedItem(brand);

        } catch (Exception ex) {
            ex.printStackTrace();
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
