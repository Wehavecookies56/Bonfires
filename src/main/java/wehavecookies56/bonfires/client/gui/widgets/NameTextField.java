package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/**
 * Created by Toby on 10/11/2016.
 */
public class NameTextField extends TextFieldWidget {

    private int max = 0;

    public NameTextField(TextRenderer fontrendererObj, int x, int y, int width, int height) {
        super(fontrendererObj, x, y, width, height, Text.empty());
    }
}
