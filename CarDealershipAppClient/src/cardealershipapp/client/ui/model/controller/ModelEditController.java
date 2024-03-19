package cardealershipapp.client.ui.model.controller;

import cardealershipapp.client.ui.model.ModelEditForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelEditController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(ModelEditController.class);
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

            JOptionPane.showMessageDialog(modelEditForm, "Podaci su uspešno promenjeni!","Obaveštenje", JOptionPane.INFORMATION_MESSAGE);

        } catch (InputValidationException | ServiceException e) {
            log.warn("ModelEditController (update) metoda: "  + e.getClass().getSimpleName() + " : " + e.getMessage());
            JOptionPane.showMessageDialog(modelEditForm, e.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom ažuriranja podataka modela: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(modelEditForm, "Desila se neočekivana greška prilikom ažuriranja podataka modela!","Greška!", JOptionPane.ERROR_MESSAGE);
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
            log.error("Neočekivana greška prilikom učitavanja modela u combobox: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(modelEditForm, "Došlo je do neočekivane greške prilikom učitavanja modela u combobox: " + ex.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void prepareForm() {

        try {
            Model model = ApplicationSession.getInstance().getModel();
            modelEditForm.getTxtId().setText(model.getId().toString());
            modelEditForm.getTxtModelName().setText(model.getName());

            Brand brand = (Brand) getResponse(Operation.BRAND_FIND_BY_ID, model.getBrand()).getResult();
            modelEditForm.getComboBrands().setSelectedItem(brand);

        }catch (ServiceException se){
            log.warn("ModelEditController (prepareForm) metoda: " + se.getClass().getSimpleName() + " : " + se.getMessage());
            JOptionPane.showMessageDialog(modelEditForm, se.getMessage(), "Pažnja!", JOptionPane.WARNING_MESSAGE);
        }catch (Exception ex) {
            log.error("Neočekivana greška prilikom pripreme forme sa neophodnim podacima modela: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            JOptionPane.showMessageDialog(modelEditForm, "Došlo je do greške prilikom popunjavanja forme sa neophodnim podacima izabranog modela: " + ex.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }

    }

}
