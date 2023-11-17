package cardealershipapp.common.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Vehicle implements Serializable  {
    
    private Long id;
    private Model model;
    private String viNumber;
    private CarBodyType bodyType;
    private Integer engineDisplacement;
    private Integer enginePower;
    private Integer yearOfProd;
    private FuelType fuelType;
    private BigDecimal price;
    private Currency currency;
    private BusinessUnit businessUnit;

    public Vehicle() {
    }

    public Vehicle(Long id) {
        this.id = id;
    }

    public Vehicle(Long id, Model model, String viNumber, CarBodyType bodyType, Integer engineDisplacement, Integer enginePower, Integer yearOfProd, FuelType fuelType, BigDecimal price, Currency currency, BusinessUnit businessUnit) {
        this.id = id;
        this.model = model;
        this.viNumber = viNumber;
        this.bodyType = bodyType;
        this.engineDisplacement = engineDisplacement;
        this.enginePower = enginePower;
        this.yearOfProd = yearOfProd;
        this.fuelType = fuelType;
        this.price = price;
        this.currency = currency;
        this.businessUnit = businessUnit;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getViNumber() {
        return viNumber;
    }

    public void setViNumber(String vinNumber) {
        this.viNumber = vinNumber;
    }

    public CarBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(CarBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public int getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(Integer engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(Integer enginePower) {
        this.enginePower = enginePower;
    }

    public int getYearOfProd() {
        return yearOfProd;
    }

    public void setYearOfProd(Integer yearOfProd) {
        this.yearOfProd = yearOfProd;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vehicle{");
        sb.append("id=").append(id);
        sb.append(", model=").append(model);
        sb.append(", vinNumber=").append(viNumber);
        sb.append(", bodyType=").append(bodyType);
        sb.append(", engineDisplacement=").append(engineDisplacement);
        sb.append(", enginePower=").append(enginePower);
        sb.append(", yearOfProd=").append(yearOfProd);
        sb.append(", fuelType=").append(fuelType);
        sb.append(", price=").append(price);
        sb.append(", currency=").append(currency);
        sb.append(", businessUnit=").append(businessUnit);
        sb.append('}');
        return sb.toString();
    }

    
    
    
  
    
    
    
}
