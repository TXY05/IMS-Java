public class Product extends Item {
    private final double sellPrice;
    private final int maxLV;
    private final int minLV;

    public Product(String name, int quantity, ItemGroups groupName, double sellPrice, int maxLV, int minLV) {
        super(name, quantity, groupName);
        this.sellPrice = sellPrice;
        this.maxLV = maxLV;
        this.minLV = minLV;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public int getMaxLV() {
        return maxLV;
    }

    public int getMinLV() {
        return minLV;
    }
}

