package cardealershipapp.client.ui.vehicle.ci;

import cardealershipapp.client.communication.Communication;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.CarBodyType;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class VehicleAddController {

    public static void save(JComboBox comboBrand, JComboBox comboModel, JComboBox comboBodyType, JComboBox comboFuelType, JComboBox comboBusinessUnit,
    JComboBox comboCurrency, JTextField txtViN, JTextField txtEngineDisplacement, JTextField txtEnginePower, JTextField txtYear, JTextField txtPrice, JDialog dialog) {

        try {

            validateInput(comboBrand, comboBodyType, comboFuelType, comboBusinessUnit, comboCurrency, txtViN, txtEngineDisplacement, txtEnginePower, txtYear, txtPrice);

            Model model = (Model) comboModel.getSelectedItem();
            CarBodyType bodyType = (CarBodyType) comboBodyType.getSelectedItem();
            FuelType fuelType = (FuelType) comboFuelType.getSelectedItem();
            BusinessUnit businessUnit = (BusinessUnit) comboBusinessUnit.getSelectedItem();
            Currency currency = (Currency) comboCurrency.getSelectedItem();
            String ViNumber = txtViN.getText().trim();
            Integer engineDispl = Integer.valueOf(txtEngineDisplacement.getText().trim());
            Integer enginePower = Integer.valueOf(txtEnginePower.getText().trim());
            Integer year = Integer.valueOf(txtYear.getText().trim());
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(txtPrice.getText().trim()));

            Vehicle vehicle = new Vehicle(null, model, ViNumber, bodyType, engineDispl, enginePower, year, fuelType, price, currency, businessUnit);
            getResponse(Operation.VEHICLE_ADD, vehicle);

            JOptionPane.showMessageDialog(dialog, "Vozilo je uspesno sacuvano!");
            dialog.dispose();
        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(dialog, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void validateInput(JComboBox comboBrand, JComboBox comboBodyType, JComboBox comboFuelType, JComboBox comboBusinessUnit, JComboBox comboCurrency, JTextField ViNumber,
    JTextField engineDispl, JTextField enginePower, JTextField year, JTextField price) throws Exception {

        List<Vehicle> vehicles =(List<Vehicle>) getResponse(Operation.VEHICLE_GET_ALL, null).getResult();
        List<Vehicle> vehicleVin = vehicles.stream()
                                                   .filter(vehicle -> vehicle.getViNumber().equals(ViNumber.getText().trim()))
                                                   .collect(Collectors.toList());

        if (comboBrand.getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali marku!");
        } else if (comboBodyType.getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali karoseriju!");
        } else if (comboFuelType.getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali vrstu goriva!");
        } else if (comboBusinessUnit.getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali lokaciju!");
        } else if (comboCurrency.getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali valutu!");
        }

        if (ViNumber.getText().trim().isEmpty() || ViNumber.getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli broj sasije!");
        } else if (ViNumber.getText().trim().length() != 17) {
            throw new InputValidationException("Broj sasije mora biti duzine 17 karaktera!");
        } else if (!vehicleVin.isEmpty()) {
            throw new InputValidationException("Vozilo sa ovim brojem sasije vec postoji u bazi!");
        }

        if (engineDispl.getText().trim().isEmpty() || engineDispl.getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za kubikazu!");
        } else if (enginePower.getText().trim().isEmpty() || enginePower.getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za snagu u kW!");
        } else if (year.getText().trim().isEmpty() || year.getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za godinu prozvodnje!");
        } else if (price.getText().trim().isEmpty() || price.getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za cenu!");
        }
        inputNumberValidation(engineDispl, enginePower, year, price);
    }

    private static void inputNumberValidation(JTextField engineDispl, JTextField enginePower, JTextField year, JTextField price) throws Exception {
        try {
            Integer.valueOf(engineDispl.getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za kubikazu! Unos mora biti broj!");
        }
        
        try {
            Integer.valueOf(year.getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za godinu proizvodnje! Unos mora biti broj!");
        }

        try {
            Integer.valueOf(enginePower.getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za snagu u kW! Unos mora biti broj!");
        }

        try {
            BigDecimal.valueOf(Double.parseDouble(price.getText().trim()));
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za cenu! Unos mora biti broj!");
        }

    }

    public static void populateCombo(JComboBox comboBusinessUnit, JComboBox comboBrand,
    JComboBox comboBodyType, JComboBox comboFuelType, JComboBox comboCurrency, JDialog dialog) {
        try {

            List<BusinessUnit> businessUnits = (List<BusinessUnit>) getResponse(Operation.BUSINESSUNIT_GET_ALL, null).getResult();
            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();

            businessUnits.stream().sorted(Comparator.comparing(BusinessUnit::getName)).forEach(comboBusinessUnit::addItem);
            brands.stream().sorted(Comparator.comparing(Brand::getBrandName)).forEach(comboBrand::addItem);
            Arrays.asList(CarBodyType.values()).forEach(comboBodyType::addItem);
            Arrays.asList(FuelType.values()).forEach(comboFuelType::addItem);
            Arrays.asList(Currency.values()).stream().sorted().forEach(comboCurrency::addItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void updateComboModel(JComboBox comboBrand, JComboBox comboModel, JDialog dialog) {
        try {
            comboModel.removeAllItems();

            if (comboBrand.getSelectedIndex() == 0) {
                comboModel.addItem("Model");

            } else {
                Brand brand = (Brand) comboBrand.getSelectedItem();
                List<Model> models = (List<Model>) getResponse(Operation.MODEL_GET_ALL, null).getResult();
                models.stream().sorted(Comparator.comparing(Model::getName))
                               .filter(model -> model.getBrand().equals(brand)).forEach(comboModel::addItem);
            }
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
