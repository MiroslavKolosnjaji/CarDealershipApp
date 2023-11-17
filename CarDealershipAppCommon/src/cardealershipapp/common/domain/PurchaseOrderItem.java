package cardealershipapp.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderItem implements Serializable  {

    private PurchaseOrder purchaseOrder;
    private Long ordinalNum;
    private Equipment equipment;
    private Integer quantity;

    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderItem(PurchaseOrder purchaseOrder, Long ordinalNum, Equipment equipment, Integer quantity) {
        this.purchaseOrder = purchaseOrder;
        this.ordinalNum = ordinalNum;
        this.equipment = equipment;
        this.quantity = quantity;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Long getOrdinalNum() {
        return ordinalNum;
    }

    public void setOrdinalNum(Long ordinalNum) {
        this.ordinalNum = ordinalNum;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PurchaseOrderItem{");
        sb.append("purchaseOrder=").append(purchaseOrder);
        sb.append(", ordinalNum=").append(ordinalNum);
        sb.append(", equipment=").append(equipment);
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.ordinalNum);
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
        final PurchaseOrderItem other = (PurchaseOrderItem) obj;
        return Objects.equals(this.ordinalNum, other.ordinalNum);
    }

  

}
