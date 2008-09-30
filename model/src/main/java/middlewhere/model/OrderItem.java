package middlewhere.model;

public interface OrderItem {

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract InventoryItem getOrderedItem();

    public abstract void setOrderedItem(InventoryItem orderedItem);

    public abstract int getLineNumber();

    public abstract void setLineNumber(int lineNumber);

    public abstract int getQuantityOrdered();

    public abstract void setQuantityOrdered(int quantityOrdered);

}