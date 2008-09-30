package middlewhere.model.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import middlewhere.model.Order;
import middlewhere.model.OrderItem;

public class OrderBean implements Order {
    private ArrayList<OrderItem> orderItems;

    private String orderNumber;

    /* (non-Javadoc)
     * @see middlewhere.model.Order#addOrderItem(middlewhere.model.OrderItemBean)
     */
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }

    /* (non-Javadoc)
     * @see middlewhere.model.Order#getOrderItems()
     */
    public Collection<OrderItem> getOrderItems() {
        return Collections.unmodifiableCollection(orderItems);
    }

    /* (non-Javadoc)
     * @see middlewhere.model.Order#getOrderNumber()
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.Order#setOrderNumber(java.lang.String)
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
