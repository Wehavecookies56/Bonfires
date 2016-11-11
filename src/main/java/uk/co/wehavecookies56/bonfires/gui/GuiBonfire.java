package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.Travel;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Toby on 10/11/2016.
 */
public class GuiBonfire extends GuiScreen {

    public final ResourceLocation MENU = new ResourceLocation(Bonfires.modid, "textures/gui/bonfire_menu.png");
    public final ResourceLocation TRAVEL_TEX = new ResourceLocation(Bonfires.modid, "textures/gui/travel_menu.png");

    GuiButton travel, leave, back;

    List<Bonfire> bonfires;

    TileEntityBonfire bonfire;
    public boolean travelOpen;

    public UUID selected;

    public final int TRAVEL = 0, LEAVE = 1, BACK = 2;

    public final int tex_width = 90;
    public final int tex_height = 166;
    public final int travel_width = 195;
    public final int travel_height = 136;

    public GuiBonfire(TileEntityBonfire bonfire) {
        this.bonfire = bonfire;
    }

    public void drawCenteredStringNoShadow(FontRenderer fr, String text, int x, int y, int color) {
        fr.drawString(text, (x - fr.getStringWidth(text) / 2), y, color);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(MENU);
        if (travelOpen) {
            drawTravelMenu(mouseX, mouseY, partialTicks);
            if (selected != null) {
                drawSelectedBonfire(mouseX, mouseY, partialTicks);
            }
        } else {
            drawTexturedModalRect((width / 4) - (tex_width / 2), (height / 2) - (tex_height / 2), 0, 0, tex_width, tex_height);
            drawCenteredStringNoShadow(mc.fontRendererObj, BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()).getName(), (width / 4), (height / 2) - (tex_height / 2) + 6, 4210752);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawSelectedBonfire(int mouseX, int mouseY, float partialTicks) {
        if (selected != null) {
            Bonfire b = BonfireRegistry.INSTANCE.getBonfire(selected);
            if (b != null) {
                drawCenteredStringNoShadow(mc.fontRendererObj, b.getName(), (width / 2), (height / 2), 4210752);
            }
        }
    }

    public void drawTravelMenu(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        Minecraft.getMinecraft().renderEngine.bindTexture(TRAVEL_TEX);
        drawTexturedModalRect((width / 2) - (travel_width / 2), (height / 2) - (travel_height / 2), 0, 0, travel_width, travel_height);
        for (int dim : DimensionManager.getIDs()) {
            //System.out.println(dim);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case TRAVEL:
                if (!travelOpen) {
                    travelOpen = true;
                    updateBonfires(mc.theWorld.provider.getDimension());
                } else {
                    if (selected != null) {
                        PacketDispatcher.sendToServer(new Travel(BonfireRegistry.INSTANCE.getBonfire(selected)));
                        mc.displayGuiScreen(null);
                    }
                }
                break;
            case LEAVE:
                mc.displayGuiScreen(null);
                break;
        }
        updateButtons();
        super.actionPerformed(button);
    }

    public void updateButtons() {
        if (travelOpen) {
            if (selected != null) {
                travel.visible = true;
            }
            leave.visible = false;
        } else {
            travel.visible = true;
            travel.xPosition = (width / 4) - (80 / 2);
            travel.yPosition = (height / 2) - (tex_height / 2) + 20;
            leave.visible = true;
        }
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(travel = new GuiButton(TRAVEL, (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 20, 80, 20, "Travel"));
        buttonList.add(leave = new GuiButton(LEAVE, (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 41, 80, 20, "Leave"));
        if (travelOpen) {
            updateBonfires(0);
            bonfires.forEach((b -> {
                String name = b.getName();
                if (name.length() > 7) {
                    name = name.substring(0, 7) + "...";
                }
                buttonList.add(new GuiButtonBonfire(this, b, 100 + bonfires.indexOf(b), (width / 2) - 85, (height / 2) + (mc.fontRendererObj.FONT_HEIGHT + 2) * bonfires.indexOf(b) - 45, name));
            }));
        }
        updateButtons();
        super.initGui();
    }

    public void updateBonfires(int dimension) {
        bonfires = BonfireRegistry.INSTANCE.getBonfiresByDimension(dimension);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return super.doesGuiPauseGame();
    }
}
