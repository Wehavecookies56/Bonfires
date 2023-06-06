package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import wehavecookies56.bonfires.Bonfires;

import java.util.ArrayList;
import java.util.List;

public class BonfireCustomButton extends Button {

    private int id;
    private final ResourceLocation TRAVEL_TEX = new ResourceLocation(Bonfires.modid, "textures/gui/travel_menu.png");

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
    public BonfireCustomButton(int buttonId, int x, int y, ButtonType type, OnPress onPress) {
        super(new Builder(Component.empty(), onPress).pos(x, y).size(16, 16));
        this.id = buttonId;
        this.type = type;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            if (mouseX >= getX() && mouseX <= getX() + width && mouseY >= getY() && mouseY <= getY() + height) {
                RenderSystem.setShaderColor(1, 1, 1, 1);
            } else {
                RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1F);
            }
            RenderSystem.setShaderTexture(0, TRAVEL_TEX);
            blit(stack, getX(), getY(), type.u, type.v, width, height);
            if (mouseX >= getX() && mouseX <= getX() + width && mouseY >= getY() && mouseY <= getY() + height) {
                List<FormattedCharSequence> lines = new ArrayList<>();
                lines.add(Component.translatable(type.translationKey).getVisualOrderText());
                Minecraft.getInstance().screen.renderTooltip(stack, lines, mouseX, mouseY);
            }
        }
    }
}
