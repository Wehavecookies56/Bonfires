package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

/**
 * Created by Toby on 17/12/2016.
 */
public class BonfirePageButton extends Button {

    private BonfireScreen parent;
    private boolean isNext;
    private int id;

    public BonfirePageButton(BonfireScreen parent, int id, int x, int y, boolean isNext) {
        super(x, y, 8, 14, new TranslationTextComponent(""), press -> {
            parent.action(id);
        });
        this.id = id;
        this.parent = parent;
        this.isNext = isNext;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            Minecraft.getInstance().textureManager.bind(parent.TRAVEL_TEX);
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
            RenderSystem.pushMatrix();
            RenderSystem.color4f(1, 1, 1, 1);
            blit(stack, x, y, u, v, width, height);
            RenderSystem.popMatrix();
        }
    }
}
