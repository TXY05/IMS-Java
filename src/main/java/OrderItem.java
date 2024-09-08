public class OrderItem extends Item {
    private int orderQuantity;

    public OrderItem(String itemName, int itemQuantity, ItemGroups itemGroup, int orderQuantity) {
        super(itemName, itemQuantity, itemGroup);
        this.orderQuantity = orderQuantity;
    }

    public OrderItem(String name, int quantity, ItemGroups groupName, double buyPrice, int maxInvLV, int minInvLV, int orderQuantity) {
        super(name, quantity, groupName, buyPrice, maxInvLV, minInvLV);
        this.orderQuantity = orderQuantity;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
