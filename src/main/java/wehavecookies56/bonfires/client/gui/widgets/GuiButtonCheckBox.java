package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;

import java.awt.Color;

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

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            RenderSystem.setShaderTexture(0, new ResourceLocation(Bonfires.modid, "textures/gui/checkbox.png"));
            blit(stack, getX(), getY(), 0, 0, 10, 10);
            if (checked) {
                blit(stack, getX(), getY(), 10, 0, 10, 10);
            }
            drawString(stack, Minecraft.getInstance().font, getMessage().getString(), getX() + width + 3, getY() + 2, new Color(255, 255, 255).hashCode());
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
