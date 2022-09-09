package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public class ScrollBarButton extends Button {

    double clickX, clickY;
    public int startX, startY, top, bottom;
    int scrollBarHeight;
    int minHeight, maxHeight;

    public ScrollBarButton(int buttonId, int x, int y, int widthIn, int minHeight, int top, int bottom) {
        super(x, y, widthIn, minHeight, Component.empty(), button -> {});
        this.top = top;
        this.minHeight = minHeight;
        this.scrollBarHeight = minHeight;
        this.bottom = bottom;
        this.maxHeight = bottom - top;
    }

    public int getBottom() {
        return bottom + scrollBarHeight;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 0.5F);
            fill(stack, x, top, x + width, getBottom(), new Color(0, 0, 0, 0.5F).hashCode());
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
            fill(stack, x, y, x+8, y+scrollBarHeight, new Color(81, 86, 71).hashCode());
            fill(stack, x+1, y+1, x+1+6, y+1+(scrollBarHeight-2), new Color(114, 118, 95).hashCode());
        }
    }

    public void setScrollBarHeight(int height) {
        this.scrollBarHeight = Math.max(this.scrollBarHeight, height);

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (clickX >= x && clickX <= x + width) {
            if (active) {
                if (startY - (clickY - mouseY) >= bottom) {
                    this.y = bottom;
                } else if (startY - (clickY - mouseY) <= top) {
                    this.y = top;
                } else {
                    this.y = (int) (startY - (clickY - mouseY));
                }
            }

        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        clickX = mouseX;
        clickY = mouseY;
        startX = x;
        startY = y;
        if (clickX >= x && clickX <= x + width && visible) {
            playDownSound(Minecraft.getInstance().getSoundManager());
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        int scrollFactor = 5;
        if (scrollDelta > 0) {
            y = (int) Math.max(y - (scrollDelta * scrollFactor), top);
        }
        if (scrollDelta < 0) {
            y = (int) Math.min(y - (scrollDelta * scrollFactor), bottom);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

}
