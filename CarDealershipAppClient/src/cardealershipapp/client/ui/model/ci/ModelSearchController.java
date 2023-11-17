package cardealershipapp.client.ui.model.ci;

import cardealershipapp.client.communication.Communication;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.Model;
import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.client.ui.component.table.MyTableCustomComponents;
import cardealershipapp.client.ui.component.table.model.ModelTableModel;
import cardealershipapp.client.ui.model.ModelEditForm;
import cardealershipapp.client.ui.validation.SelectRowException;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ModelSearchController {

    public static void edit(JTable tblModels, JDialog dialog) {
        try {

            int[] selectedRows = validateSelection(tblModels);

            if (selectedRows.length > 1) {
                throw new Exception("Funkcija nije omogucena! Selektovano je vise od jednog reda!");
            }

            Long modelId = (Long) tblModels.getValueAt(selectedRows[0], 0);
            Model model = (Model) getResponse(Operation.MODEL_FIND_BY_ID, new Model(modelId)).getResult();

            ApplicationSession.getInstance().setModel(model);
            JDialog modelEditDialog = new ModelEditForm(null, true);
            modelEditDialog.setLocationRelativeTo(dialog);
            modelEditDialog.setVisible(true);

        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(dialog, sre.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void delete(JTable tblModels, JDialog dialog) {

        try {
            int[] selectedRows = validateSelection(tblModels);
            int answer;
            String confirmMessage = "";

            if (selectedRows.length == 1) {
                String modelName = (String) tblModels.getValueAt(selectedRows[0], 1);
                answer = option("Da li ste sigurni da zelite da obrisete model " + modelName.toUpperCase() + "?", dialog);
                confirmMessage = "Model je uspesno obrisan!";
            } else {
                answer = option("Da li ste sigurni da zelite da obrisete selektovane modele?", dialog);
                confirmMessage = "Selektovani modeli su uspesno obrisani!";
            }

            if (answer == JOptionPane.YES_OPTION) {

                if (selectedRows.length == 1) {
                    getResponse(Operation.MODEL_DELETE, new Model((Long) tblModels.getValueAt(selectedRows[0], 0)));
                } else {
                    List<Model> models = new ArrayList<>();
                    for (int row : selectedRows) {
                        models.add(new Model((Long) tblModels.getValueAt(row, 0)));
                    }
                    getResponse(Operation.MODEL_DELETE_MULTI, models);
                }

                JOptionPane.showMessageDialog(dialog, confirmMessage);
            }

        } catch (SelectRowException sre) {
            JOptionPane.showMessageDialog(dialog, sre.getMessage(), "Paznja!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }

    }

    private static int option(String message, JDialog dialog) {
        String[] options = {"Da", "Ne"};
        return JOptionPane.showOptionDialog(dialog, message, "Paznja!",
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, EXIT_ON_CLOSE);
    }

    private static int[] validateSelection(JTable tblCities) throws Exception {
        int[] selectedRows = tblCities.getSelectedRows();

        if (selectedRows.length == 0) {
            throw new SelectRowException("Niste selektovali red u tabeli!");
        }
        return selectedRows;
    }

    public static void fillTable(JTable tblModels, JComboBox comboBrand, JDialog dialog) {
        try {

            List<Model> models = (List<Model>) getResponse(Operation.MODEL_GET_ALL, null).getResult();

            if (comboBrand.getSelectedIndex() > 0) {
                Brand brand = (Brand) comboBrand.getSelectedItem();
                List<Model> modelsSortedByName = models.stream().sorted(Comparator.comparing(Model::getName))
                .filter(model -> model.getBrand().equals(brand))
                .collect(Collectors.toList());
                tblModels.setModel(new ModelTableModel(modelsSortedByName));
            } else {
                List<Model> modelsSortedByBrandAndName = models.stream().sorted(Comparator.comparing(Model::getBrandName).thenComparing(Model::getName))
                .collect(Collectors.toList());
                tblModels.setModel(new ModelTableModel(modelsSortedByBrandAndName));
            }
                MyTableCustomComponents.setTblHeader(tblModels);
                MyTableCustomComponents.centerCellText(tblModels);

        } catch (SocketException soe) {
            JOptionPane.showMessageDialog(dialog, soe.getMessage(), "Paznja!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void populateCombo(JComboBox comboBrand, JDialog dialog) {
        try {

            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();
            brands.forEach(comboBrand::addItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Doslo je do greske prilikom ucitavanja podataka za comboBox!", "Paznja!", JOptionPane.ERROR_MESSAGE);
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
