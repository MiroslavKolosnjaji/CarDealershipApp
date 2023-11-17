package cardealershipapp.common.domain;

import java.io.Serializable;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Customer implements Serializable  {
    
    private Long id;
    private String name;
    private String CompanyName;
    private String Address;
    private String phone;
    private String email;

    public Customer() {
    }

    public Customer(Long id) {
        this.id = id;
    }

    public Customer(Long id, String name, String CompanyName, String Address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.CompanyName = CompanyName;
        this.Address = Address;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
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
        sb.append("Customer{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", CompanyName=").append(CompanyName);
        sb.append(", Address=").append(Address);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
