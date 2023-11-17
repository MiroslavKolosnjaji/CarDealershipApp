package cardealershipapp.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Model implements Serializable  {

    private Long Id;
    private String name;
    private Brand brand;

    public Model() {
    }

    public Model(Long Id) {
        this.Id = Id;
    }

    public Model(Long Id, String name, Brand brand) {
        this.Id = Id;
        this.name = name;
        this.brand = brand;
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

    public Brand getBrand() {
        return brand;
    }
    
    public String getBrandName() {
        return brand.getBrandName();
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.brand);
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
        final Model other = (Model) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.brand, other.brand);
    }

}
