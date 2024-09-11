public class OrderItem {
    private Item item;
    private int orderQuantity;

    public OrderItem(String itemName, int itemQuantity, ItemGroups itemGroup, int orderQuantity) {
        this.item = new Item(itemName, itemQuantity, itemGroup);
        this.orderQuantity = orderQuantity;
    }

    public OrderItem(String name, int quantity, ItemGroups groupName, double buyPrice, int maxInvLV, int minInvLV, int orderQuantity) {
        this.item = new Item(name, quantity, groupName, buyPrice, maxInvLV, minInvLV);
        this.orderQuantity = orderQuantity;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}