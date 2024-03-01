package cardealershipapp.client.ui.model.ci;

import cardealershipapp.client.ui.model.ModelSearchForm;
import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.MyTableCustomComponents;
import cardealershipapp.client.ui.component.table.model.ModelTableModel;
import cardealershipapp.client.ui.model.ModelEditForm;
import cardealershipapp.client.ui.validation.SelectRowException;
import cardealershipapp.common.transfer.Operation;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelSearchController implements Responsive {

    private final ModelSearchForm modelSearchForm;

    public ModelSearchController(ModelSearchForm modelSearchForm) {
        this.modelSearchForm = modelSearchForm;
    }

    public void edit() {
        try {

            int[] selectedRows = validateSelection(modelSearchForm.getTblModels());

            if (selectedRows.length > 1) {
                throw new Exception("Funkcija nije omogucena! Selektovano je vise od jednog reda!");
            }

            Long modelId = (Long) modelSearchForm.getTblModels().getValueAt(selectedRows[0], 0);
            Model model = (Model) getResponse(Operation.MODEL_FIND_BY_ID, new Model(modelId)).getResult();

            ApplicationSession.getInstance().setModel(model);
            JDialog modelEditDialog = new ModelEditForm(null, true);
            modelEditDialog.setLocationRelativeTo(modelSearchForm);
            modelEditDialog.setVisible(true);

        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(modelSearchForm, sre.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void delete() {

        try {
            int[] selectedRows = validateSelection(modelSearchForm.getTblModels());
            int answer;
            String confirmMessage = "";

            if (selectedRows.length == 1) {
                String modelName = (String) modelSearchForm.getTblModels().getValueAt(selectedRows[0], 1);
                answer = option("Da li ste sigurni da zelite da obrisete model " + modelName.toUpperCase() + "?", modelSearchForm);
                confirmMessage = "Model je uspesno obrisan!";
            } else {
                answer = option("Da li ste sigurni da zelite da obrisete selektovane modele?", modelSearchForm);
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

                JOptionPane.showMessageDialog(modelSearchForm, confirmMessage);
            }

        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(modelSearchForm, sre.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }

    }

    private int option(String message, JDialog dialog) {
        String[] options = {"Da", "Ne"};
        return JOptionPane.showOptionDialog(dialog, message, "Paznja!",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

    private int[] validateSelection(JTable tblCities) throws Exception {
        int[] selectedRows = tblCities.getSelectedRows();

        if (selectedRows.length == 0) {
            throw new SelectRowException("Niste selektovali red u tabeli!");
        }
        return selectedRows;
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
            JOptionPane.showMessageDialog(modelSearchForm, soe.getMessage(), "Paznja!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelSearchForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void populateCombo() {
        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(modelSearchForm.getComboBrand()::addItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelSearchForm, "Doslo je do greske prilikom ucitavanja podataka za comboBox!", "Paznja!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
