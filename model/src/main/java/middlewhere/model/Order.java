package middlewhere.model;

import java.util.Collection;

/**
 * Basic model for an Order.
 * @author cschmidt
 */
public interface Order {

    public abstract void addOrderItem(OrderItem item);

    public abstract Collection<OrderItem> getOrderItems();

    public abstract String getOrderNumber();

    public abstract void setOrderNumber(String orderNumber);

}