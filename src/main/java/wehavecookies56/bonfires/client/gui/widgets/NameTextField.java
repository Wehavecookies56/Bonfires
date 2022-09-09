package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;

/**
 * Created by Toby on 10/11/2016.
 */
public class NameTextField extends EditBox {

    private int max = 0;

    public NameTextField(Font fontrendererObj, int x, int y, int width, int height) {
        super(fontrendererObj, x, y, width, height, new TextComponent(""));
    }
}
