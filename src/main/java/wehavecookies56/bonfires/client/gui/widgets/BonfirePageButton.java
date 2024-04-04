package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

/**
 * Created by Toby on 17/12/2016.
 */
public class BonfirePageButton extends ButtonWidget {

    private BonfireScreen parent;
    private boolean isNext;
    private int id;

    public BonfirePageButton(BonfireScreen parent, int id, int x, int y, boolean isNext) {
        super(x, y, 8, 14, Text.empty(), press -> parent.action(id), ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.id = id;
        this.parent = parent;
        this.isNext = isNext;
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            int texWidth = 8;
            int texHeight = 14;
            int u = 256 - texWidth * 2;
            if (isNext) {
                u = (256 - texWidth * 2) + texWidth;
            }
            int v = 0;
            if (mouseX >= getX() && mouseX <= getX() + width && mouseY >= getY() && mouseY <= getY() + height) {
                v = texHeight;
            }
            if (!active) {
                v = texHeight * 2;
            }
            guiGraphics.setShaderColor(1, 1, 1, 1);
            guiGraphics.drawTexture(parent.TRAVEL_TEX, getX(), getY(), u, v, width, height);
        }
    }
}
