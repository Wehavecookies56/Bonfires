package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;

import java.awt.*;

public class GuiButtonCheckBox extends AbstractButton {

    private boolean checked;

    public GuiButtonCheckBox(int x, int y, String buttonText, boolean checked) {
        super(x, y, 10, 10, Component.translatable(buttonText));
        this.checked = checked;
    }

    @Override
    public void onPress() {
        this.checked = !checked;
    }

    private final ResourceLocation TEXTURE = new ResourceLocation(Bonfires.modid, "textures/gui/checkbox.png");

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            guiGraphics.blit(TEXTURE, getX(), getY(), 0, 0, 10, 10);
            if (checked) {
                guiGraphics.blit(TEXTURE, getX(), getY(), 10, 0, 10, 10);
            }
            guiGraphics.drawString(Minecraft.getInstance().font, getMessage().getString(), getX() + width + 3, getY() + 2, new Color(255, 255, 255).hashCode());
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 3 + Minecraft.getInstance().font.width(getMessage());
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
        pNarrationElementOutput.add(NarratedElementType.HINT, isChecked() ? Component.translatable(LocalStrings.NARRATION_BUTTON_CHECKBOX_CHECKED) : Component.translatable(LocalStrings.NARRATION_BUTTON_CHECKBOX_UNCHECKED));
    }
}
