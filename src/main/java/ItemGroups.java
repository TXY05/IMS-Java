import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private String groupName;
    private List<Item> items;

    public ItemGroups(String groupName) {
        this.groupName = groupName;
        this.items = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void addItemGroup(Item item) {
        items.add(item);
    }

    public void removeItemGroup(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }
}
