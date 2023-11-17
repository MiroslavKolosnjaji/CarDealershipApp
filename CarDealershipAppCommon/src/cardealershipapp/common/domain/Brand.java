package cardealershipapp.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Brand implements Serializable {

    private Long id;
    private String brandName;

    public Brand() {
    }

    public Brand(Long id) {
        this.id = id;
    }
    

    public Brand(Long id, String brandName) {
        this.id = id;
        this.brandName = brandName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return brandName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.brandName);
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
        final Brand other = (Brand) obj;
        return Objects.equals(this.brandName, other.brandName);
    }

}
