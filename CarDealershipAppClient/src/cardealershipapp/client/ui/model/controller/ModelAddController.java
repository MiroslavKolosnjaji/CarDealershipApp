package cardealershipapp.client.ui.model.controller;

import cardealershipapp.client.ui.model.ModelAddForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.client.util.ControllerUtils;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.exception.InputValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelAddController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(ModelAddController.class);
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

            ControllerUtils.showMessageDialog(modelAddForm, "Model je uspešno sačuvan!");
            modelAddForm.getTxtModelName().setText("");

        } catch (InputValidationException | ServiceException e) {
            log.warn("ModelAddController (save) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(modelAddForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            log.error("Neočekivana greška prilikom čuvanja modela: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(modelAddForm,"Došlo je do neočekivane greške prilikom čuvanja modela: " + e.getMessage(),"Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void populateCombo() {
        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(modelAddForm.getComboBrands()::addItem);

        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom učitavanja modela u combobox: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(modelAddForm, "Došlo je do neočekivane greške prilikom učitavanja modela u combobox: " + ex.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validateInput() throws InputValidationException {
        if (modelAddForm.getTxtModelName().getText().trim().isEmpty() || modelAddForm.getTxtModelName().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli naziv modela!");
        }
    }

}
