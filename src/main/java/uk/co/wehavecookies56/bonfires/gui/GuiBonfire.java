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

    GuiButton travel, leave, back, next, prev;

    GuiButtonDimensionTab tab1, tab2, tab3, tab4, tab5, tab6;

    Map<Integer, List<Bonfire>> bonfires;

    List<Integer> buttonIDs;
    List<Integer> dimensions;
    List<List<Integer>> pages;

    int currentPage = 0;

    TileEntityBonfire bonfire;
    public boolean travelOpen;

    public UUID selected;

    public final int TRAVEL = 0, LEAVE = 1, BACK = 2, NEXT = 3, PREV = 4, TAB1 = 5, TAB2 = 6, TAB3 = 7, TAB4 = 8, TAB5 = 9, TAB6 = 10;

    int dimTabSelected = TAB1;

    GuiButtonDimensionTab[] tabs;

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
            fontRendererObj.drawString(DimensionManager.getProvider(tabs[dimTabSelected-5].getDimension()).getDimensionType().getName(), (width / 2) - 88, (height / 2) - 62, 1184274);
            if (selected != null) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                drawSelectedBonfire(mouseX, mouseY, partialTicks);
            } else {
                super.drawScreen(mouseX, mouseY, partialTicks);
            }
        } else {
            drawTexturedModalRect((width / 4) - (tex_width / 2), (height / 2) - (tex_height / 2), 0, 0, tex_width, tex_height);
            super.drawScreen(mouseX, mouseY, partialTicks);
            String name = "";
            if (BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()) != null) {
                name = BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()).getName();
            }
            drawCenteredStringNoShadow(mc.fontRendererObj, name, (width / 4), (height / 2) - (tex_height / 2) + 6, 4210752);
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
        for (GuiButtonDimensionTab tab : tabs) {
            tab.drawButton(mc, mouseX, mouseY);
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(TRAVEL_TEX);
        drawTexturedModalRect((width / 2) - (travel_width / 2), (height / 2) - (travel_height / 2), 0, 0, travel_width, travel_height);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case TRAVEL:
                if (!travelOpen) {
                    travelOpen = true;
                    updateBonfires(tabs[dimTabSelected-5].getDimension());
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
            case NEXT:
                if (currentPage != pages.size()-1)
                    currentPage++;
                break;
            case PREV:
                if (currentPage != 0)
                    currentPage--;
                break;
            case TAB1:
                dimTabSelected = TAB1;
                break;
            case TAB2:
                dimTabSelected = TAB2;
                break;
            case TAB3:
                dimTabSelected = TAB3;
                break;
            case TAB4:
                dimTabSelected = TAB4;
                break;
            case TAB5:
                dimTabSelected = TAB5;
                break;
            case TAB6:
                dimTabSelected = TAB6;
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
            for (int i = 0; i < tabs.length; i++) {
                if (i < pages.get(currentPage).size()) {
                    tabs[i].visible = true;
                    tabs[i].setDimension(pages.get(currentPage).get(i));
                }
            }
            leave.visible = false;
            next.visible = true;
            prev.visible = true;
            if (currentPage == 0)
                prev.enabled = false;
            else
                prev.enabled = true;
            if (currentPage == pages.size()-1)
                next.enabled = false;
            else
                next.enabled = true;
        } else {
            travel.visible = true;
            travel.xPosition = (width / 4) - (80 / 2);
            travel.yPosition = (height / 2) - (tex_height / 2) + 20;
            leave.visible = true;
            next.visible = false;
            prev.visible = false;
            prev.enabled = false;
            next.enabled = false;
            for (GuiButtonDimensionTab tab : tabs) {
                tab.visible = false;
            }
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
        dimensions = new ArrayList<>();
        pages = new ArrayList<>();
        buttonList.add(travel = new GuiButton(addButton(TRAVEL), (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 20, 80, 20, I18n.format(LocalStrings.BUTTON_TRAVEL)));
        buttonList.add(leave = new GuiButton(addButton(LEAVE), (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 41, 80, 20, I18n.format(LocalStrings.BUTTON_LEAVE)));
        buttonList.add(next = new GuiButton(addButton(NEXT), 0, 0, 20, 20, ">"));
        buttonList.add(prev = new GuiButton(addButton(PREV), 20, 0, 20, 20, "<"));
        tabs = new GuiButtonDimensionTab[] {
                new GuiButtonDimensionTab(this, addButton(TAB1), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB2), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB3), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB4), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB5), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB6), 0, 0)
        };
        for (int i = 0; i < tabs.length; i++) {
            buttonList.add(tabs[i]);
            tabs[i].xPosition = ((width) / 2 - (travel_width / 2) + (i * 28)) + ((travel_width - 28) / 2);
            tabs[i].yPosition = (height / 2) - (travel_width / 2);
        }
        for (int dim : DimensionManager.getIDs()) {
            updateBonfires(dim);
            bonfires.get(dim).forEach((b -> {
                String name = b.getName();
                if (name.length() > 10) {
                    name = name.substring(0, 10) + "...";
                }
                buttonList.add(new GuiButtonBonfire(this, b, addButton(buttonIDs.size() + 1), (width / 2) - 85, (height / 2) + (mc.fontRendererObj.FONT_HEIGHT + 2) * bonfires.get(dim).indexOf(b) - 45, name, 0));
            }));
            dimensions.add(dim);
        }
        for (int i = 0; i < (dimensions.size() / 6)+1; i++) {
            if ((i*6)+5 > dimensions.size())
                pages.add(dimensions.subList(i*6, dimensions.size()));
            else
                pages.add(dimensions.subList(i*6, (i*6)+5));
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
