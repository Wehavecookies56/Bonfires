package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

import java.awt.Color;

/**
 * Created by Toby on 10/11/2016.
 */
public class BonfireButton extends ExtendedButton {

    private BonfireScreen parent;
    private Bonfire bonfire;

    public BonfireButton(BonfireScreen parent, int id, int x, int y) {
        super(x, y, 93, Minecraft.getInstance().font.lineHeight+4, new TextComponent(""), button -> {
            parent.action(id);
        });
        this.parent = parent;
    }

    public Bonfire getBonfire() {
        return bonfire;
    }

    public void setBonfire(Bonfire bonfire) {
        this.bonfire = bonfire;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (bonfire != null) {
            if (bonfire.getDimension() == parent.tabs[parent.dimTabSelected-5].getDimension()) {
                setMessage(new TranslatableComponent(bonfire.getName()));
                if (visible) {
                    Font fontrenderer = Minecraft.getInstance().font;
                    int colour = new Color(255, 255, 255).hashCode();
                    if (parent.bonfireSelected >= parent.BONFIRE1) {
                        if (parent.bonfires != null) {
                            if (parent.bonfires.get(parent.tabs[parent.dimTabSelected - 5].getDimension()) != null) {
                                if (parent.bonfires.get(parent.tabs[parent.dimTabSelected - 5].getDimension()).get(parent.bonfirePage) != null) {
                                    if (parent.bonfires.get(parent.tabs[parent.dimTabSelected - 5].getDimension()).get(parent.bonfirePage).get(parent.bonfireSelected - 11) != null) {
                                        Bonfire b = parent.bonfires.get(parent.tabs[parent.dimTabSelected - 5].getDimension()).get(parent.bonfirePage).get(parent.bonfireSelected - 11);
                                        if (bonfire == b) {
                                            colour = 46339;
                                            fill(stack, x, y, x + width, y + height, 0xFF777777);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                        fill(stack, x, y, x + width, y + height, 0xFF777777);
                    }
                    parent.drawCenteredStringNoShadow(stack, fontrenderer, getMessage().getString(), this.x + width / 2, this.y + height / 2, colour);
                }
            }
        } else {
            visible = false;
            setMessage(new TextComponent(""));
        }
    }

}
