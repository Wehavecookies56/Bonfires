package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
    public BonfireCustomButton(int buttonId, int x, int y, ButtonType type, IPressable onPress) {
        super(x, y, 16, 16, new StringTextComponent(""), onPress);
        this.id = buttonId;
        this.type = type;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            RenderSystem.color4f(1, 1, 1, 1);
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                RenderSystem.color4f(1, 1, 1, 1);
            } else {
                RenderSystem.color4f(0.8F, 0.8F, 0.8F, 1F);
            }
            Minecraft.getInstance().textureManager.bind(TRAVEL_TEX);
            blit(stack, x, y, type.u, type.v, width, height);
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                List<IReorderingProcessor> lines = new ArrayList<>();
                lines.add(new TranslationTextComponent(type.translationKey).getVisualOrderText());
                Minecraft.getInstance().screen.renderTooltip(stack, lines, mouseX, mouseY);
            }
        }
    }
}
