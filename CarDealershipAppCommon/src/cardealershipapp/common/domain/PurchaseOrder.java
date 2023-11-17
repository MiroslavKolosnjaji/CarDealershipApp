package cardealershipapp.common.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrder implements Serializable  {
    
    private Long purchaseOrderNum;
    private LocalDate date;
    private Customer customer;
    private Vehicle vehicle;
    private User salesPerson;
    private List<PurchaseOrderItem> purchaseOrderItems;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Long purchaseOrderNum) {
        this.purchaseOrderNum = purchaseOrderNum;
    }

    public PurchaseOrder(Long purchaseOrderNum, LocalDate date, Customer customer, Vehicle vehicle, User salesPerson, List<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderNum = purchaseOrderNum;
        this.date = date;
        this.customer = customer;
        this.vehicle = vehicle;
        this.salesPerson = salesPerson;
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public Long getPurchaseOrderNum() {
        return purchaseOrderNum;
    }

    public void setPurchaseOrderNum(Long purchaseOrderNum) {
        this.purchaseOrderNum = purchaseOrderNum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public User getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(User salesPerson) {
        this.salesPerson = salesPerson;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public void setPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PurchaseOrder{");
        sb.append("purchaseOrderNum=").append(purchaseOrderNum);
        sb.append(", date=").append(date);
        sb.append(", customer=").append(customer);
        sb.append(", vehicle=").append(vehicle);
        sb.append(", salesPerson=").append(salesPerson);
        sb.append(", purchaseOrderItems=").append(purchaseOrderItems);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.purchaseOrderNum);
        hash = 61 * hash + Objects.hashCode(this.date);
        hash = 61 * hash + Objects.hashCode(this.customer);
        hash = 61 * hash + Objects.hashCode(this.vehicle);
        hash = 61 * hash + Objects.hashCode(this.salesPerson);
        hash = 61 * hash + Objects.hashCode(this.purchaseOrderItems);
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
        final PurchaseOrder other = (PurchaseOrder) obj;
        if (!Objects.equals(this.purchaseOrderNum, other.purchaseOrderNum)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.vehicle, other.vehicle)) {
            return false;
        }
        if (!Objects.equals(this.salesPerson, other.salesPerson)) {
            return false;
        }
        return Objects.equals(this.purchaseOrderItems, other.purchaseOrderItems);
    }
    
    

   
}
