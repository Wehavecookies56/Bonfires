package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.Sys;
import uk.co.wehavecookies56.bonfires.Bonfire;

/**
 * Created by Toby on 10/11/2016.
 */
public class GuiButtonBonfire extends GuiButton {

    GuiBonfire parent;
    Bonfire bonfire;

    public GuiButtonBonfire(GuiBonfire parent, int buttonId, int x, int y) {
        super(buttonId, x, y, 69, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2, "");
        this.parent = parent;
    }

    public Bonfire getBonfire() {
        return bonfire;
    }

    public void setBonfire(Bonfire bonfire) {
        this.bonfire = bonfire;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (bonfire != null) {
            if (bonfire.getDimension() == parent.tabs[parent.dimTabSelected-5].getDimension()) {
                displayString = bonfire.getName();
                if (visible) {
                    FontRenderer fontrenderer = mc.fontRendererObj;
                    int colour = 4210752;
                    if (parent.bonfireSelected >= parent.BONFIRE1) {
                        if (parent.bonfires != null) {
                            if (parent.bonfires.get(parent.tabs[parent.dimTabSelected - 5].getDimension()) != null) {
                                Bonfire b = parent.bonfires.get(parent.tabs[parent.dimTabSelected - 5].getDimension()).get(parent.bonfirePage).get(parent.bonfireSelected - 11);
                                if (bonfire == b) {
                                    colour = 46339;
                                }
                            }
                        }
                    }
                    parent.drawCenteredStringNoShadow(fontrenderer, this.displayString, this.xPosition + width / 2, this.yPosition + 1, colour);
                }
            }
        } else {
            visible = false;
            displayString = "";
        }
    }
}
