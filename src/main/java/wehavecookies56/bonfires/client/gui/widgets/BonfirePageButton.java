package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

/**
 * Created by Toby on 17/12/2016.
 */
public class BonfirePageButton extends Button {

    private BonfireScreen parent;
    private boolean isNext;
    private int id;

    public BonfirePageButton(BonfireScreen parent, int id, int x, int y, boolean isNext) {
        super(x, y, 8, 14, new TextComponent(""), press -> {
            parent.action(id);
        });
        this.id = id;
        this.parent = parent;
        this.isNext = isNext;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            RenderSystem.setShaderTexture(0, parent.TRAVEL_TEX);
            int texWidth = 8;
            int texHeight = 14;
            int u = 256 - texWidth * 2;
            if (isNext) {
                u = (256 - texWidth * 2) + texWidth;
            }
            int v = 0;
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                v = texHeight;
            }
            if (!active) {
                v = texHeight * 2;
            }
            RenderSystem.setShaderColor(1, 1, 1, 1);
            blit(stack, x, y, u, v, width, height);
        }
    }
}
