package cardealershipapp.client.ui.vehicle.ci;

import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.client.ui.vehicle.VehicleAddForm;
import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.CarBodyType;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.Vehicle;
import cardealershipapp.common.transfer.Operation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleAddController implements Responsive {

    private VehicleAddForm vehicleAddForm;

    public VehicleAddController(VehicleAddForm vehicleAddForm) {
        this.vehicleAddForm = vehicleAddForm;
    }

    public void save() {

        try {

            validateInput();

            Model model = (Model) vehicleAddForm.getComboModel().getSelectedItem();
            CarBodyType bodyType = (CarBodyType) vehicleAddForm.getComboBodyType().getSelectedItem();
            FuelType fuelType = (FuelType) vehicleAddForm.getComboFuelType().getSelectedItem();
            BusinessUnit businessUnit = (BusinessUnit) vehicleAddForm.getComboBusinessUnit().getSelectedItem();
            Currency currency = (Currency) vehicleAddForm.getComboCurrency().getSelectedItem();
            String ViNumber = vehicleAddForm.getTxtVin().getText().trim();
            Integer engineDispl = Integer.valueOf(vehicleAddForm.getTxtEngineDisplacement().getText().trim());
            Integer enginePower = Integer.valueOf(vehicleAddForm.getTxtEnginePower().getText().trim());
            Integer year = Integer.valueOf(vehicleAddForm.getTxtYear().getText().trim());
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(vehicleAddForm.getTxtPrice().getText().trim()));

            Vehicle vehicle = new Vehicle(null, model, ViNumber, bodyType, engineDispl, enginePower, year, fuelType, price, currency, businessUnit);
            getResponse(Operation.VEHICLE_ADD, vehicle);

            JOptionPane.showMessageDialog(vehicleAddForm, "Vozilo je uspesno sacuvano!");
            vehicleAddForm.dispose();
        } catch (InputValidationException ive) {
            JOptionPane.showMessageDialog(vehicleAddForm, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleAddForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }


    public void populateCombo() {
        try {

            List<BusinessUnit> businessUnits = (List<BusinessUnit>) getResponse(Operation.BUSINESSUNIT_GET_ALL, null).getResult();
            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();

            businessUnits.stream().sorted(Comparator.comparing(BusinessUnit::getName)).forEach(vehicleAddForm.getComboBusinessUnit()::addItem);
            brands.stream().sorted(Comparator.comparing(Brand::getBrandName)).forEach(vehicleAddForm.getComboBrand()::addItem);
            Arrays.asList(CarBodyType.values()).forEach(vehicleAddForm.getComboBodyType()::addItem);
            Arrays.asList(FuelType.values()).forEach(vehicleAddForm.getComboFuelType()::addItem);
            Arrays.asList(Currency.values()).stream().sorted().forEach(vehicleAddForm.getComboCurrency()::addItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleAddForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateComboModel() {
        try {
            vehicleAddForm.getComboModel().removeAllItems();

            if (vehicleAddForm.getComboBrand().getSelectedIndex() == 0) {
                vehicleAddForm.getComboModel().addItem("Model");

            } else {
                Brand brand = (Brand) vehicleAddForm.getComboBrand().getSelectedItem();
                List<Model> models = (List<Model>) getResponse(Operation.MODEL_GET_ALL, null).getResult();
                models.stream().sorted(Comparator.comparing(Model::getName))
                        .filter(model -> model.getBrand().equals(brand)).forEach(vehicleAddForm.getComboModel()::addItem);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleAddForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void comboFuelTypeState() {
        if (vehicleAddForm.getComboFuelType().getSelectedItem() == FuelType.ELEKTRICNIPOGON) {
            vehicleAddForm.getLblEngineCapacity().setText("Kapacitet baterije:");
        } else {
            vehicleAddForm.getLblEngineCapacity().setText("Zapremina motora:");
        }
    }

    private void validateInput() throws Exception {
        validateComboSelectedIndex();
        validateTextFields();
        inputNumberValidation();
    }

    private void validateTextFields() throws Exception {
        List<Vehicle> vehicles = (List<Vehicle>) getResponse(Operation.VEHICLE_GET_ALL, null).getResult();
        List<Vehicle> vehicleVin = vehicles.stream()
                .filter(vehicle -> vehicle.getViNumber().equals(vehicleAddForm.getTxtVin().getText().trim()))
                .collect(Collectors.toList());


        if (vehicleAddForm.getTxtVin().getText().trim().isEmpty() || vehicleAddForm.getTxtVin().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli broj sasije!");
        } else if (vehicleAddForm.getTxtVin().getText().trim().length() != 17) {
            throw new InputValidationException("Broj sasije mora biti duzine 17 karaktera!");
        } else if (!vehicleVin.isEmpty()) {
            throw new InputValidationException("Vozilo sa ovim brojem sasije vec postoji u bazi!");
        }

        if (vehicleAddForm.getTxtEngineDisplacement().getText().trim().isEmpty() || vehicleAddForm.getTxtEngineDisplacement().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za kubikazu!");
        } else if (vehicleAddForm.getTxtEnginePower().getText().trim().isEmpty() || vehicleAddForm.getTxtEnginePower().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za snagu u kW!");
        } else if (vehicleAddForm.getTxtYear().getText().trim().isEmpty() || vehicleAddForm.getTxtYear().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za godinu prozvodnje!");
        } else if (vehicleAddForm.getTxtPrice().getText().trim().isEmpty() || vehicleAddForm.getTxtPrice().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za cenu!");
        }

    }

    private void validateComboSelectedIndex() throws InputValidationException {
        if (vehicleAddForm.getComboBrand().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali marku!");
        } else if (vehicleAddForm.getComboBodyType().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali karoseriju!");
        } else if (vehicleAddForm.getComboBodyType().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali vrstu goriva!");
        } else if (vehicleAddForm.getComboBusinessUnit().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali lokaciju!");
        } else if (vehicleAddForm.getComboCurrency().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali valutu!");
        }
    }

    private void inputNumberValidation() throws Exception {
        try {
            Integer.valueOf(vehicleAddForm.getTxtEngineDisplacement().getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za kubikazu! Unos mora biti broj!");
        }

        try {
            Integer.valueOf(vehicleAddForm.getTxtYear().getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za godinu proizvodnje! Unos mora biti broj!");
        }

        try {
            Integer.valueOf(vehicleAddForm.getTxtEnginePower().getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za snagu u kW! Unos mora biti broj!");
        }

        try {
            BigDecimal.valueOf(Double.parseDouble(vehicleAddForm.getTxtPrice().getText().trim()));
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za cenu! Unos mora biti broj!");
        }

    }

}
