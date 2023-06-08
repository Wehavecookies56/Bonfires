package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

/**
 * Created by Toby on 17/12/2016.
 */
public class BonfirePageButton extends Button {

    private BonfireScreen parent;
    private boolean isNext;
    private int id;

    public BonfirePageButton(BonfireScreen parent, int id, int x, int y, boolean isNext) {
        super(new Builder(Component.empty(), press -> {
            parent.action(id);
        }).pos(x, y).size(8, 14));
        this.id = id;
        this.parent = parent;
        this.isNext = isNext;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
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
            guiGraphics.setColor(1, 1, 1, 1);
            guiGraphics.blit(parent.TRAVEL_TEX, getX(), getY(), u, v, width, height);
        }
    }
}
