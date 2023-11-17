package cardealershipapp.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class BusinessUnit implements Serializable  {

    private Long Id;
    private String name;
    private String CompanyRegId;
    private String TaxId;
    private City city;
    private String address;
    private String phone;
    private String email;

    public BusinessUnit() {
    }

    public BusinessUnit(Long Id) {
        this.Id = Id;
    }

    public BusinessUnit(Long Id, String name, String CompanyRegId, String TaxId, City city, String address, String phone, String email) {
        this.Id = Id;
        this.name = name;
        this.CompanyRegId = CompanyRegId;
        this.TaxId = TaxId;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

   

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyRegId() {
        return CompanyRegId;
    }

    public void setCompanyRegId(String CompanyRegId) {
        this.CompanyRegId = CompanyRegId;
    }

    public String getTaxId() {
        return TaxId;
    }

    public void setTaxId(String TaxId) {
        this.TaxId = TaxId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.CompanyRegId);
        hash = 79 * hash + Objects.hashCode(this.TaxId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BusinessUnit other = (BusinessUnit) obj;
        if (!Objects.equals(this.CompanyRegId, other.CompanyRegId)) {
            return false;
        }
        return Objects.equals(this.TaxId, other.TaxId);
    }
    
    

    


}
