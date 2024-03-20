package cardealershipapp.client.ui.model.controller;

import cardealershipapp.client.ui.model.ModelSearchForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.client.util.ControllerUtils;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.MyTableCustomComponents;
import cardealershipapp.client.ui.component.table.model.ModelTableModel;
import cardealershipapp.client.ui.model.ModelEditForm;
import cardealershipapp.client.ui.exception.SelectRowException;
import cardealershipapp.common.exception.ServiceException;
import cardealershipapp.common.transfer.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelSearchController implements Responsive {

    private static final Logger log = LoggerFactory.getLogger(ModelSearchController.class);
    private final ModelSearchForm modelSearchForm;

    public ModelSearchController(ModelSearchForm modelSearchForm) {
        this.modelSearchForm = modelSearchForm;
    }

    public void edit() {
        try {

            int[] selectedRows = validateSelection(modelSearchForm.getTblModels());

            if (selectedRows.length > 1)
                throw new SelectRowException("Funkcija nije omogućena! Selektovano je više od jednog reda!");


            Long modelId = (Long) modelSearchForm.getTblModels().getValueAt(selectedRows[0], 0);
            Model model = (Model) getResponse(Operation.MODEL_FIND_BY_ID, new Model(modelId)).getResult();

            ApplicationSession.getInstance().setModel(model);
            JDialog modelEditDialog = new ModelEditForm(null, true);
            modelEditDialog.setLocationRelativeTo(modelSearchForm);
            modelEditDialog.setVisible(true);

        } catch (SelectRowException | ServiceException e) {
            log.warn("ModelSearchController (edit) metoda: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            ControllerUtils.showWarningMessageDialog(modelSearchForm, e.getMessage());
        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom otvaranja edit dialoga: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ControllerUtils.showErrorMessageDialog(modelSearchForm, "Desila se neočekivana greška prilikom otvaranja edit dialoga: " + ex.getMessage());
        }
    }

    public void delete() {

        try {
            int[] selectedRows = validateSelection(modelSearchForm.getTblModels());
            int answer;
            String confirmMessage;

            if (selectedRows.length == 1) {
                String modelName = (String) modelSearchForm.getTblModels().getValueAt(selectedRows[0], 1);
                answer = ControllerUtils.optionPane("Da li ste sigurni da zelite da obrisete model " + modelName.toUpperCase() + "?", modelSearchForm);
                confirmMessage = "Model je uspesno obrisan!";
            } else {
                answer = ControllerUtils.optionPane("Da li ste sigurni da zelite da obrisete selektovane modele?", modelSearchForm);
                confirmMessage = "Selektovani modeli su uspesno obrisani!";
            }

            if (answer == JOptionPane.YES_OPTION) {

                if (selectedRows.length == 1) {
                    getResponse(Operation.MODEL_DELETE, new Model((Long) modelSearchForm.getTblModels().getValueAt(selectedRows[0], 0)));
                } else {
                    List<Model> models = new ArrayList<>();
                    for (int row : selectedRows) {
                        models.add(new Model((Long) modelSearchForm.getTblModels().getValueAt(row, 0)));
                    }
                    getResponse(Operation.MODEL_DELETE_MULTI, models);
                }

                ControllerUtils.showMessageDialog(modelSearchForm, confirmMessage);
            }

        } catch (SelectRowException | ServiceException ex) {
            log.warn("ModelSearchController (delete) metoda: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ControllerUtils.showWarningMessageDialog(modelSearchForm, ex.getMessage());
        } catch (Exception ex) {
            log.error("Greška prilikom brisanja modela: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ControllerUtils.showErrorMessageDialog(modelSearchForm, ex.getMessage());
        }

    }

    public void fillTable() {
        try {

            List<Model> models = (List<Model>) getResponse(Operation.MODEL_GET_ALL, null).getResult();

            if (modelSearchForm.getComboBrand().getSelectedIndex() > 0) {
                Brand brand = (Brand) modelSearchForm.getComboBrand().getSelectedItem();
                List<Model> modelsSortedByName = models.stream().sorted(Comparator.comparing(Model::getName))
                        .filter(model -> model.getBrand().equals(brand))
                        .collect(Collectors.toList());
                modelSearchForm.getTblModels().setModel(new ModelTableModel(modelsSortedByName));
            } else {
                List<Model> modelsSortedByBrandAndName = models.stream().sorted(Comparator.comparing(Model::getBrandName).thenComparing(Model::getName))
                        .collect(Collectors.toList());
                modelSearchForm.getTblModels().setModel(new ModelTableModel(modelsSortedByBrandAndName));
            }
            MyTableCustomComponents.setTblHeader(modelSearchForm.getTblModels());
            MyTableCustomComponents.centerCellText(modelSearchForm.getTblModels());

        } catch (SocketException soe) {
            log.error("Došlo je do greške prilikom komunikacije socketa: " + soe.getClass().getSimpleName() + " : " + soe.getMessage());
            ControllerUtils.showErrorMessageDialog(modelSearchForm, soe.getMessage());
            System.exit(0);
        } catch (ServiceException se) {
            log.warn("ModelSearchController (fillTable) metoda: " + se.getClass().getSimpleName() + " : " + se.getMessage());
            ControllerUtils.showWarningMessageDialog(modelSearchForm, se.getMessage());
        } catch (Exception e) {
            log.error("Greška prilikom popunjavanja tabele: " + e.getClass().getSimpleName() + " : " + e.getMessage());
            ControllerUtils.showErrorMessageDialog(modelSearchForm, "Desila se neočekivana greška prilikom popunjavanja tabele: " + e.getMessage());
        }
    }

    public void populateCombo() {
        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(modelSearchForm.getComboBrand()::addItem);

        } catch (Exception ex) {
            log.error("Neočekivana greška prilikom učitavanja brendova u combobox: " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ControllerUtils.showErrorMessageDialog(modelSearchForm, "Došlo je do neočekivane greške prilikom učitavanja brendova u combobox: " + ex.getMessage());
        }
    }

    private int[] validateSelection(JTable tblCities) throws SelectRowException {
        int[] selectedRows = tblCities.getSelectedRows();

        if (selectedRows.length == 0) {
            throw new SelectRowException("Niste selektovali red u tabeli!");
        }
        return selectedRows;
    }

}
