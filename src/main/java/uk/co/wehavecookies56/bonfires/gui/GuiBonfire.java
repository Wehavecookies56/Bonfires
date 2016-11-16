package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.Sys;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.LocalStrings;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.Travel;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

import java.io.IOException;
import java.util.*;

/**
 * Created by Toby on 10/11/2016.
 */
public class GuiBonfire extends GuiScreen {

    public final ResourceLocation MENU = new ResourceLocation(Bonfires.modid, "textures/gui/bonfire_menu.png");
    public final ResourceLocation TRAVEL_TEX = new ResourceLocation(Bonfires.modid, "textures/gui/travel_menu.png");

    GuiButton travel, leave, back;

    Map<Integer, List<Bonfire>> bonfires;

    List<Integer> buttonIDs;

    int dimTabSelected = 0;

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
        drawDefaultBackground();
        Minecraft.getMinecraft().renderEngine.bindTexture(MENU);
        if (travelOpen) {
            drawTravelMenu(mouseX, mouseY, partialTicks);
            fontRendererObj.drawString(DimensionManager.getProvider(dimTabSelected).getDimensionType().getName(), (width / 2) - 88, (height / 2) - 62, 1184274);
            if (selected != null) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                drawSelectedBonfire(mouseX, mouseY, partialTicks);
            } else {
                super.drawScreen(mouseX, mouseY, partialTicks);
            }
        } else {
            drawTexturedModalRect((width / 4) - (tex_width / 2), (height / 2) - (tex_height / 2), 0, 0, tex_width, tex_height);
            super.drawScreen(mouseX, mouseY, partialTicks);
            drawCenteredStringNoShadow(mc.fontRendererObj, BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()).getName(), (width / 4), (height / 2) - (tex_height / 2) + 6, 4210752);
        }
    }

    public void drawSelectedBonfire(int mouseX, int mouseY, float partialTicks) {
        if (selected != null) {
            Bonfire b = BonfireRegistry.INSTANCE.getBonfire(selected);
            if (b != null) {
                fontRendererObj.drawString(b.getName(), (width / 2) - 25, (height / 2) - 45, 4210752);
                fontRendererObj.drawString("X:" + b.getPos().getX() + " Y:" + b.getPos().getY() + " Z:" + b.getPos().getZ(), (width / 2) - 25, (height / 2) - 35, 4210752);
                if (mouseX >= (width / 2) - 25 && mouseX <= (width / 2) - 25 + fontRendererObj.getStringWidth(b.getName()) && mouseY >= (height / 2) - 45 && mouseY <= (height / 2) - 45 + fontRendererObj.FONT_HEIGHT) {
                    List<String> lines = new ArrayList<>();
                    lines.add("ID: " + b.getId());
                    drawHoveringText(lines, mouseX, mouseY);
                }
            }
        }
    }

    public void drawTravelMenu(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TRAVEL_TEX);
        drawTexturedModalRect((width / 2) - (travel_width / 2), (height / 2) - (travel_height / 2), 0, 0, travel_width, travel_height);
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
                travel.xPosition = (width / 2) - 20;
                travel.yPosition = (height / 2) + 38;
            } else {
                travel.visible = false;
            }
            leave.visible = false;
        } else {
            travel.visible = true;
            travel.xPosition = (width / 4) - (80 / 2);
            travel.yPosition = (height / 2) - (tex_height / 2) + 20;
            leave.visible = true;
        }
    }

    public int addButton(int id) {
        buttonIDs.add(id);
        return id;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        bonfires = new HashMap<>();
        buttonIDs = new ArrayList<>();
        buttonList.add(travel = new GuiButton(addButton(TRAVEL), (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 20, 80, 20, I18n.format(LocalStrings.BUTTON_TRAVEL)));
        buttonList.add(leave = new GuiButton(addButton(LEAVE), (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 41, 80, 20, I18n.format(LocalStrings.BUTTON_LEAVE)));
        for (int dim : DimensionManager.getIDs()) {
            updateBonfires(dim);
            bonfires.get(dim).forEach((b -> {
                String name = b.getName();
                if (name.length() > 7) {
                    name = name.substring(0, 7) + "...";
                }
                buttonList.add(new GuiButtonBonfire(this, b, addButton(buttonIDs.size() + 1), (width / 2) - 85, (height / 2) + (mc.fontRendererObj.FONT_HEIGHT + 2) * bonfires.get(dim).indexOf(b) - 45, name, 0));
            }));
        }
        updateButtons();
        super.initGui();
    }

    public void updateBonfires(int dimension) {
        bonfires.put(dimension, BonfireRegistry.INSTANCE.getBonfiresByDimension(dimension));
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
        return false;
    }
}
