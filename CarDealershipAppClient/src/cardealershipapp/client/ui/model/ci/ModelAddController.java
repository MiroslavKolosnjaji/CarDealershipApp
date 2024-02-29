package cardealershipapp.client.ui.model.ci;

import cardealershipapp.client.communication.Communication;
import cardealershipapp.client.ui.model.ModelAddForm;
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

    private final ModelAddForm modelAddForm;
    public ModelAddController(ModelAddForm modelAddForm) {
        this.modelAddForm = modelAddForm;
    }

    public void save() {
        try {

            validateInput();

            String name = modelAddForm.getTxtModelName().getText().trim();
            Brand brand = (Brand) modelAddForm.getComboBrands().getSelectedItem();

            Model model = new Model(null, name, brand);
            getResponse(Operation.MODEL_ADD, model);

            JOptionPane.showMessageDialog(modelAddForm, "Model je uspesno unet!");
            modelAddForm.getTxtModelName().setText("");

        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(modelAddForm, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelAddForm, ex.getMessage());
        }
    }


    public void populateCombo() {
        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(modelAddForm.getComboBrands()::addItem);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelAddForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void validateInput() throws Exception {
        if (modelAddForm.getTxtModelName().getText().trim().isEmpty() || modelAddForm.getTxtModelName().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli naziv modela!");
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
