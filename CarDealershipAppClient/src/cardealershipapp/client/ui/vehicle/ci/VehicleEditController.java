package cardealershipapp.client.ui.vehicle.ci;

import cardealershipapp.client.ui.response.Responsive;
import cardealershipapp.client.ui.vehicle.VehicleEditForm;
import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.domain.CarBodyType;
import cardealershipapp.common.domain.Currency;
import cardealershipapp.common.domain.FuelType;
import cardealershipapp.common.domain.Model;
import cardealershipapp.common.domain.Vehicle;

import cardealershipapp.client.session.ApplicationSession;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.common.transfer.Operation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleEditController implements Responsive {

    private VehicleEditForm vehicleEditForm;

    public VehicleEditController(VehicleEditForm vehicleEditForm) {
        this.vehicleEditForm = vehicleEditForm;
    }

    public void update() {

        try {

            validateInput();

            Long id = Long.valueOf(vehicleEditForm.getTxtId().getText().trim());
            Model model = (Model) vehicleEditForm.getComboModel().getSelectedItem();
            CarBodyType bodyType = (CarBodyType) vehicleEditForm.getComboBodyType().getSelectedItem();
            FuelType fuelType = (FuelType) vehicleEditForm.getComboFuelType().getSelectedItem();
            BusinessUnit businessUnit = (BusinessUnit) vehicleEditForm.getComboBusinessUnit().getSelectedItem();
            Currency currency = (Currency) vehicleEditForm.getComboCurrency().getSelectedItem();
            String ViNumber = vehicleEditForm.getTxtVin().getText().trim();
            Integer engineDispl = Integer.valueOf(vehicleEditForm.getTxtEngineDisplacement().getText().trim());
            Integer enginePower = Integer.valueOf(vehicleEditForm.getTxtEnginePower().getText().trim());
            Integer year = Integer.valueOf(vehicleEditForm.getTxtYear().getText().trim());
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(vehicleEditForm.getTxtPrice().getText().trim()));

            Vehicle vehicle = new Vehicle(id, model, ViNumber, bodyType, engineDispl, enginePower, year, fuelType, price, currency, businessUnit);
            getResponse(Operation.VEHICLE_UPDATE, vehicle);

            JOptionPane.showMessageDialog(vehicleEditForm, "Podaci o vozilu su uspesno izmenjeni!");
            vehicleEditForm.dispose();
        } catch (InputValidationException ive) {
            ive.printStackTrace();
            JOptionPane.showMessageDialog(vehicleEditForm, ive.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleEditForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }


    public void populateCombo() {
        try {

            List<BusinessUnit> businessUnits = (List<BusinessUnit>) getResponse(Operation.BUSINESSUNIT_GET_ALL, null).getResult();
            List<Brand> brands = (List<Brand>) getResponse(Operation.BRAND_GET_ALL, null).getResult();

            businessUnits.stream().sorted(Comparator.comparing(BusinessUnit::getName)).forEach(vehicleEditForm.getComboBusinessUnit()::addItem);
            brands.stream().sorted(Comparator.comparing(Brand::getBrandName)).forEach(vehicleEditForm.getComboBrand()::addItem);
            Arrays.asList(CarBodyType.values()).forEach(vehicleEditForm.getComboBodyType()::addItem);
            Arrays.asList(FuelType.values()).forEach(vehicleEditForm.getComboFuelType()::addItem);
            Arrays.asList(Currency.values()).stream().sorted().forEach(vehicleEditForm.getComboCurrency()::addItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleEditForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateComboModel() {
        try {
            vehicleEditForm.getComboModel().removeAllItems();

            if (vehicleEditForm.getComboBrand().getSelectedIndex() == 0) {
                vehicleEditForm.getComboModel().addItem("Model");

            } else {
                Brand brand = (Brand) vehicleEditForm.getComboBrand().getSelectedItem();
                List<Model> models = (List<Model>) getResponse(Operation.MODEL_GET_ALL, null).getResult();
                models.stream().sorted(Comparator.comparing(Model::getName))
                        .filter(model -> model.getBrand().equals(brand)).forEach(vehicleEditForm.getComboModel()::addItem);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vehicleEditForm, ex.getMessage(), "Paznja!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void validateInput() throws Exception {
        validateComboSelectedIndex();
        validateTextFields();
        inputNumberValidation();
    }

    private void validateTextFields() throws Exception {

        if (vehicleEditForm.getTxtVin().getText().trim().isEmpty() || vehicleEditForm.getTxtVin().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli broj sasije!");
        } else if (vehicleEditForm.getTxtVin().getText().trim().length() != 17) {
            throw new InputValidationException("Broj sasije mora biti duzine 17 karaktera!");
        }

        if (vehicleEditForm.getTxtEngineDisplacement().getText().trim().isEmpty() || vehicleEditForm.getTxtEngineDisplacement().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za kubikazu!");
        } else if (vehicleEditForm.getTxtEnginePower().getText().trim().isEmpty() || vehicleEditForm.getTxtEnginePower().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za snagu u kW!");
        } else if (vehicleEditForm.getTxtYear().getText().trim().isEmpty() || vehicleEditForm.getTxtYear().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za godinu prozvodnje!");
        } else if (vehicleEditForm.getTxtPrice().getText().trim().isEmpty() || vehicleEditForm.getTxtPrice().getText().trim().isBlank()) {
            throw new InputValidationException("Niste uneli vrednost za cenu!");
        }

    }

    private void validateComboSelectedIndex() throws InputValidationException {
        if (vehicleEditForm.getComboBrand().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali marku!");
        } else if (vehicleEditForm.getComboBodyType().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali karoseriju!");
        } else if (vehicleEditForm.getComboBodyType().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali vrstu goriva!");
        } else if (vehicleEditForm.getComboBusinessUnit().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali lokaciju!");
        } else if (vehicleEditForm.getComboCurrency().getSelectedIndex() == 0) {
            throw new InputValidationException("Niste izabrali valutu!");
        }
    }

    private void inputNumberValidation() throws Exception {
        try {
            Integer.valueOf(vehicleEditForm.getTxtEngineDisplacement().getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za kubikazu! Unos mora biti broj!");
        }

        try {
            Integer.valueOf(vehicleEditForm.getTxtYear().getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za godinu proizvodnje! Unos mora biti broj!");
        }

        try {
            Integer.valueOf(vehicleEditForm.getTxtEnginePower().getText().trim());
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za snagu u kW! Unos mora biti broj!");
        }

        try {
            BigDecimal.valueOf(Double.parseDouble(vehicleEditForm.getTxtPrice().getText().trim()));
        } catch (NumberFormatException nfe) {
            throw new InputValidationException("pogresan unos za cenu! Unos mora biti broj!");
        }

    }
    public void prepareForm() {
        Vehicle vehicle = ApplicationSession.getInstance().getVehicle();

        vehicleEditForm.getComboBrand().setSelectedItem(vehicle.getModel().getBrand());
        vehicleEditForm.getComboModel().setSelectedItem(vehicle.getModel());
        vehicleEditForm.getComboBodyType().setSelectedItem(vehicle.getBodyType());
        vehicleEditForm.getComboFuelType().setSelectedItem(vehicle.getFuelType());
        vehicleEditForm.getComboCurrency().setSelectedItem(vehicle.getCurrency());
        vehicleEditForm.getComboBusinessUnit().setSelectedItem(vehicle.getBusinessUnit());
        vehicleEditForm.getTxtEngineDisplacement().setText(String.valueOf(vehicle.getEngineDisplacement()));
        vehicleEditForm.getTxtEnginePower().setText(String.valueOf(vehicle.getEnginePower()));
        vehicleEditForm.getTxtPrice().setText(String.valueOf(vehicle.getPrice()));
        vehicleEditForm.getTxtYear().setText(String.valueOf(vehicle.getYearOfProd()));
        vehicleEditForm.getTxtViN().setText(vehicle.getViNumber());
        vehicleEditForm.getTxtId().setText(String.valueOf(vehicle.getId()));
    }

}
