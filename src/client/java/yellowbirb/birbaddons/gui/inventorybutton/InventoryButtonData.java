package yellowbirb.birbaddons.gui.inventorybutton;

public class InventoryButtonData {

    public int x;
    public int y;
    public int width = 18;
    public int height = 18;
    public String command = "";
    public String icon = "";

    public InventoryButtonData() {}

    public InventoryButtonData(int x, int y) {
        this.x = x;
        this.y = y;
    }
}