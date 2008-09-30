package middlewhere.model;

import java.io.Serializable;

import middlewhere.util.ToStringHelper;

/**
 * Encapsulates a request to update an <code>InventoryItem</code>'s quantity
 * on order. 
 * @author cschmidt
 */
public class QuantityOnHandUpdate 
    implements Comparable<Object>, Serializable {

    private static ToStringHelper toString = 
        new ToStringHelper(QuantityOnHandUpdate.class);
    
    /**
     * 
     */
    private static final long serialVersionUID = 2051133347942996810L;

    private String itemNumber;
    private int quantityOrdered;

    public QuantityOnHandUpdate(String itemNumber, int quantityOrdered) {
        this.itemNumber = itemNumber;
        if (quantityOrdered < 0) {
            throw new IllegalArgumentException("quantityOrdered="
                    + quantityOrdered);
        }
        this.quantityOrdered = quantityOrdered;
    }

    public int compareTo(Object o) {
        return itemNumber.compareTo(((QuantityOnHandUpdate)o).itemNumber);
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString.start(buf);
        toString.addAttribute(buf, "itemNumber", itemNumber);
        toString.addAttribute(buf, "quantityOrdered", quantityOrdered);
        toString.end(buf);
        return buf.toString();
    }
}
