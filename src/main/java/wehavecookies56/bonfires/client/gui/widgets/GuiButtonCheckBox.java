package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;

import java.awt.*;

public class GuiButtonCheckBox extends PressableWidget {

    private boolean checked;

    public GuiButtonCheckBox(int x, int y, String buttonText, boolean checked) {
        super(x, y, 10, 10, Text.translatable(buttonText));
        this.checked = checked;
    }

    @Override
    public void onPress() {
        this.checked = !checked;
    }

    private final Identifier TEXTURE = new Identifier(Bonfires.modid, "textures/gui/checkbox.png");

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            guiGraphics.drawTexture(TEXTURE, getX(), getY(), 0, 0, 10, 10);
            if (checked) {
                guiGraphics.drawTexture(TEXTURE, getX(), getY(), 10, 0, 10, 10);
            }
            guiGraphics.drawText(MinecraftClient.getInstance().textRenderer, getMessage().getString(), getX() + width + 3, getY() + 2, new Color(255, 255, 255).hashCode(), true);
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 3 + MinecraftClient.getInstance().textRenderer.getWidth(getMessage());
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
        builder.put(NarrationPart.HINT, isChecked() ? Text.translatable(LocalStrings.NARRATION_BUTTON_CHECKBOX_CHECKED) : Text.translatable(LocalStrings.NARRATION_BUTTON_CHECKBOX_UNCHECKED));
    }
}
