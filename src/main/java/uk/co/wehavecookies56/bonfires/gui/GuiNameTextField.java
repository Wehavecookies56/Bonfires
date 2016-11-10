package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

/**
 * Created by Toby on 10/11/2016.
 */
public class GuiNameTextField extends GuiTextField {

    int max = 0;

    public GuiNameTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int width, int height) {
        super(componentId, fontrendererObj, x, y, width, height);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public boolean textboxKeyTyped(char typedChar, int keyCode) {
        switch (keyCode) {
            case Keyboard.KEY_BACK:
                this.deleteFromCursor(0);
                break;
            case Keyboard.KEY_LEFT:
                this.moveCursorBy(0);
                break;
            case Keyboard.KEY_RIGHT:
                this.moveCursorBy(0);
                break;
            case Keyboard.KEY_RETURN:
                this.setFocused(false);
                break;
            default:
                String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), typedChar).toString();
                if (max > 0) {
                    if (max < text.length()) {
                        return false;
                    }
                }
            break;
        }
        return super.textboxKeyTyped(typedChar, keyCode);
    }
}
