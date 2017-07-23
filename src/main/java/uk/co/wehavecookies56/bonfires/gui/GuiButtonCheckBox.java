package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.bonfires.Bonfires;

import java.awt.*;

public class GuiButtonCheckBox extends GuiButton {

    boolean checked;

    public GuiButtonCheckBox(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, 10, 10, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            mc.renderEngine.bindTexture(new ResourceLocation(Bonfires.modid, "textures/gui/checkbox.png"));
            drawTexturedModalRect(x, y, 0, 0, 10, 10);
            if (checked) {
                drawTexturedModalRect(x, y, 10, 0, 10, 10);
            }
            drawString(mc.fontRenderer, displayString, x + width + 3, y + 2, new Color(255, 255, 255).hashCode());
        }
    }

    @Override
    public int getButtonWidth() {
        return super.getButtonWidth() + 3 + Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString);
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (enabled && visible) {
            if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.getButtonWidth() && mouseY < this.y + this.height) {
                checked = !checked;
            }
            return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.getButtonWidth() && mouseY < this.y + this.height;
        }
        return false;
    }
}
