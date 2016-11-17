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
    int dim;

    public GuiButtonBonfire(GuiBonfire parent, Bonfire bonfire, int buttonId, int x, int y, String buttonText, int dim) {
        super(buttonId, x, y, Minecraft.getMinecraft().fontRendererObj.getStringWidth(buttonText), Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, buttonText);
        this.parent = parent;
        this.bonfire = bonfire;
        this.dim = dim;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (parent.tabs[parent.dimTabSelected-5].getDimension() == dim) {
            if (parent.travelOpen) {
                this.visible = true;
                FontRenderer fontrenderer = mc.fontRendererObj;
                int colour = 4210752;
                if (parent.bonfire.getID().compareTo(bonfire.getId()) == 0) {
                    colour = 46339                                                                                                                                                                                                                                                                                           ;
                }
                fontrenderer.drawString(this.displayString, this.xPosition, this.yPosition, colour);
            } else {
                this.visible = false;
            }
        } else {
            this.visible = false;
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            parent.selected = bonfire.getId();
        }
        return super.mousePressed(mc, mouseX, mouseY);
    }
}
