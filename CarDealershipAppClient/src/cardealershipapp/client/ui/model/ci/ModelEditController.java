package cardealershipapp.client.ui.model.ci;

import cardealershipapp.client.ui.model.ModelEditForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.common.transfer.Operation;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelEditController implements Responsive {

    private final ModelEditForm modelEditForm;

    public ModelEditController(ModelEditForm modelEditForm) {
        this.modelEditForm = modelEditForm;
    }

    public void update() {
        try {

            validateInput();

            Long modelId = Long.valueOf(modelEditForm.getTxtId().getText().trim());
            Brand brand = (Brand) modelEditForm.getComboBrands().getSelectedItem();
            String name = modelEditForm.getTxtModelName().getText().trim();

            Model model = new Model(modelId, name, brand);
            getResponse(Operation.MODEL_UPDATE, model);

            JOptionPane.showMessageDialog(modelEditForm, "Podaci su uspesno promenjeni!");

        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(modelEditForm, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelEditForm, "Desila se greska, promena podataka modela nije uspela!");
        }
    }

    private void validateInput() throws Exception {
        if (modelEditForm.getTxtModelName().getText().trim().isEmpty() || modelEditForm.getTxtModelName().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli naziv modela!");
        }
    }

    public void populateCombo() {
        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(modelEditForm.getComboBrands()::addItem);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modelEditForm, "Doslo je do greske prilikom popunjavanja comboBox-a!", "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void prepareForm() {

        try {
            Model model = ApplicationSession.getInstance().getModel();
            modelEditForm.getTxtId().setText(model.getId().toString());
            modelEditForm.getTxtModelName().setText(model.getName());

            Brand brand = (Brand) getResponse(Operation.BRAND_FIND_BY_ID, model.getBrand()).getResult();
            modelEditForm.getComboBrands().setSelectedItem(brand);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelEditForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }

    }

}
