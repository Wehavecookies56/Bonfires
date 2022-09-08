package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import wehavecookies56.bonfires.Bonfires;

import java.awt.*;

public class GuiButtonCheckBox extends AbstractButton {

    private boolean checked;

    public GuiButtonCheckBox(int x, int y, String buttonText, boolean checked) {
        super(x, y, 10, 10, new TranslationTextComponent(buttonText));
        this.checked = checked;
    }

    @Override
    public void onPress() {
        this.checked = !checked;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            Minecraft.getInstance().textureManager.bind(new ResourceLocation(Bonfires.modid, "textures/gui/checkbox.png"));
            blit(stack, x, y, 0, 0, 10, 10);
            if (checked) {
                blit(stack, x, y, 10, 0, 10, 10);
            }
            drawString(stack, Minecraft.getInstance().font, getMessage().getString(), x + width + 3, y + 2, new Color(255, 255, 255).hashCode());
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
}
