package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;

import java.util.ArrayList;
import java.util.List;

public class BonfireCustomButton extends ButtonWidget {

    private int id;
    private final Identifier TRAVEL_TEX = new Identifier(Bonfires.modid, "textures/gui/travel_menu.png");

    public enum ButtonType {
        SCREENSHOT(240, 42, "tooltip.bonfires.screenshot"), INFO(240, 58, "tooltip.bonfires.info");

        int u, v;
        public String translationKey;
        ButtonType(int u, int v, String translationKey) {
            this.u = u;
            this.v = v;
            this.translationKey = translationKey;
        }
    }

    ButtonType type;
    public BonfireCustomButton(int buttonId, int x, int y, ButtonType type, PressAction onPress) {
        super(x, y, 16, 16, Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.id = buttonId;
        this.type = type;
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            guiGraphics.setShaderColor(1, 1, 1, 1);
            if (mouseX >= getX() && mouseX <= getX() + width && mouseY >= getY() && mouseY <= getY() + height) {
                RenderSystem.setShaderColor(1, 1, 1, 1);
            } else {
                RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1F);
            }
            guiGraphics.drawTexture(TRAVEL_TEX, getX(), getY(), type.u, type.v, width, height);
            if (mouseX >= getX() && mouseX <= getX() + width && mouseY >= getY() && mouseY <= getY() + height) {
                List<Text> lines = new ArrayList<>();
                lines.add(Text.translatable(type.translationKey));
                guiGraphics.drawTooltip(MinecraftClient.getInstance().textRenderer, lines, mouseX, mouseY);
            }
        }
    }
}
