package cardealershipapp.client.ui.model.ci;

import cardealershipapp.client.ui.model.ModelAddForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.validation.InputValidationException;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelAddController implements Responsive {

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

}
