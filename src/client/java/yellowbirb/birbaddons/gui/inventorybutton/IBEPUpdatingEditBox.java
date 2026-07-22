package yellowbirb.birbaddons.gui.inventorybutton;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class IBEPUpdatingEditBox extends EditBox {

    InventoryButtonEditPopup ibep;

    public IBEPUpdatingEditBox(Font font, int x, int y, int width, int height, Component narration, InventoryButtonEditPopup ibep) {
        super(font, x, y, width, height, narration);
        this.ibep = ibep;
    }

    @Override
    public void insertText(@NonNull String input) {
        super.insertText(input);
        ibep.saveButton();
    }
}
