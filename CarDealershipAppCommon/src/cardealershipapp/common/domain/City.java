package cardealershipapp.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class City implements Serializable {

    private Long id;
    private Integer zipCode;
    private String name;

    public City() {
    }

    public City(Long id) {
        this.id = id;
    }

    public City(Long id, Integer zipCode, String name) {
        this.id = id;
        this.zipCode = zipCode;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        hash = 53 * hash + Objects.hashCode(this.zipCode);
        hash = 53 * hash + Objects.hashCode(this.name);
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
        final City other = (City) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.zipCode, other.zipCode);
    }

}
