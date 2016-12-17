package uk.co.wehavecookies56.bonfires.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.LocalStrings;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.Travel;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Toby on 10/11/2016.
 */
public class GuiBonfire extends GuiScreen {

    public final ResourceLocation MENU = new ResourceLocation(Bonfires.modid, "textures/gui/bonfire_menu.png");
    public final ResourceLocation TRAVEL_TEX = new ResourceLocation(Bonfires.modid, "textures/gui/travel_menu.png");

    GuiButton travel, leave, back, next, prev;

    Map<Integer, List<List<Bonfire>>> bonfires;

    List<Integer> buttonIDs;
    List<Integer> dimensions;
    List<List<Integer>> pages;

    int currentPage = 0;
    int bonfirePage = 0;

    TileEntityBonfire bonfire;
    public boolean travelOpen;

    public final int TRAVEL = 0, LEAVE = 1, BACK = 2, NEXT = 3, PREV = 4, TAB1 = 5, TAB2 = 6, TAB3 = 7, TAB4 = 8, TAB5 = 9, TAB6 = 10, BONFIRE1 = 11, BONFIRE2 = 12, BONFIRE3 = 13, BONFIRE4 = 14, BONFIRE5 = 15, BONFIRE6 = 16, BONFIRE7 = 17, BONFIRE_NEXT = 18, BONFIRE_PREV = 19;

    int dimTabSelected = TAB1;
    int bonfireSelected = 0;

    GuiButtonDimensionTab[] tabs;
    GuiButtonBonfire[] bonfireButtons;
    GuiButtonBonfirePage bonfire_next, bonfire_prev;

    public final int tex_width = 90;
    public final int tex_height = 166;
    public final int travel_width = 195;
    public final int travel_height = 136;

    public GuiBonfire(TileEntityBonfire bonfire) {
        this.bonfire = bonfire;
    }

    public void drawCenteredStringNoShadow(FontRenderer fr, String text, int x, int y, int color) {
        fr.drawString(text, (x - (fr.getStringWidth(text) / 2)), (y - (fr.FONT_HEIGHT / 2)), color);
    }

    public static Map<Integer, List<List<Bonfire>>> createSeries(int dimension) {
        List<Bonfire> bonfires = BonfireRegistry.INSTANCE.getBonfiresByDimension(dimension);

        if (!bonfires.isEmpty()) {
            List<List<Bonfire>> book = new ArrayList<>();

            int plus = 1;
            if (bonfires.size() < 7) {
                plus = bonfires.size();
            }

            for (int i = 0; i < (bonfires.size() / 7) + plus; i++) {
                List<Bonfire> page;
                int start = i * 7;
                if (bonfires.size() < 7)
                    start = 0;
                if ((start) + 6 > bonfires.size())
                    page = bonfires.subList(start, bonfires.size());
                else
                    page = bonfires.subList(start, (start) + 7);
                book.add(page);
            }
            Map<Integer, List<List<Bonfire>>> series = new HashMap<>();
            series.put(dimension, book);
            return series;
        } else {
            return null;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(MENU);
        if (travelOpen) {
            drawTravelMenu(mouseX, mouseY, partialTicks);
            try {
                Field dimensionsF = ReflectionHelper.findField(DimensionManager.class, "dimensions");
                dimensionsF.setAccessible(true);
                Object dimIDs = dimensionsF.get(new DimensionManager());
                Hashtable<Integer, DimensionType> dimensionIDs = (Hashtable<Integer, DimensionType>)dimIDs ;
                fontRendererObj.drawString(dimensionIDs.get(tabs[dimTabSelected - 5].getDimension()).getName() + " (" + tabs[dimTabSelected - 5].getDimension() + ")", (width / 2) - 88, (height / 2) - 62, 1184274);
                dimensionsF.setAccessible(false);
            } catch (IllegalAccessException e) {

            }

            if (bonfireSelected >= BONFIRE1) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                drawSelectedBonfire(mouseX, mouseY, partialTicks);
            } else {
                super.drawScreen(mouseX, mouseY, partialTicks);
            }
            String pages = "0/0";
            if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                pages = (bonfirePage + 1) + "/" + bonfires.get(tabs[dimTabSelected - 5].getDimension()).size();
            }
            int xZero = (width / 2) - (travel_width / 2) + 16;
            int yZero = (height / 2) - (travel_height / 2) + 128 - 17;
            drawString(fontRendererObj, pages, xZero + (55 / 2) - fontRendererObj.getStringWidth(pages) / 2, yZero + (14 / 2) - fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);
        } else {
            drawTexturedModalRect((width / 4) - (tex_width / 2), (height / 2) - (tex_height / 2), 0, 0, tex_width, tex_height);
            super.drawScreen(mouseX, mouseY, partialTicks);
            String name = "";
            if (BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()) != null) {
                name = BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()).getName();
            }
            drawCenteredStringNoShadow(mc.fontRendererObj, name, (width / 4), (height / 2) - (tex_height / 2) + 10, 4210752);
        }
        GL11.glPopMatrix();
    }

    public void drawSelectedBonfire(int mouseX, int mouseY, float partialTicks) {
        if (bonfireSelected >= BONFIRE1) {
            if (bonfires != null) {
                if (bonfires.get(tabs[dimTabSelected-5].getDimension()) != null) {
                    Bonfire b = bonfires.get(tabs[dimTabSelected-5].getDimension()).get(bonfirePage).get(bonfireSelected-11);
                    if (b != null) {
                        int nameX = (width / 2) - 10;
                        int nameY = (height / 2) - 45;
                        int nameEndX = nameX + fontRendererObj.getStringWidth(b.getName());
                        int nameEndY = nameY + fontRendererObj.FONT_HEIGHT;
                        fontRendererObj.drawString(b.getName(), nameX, nameY, 4210752);
                        fontRendererObj.drawString("X:" + b.getPos().getX() + " Y:" + b.getPos().getY() + " Z:" + b.getPos().getZ(), nameX, nameY + 12, 4210752);
                        if (mouseX >= nameX && mouseX <= nameEndX && mouseY >= nameY && mouseY <= nameEndY) {
                            List<String> lines = new ArrayList<>();
                            lines.add("ID: " + b.getId());
                            drawHoveringText(lines, mouseX, mouseY);
                        }
                    }
                }
            }
        }
    }

    public void drawHovering(List<String> lines, int x, int y) {
        drawHoveringText(lines, x, y);
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
                    updateBonfires();
                } else {
                    if (bonfireSelected >= BONFIRE1) {
                        if (bonfires != null) {
                            if (bonfires.get(tabs[dimTabSelected-5].getDimension()) != null) {
                                Bonfire b = bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage).get(bonfireSelected - 11);
                                PacketDispatcher.sendToServer(new Travel(b));
                                mc.displayGuiScreen(null);
                            }
                        }
                    }
                }
                break;
            case LEAVE:
                mc.displayGuiScreen(null);
                break;
            case NEXT:
                if (currentPage != pages.size()-1) {
                    currentPage++;
                    dimTabSelected = TAB1;
                    bonfireSelected = 0;
                }
                break;
            case PREV:
                if (currentPage != 0) {
                    currentPage--;
                    dimTabSelected = TAB1;
                    bonfireSelected = 0;
                }
                break;
            case BONFIRE_NEXT:
                if (bonfirePage != bonfires.get(tabs[dimTabSelected - 5].getDimension()).size()-1) {
                    bonfirePage++;
                    bonfireSelected = 0;
                }
                break;
            case BONFIRE_PREV:
                if (bonfirePage != 0) {
                    bonfirePage--;
                    bonfireSelected = 0;
                }
                break;
            case TAB1:
                dimTabSelected = TAB1;
                bonfireSelected = 0;
                bonfirePage = 0;
                break;
            case TAB2:
                dimTabSelected = TAB2;
                bonfireSelected = 0;
                bonfirePage = 0;
                break;
            case TAB3:
                dimTabSelected = TAB3;
                bonfireSelected = 0;
                bonfirePage = 0;
                break;
            case TAB4:
                dimTabSelected = TAB4;
                bonfireSelected = 0;
                bonfirePage = 0;
                break;
            case TAB5:
                dimTabSelected = TAB5;
                bonfireSelected = 0;
                bonfirePage = 0;
                break;
            case TAB6:
                dimTabSelected = TAB6;
                bonfireSelected = 0;
                bonfirePage = 0;
                break;
            case BONFIRE1:
                bonfireSelected = BONFIRE1;
                break;
            case BONFIRE2:
                bonfireSelected = BONFIRE2;
                break;
            case BONFIRE3:
                bonfireSelected = BONFIRE3;
                break;
            case BONFIRE4:
                bonfireSelected = BONFIRE4;
                break;
            case BONFIRE5:
                bonfireSelected = BONFIRE5;
                break;
            case BONFIRE6:
                bonfireSelected = BONFIRE6;
                break;
            case BONFIRE7:
                bonfireSelected = BONFIRE7;
                break;
        }
        updateButtons();
        super.actionPerformed(button);
    }

    public void updateButtons() {
        for (GuiButtonDimensionTab tab : tabs) {
            tab.visible = false;
        }
        if (travelOpen) {
            if (bonfireSelected >= BONFIRE1) {
                travel.visible = true;
                travel.xPosition = (width / 2) - 5;
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
            for (int i = 0; i < bonfireButtons.length; i++) {
                if (tabs[dimTabSelected-5] != null) {
                    if (bonfires != null) {
                        if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                            if (i < bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage).size()) {
                                bonfireButtons[i].visible = true;
                                bonfireButtons[i].setBonfire(bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage).get(i));
                            } else {
                                bonfireButtons[i].visible = false;
                            }
                        } else {
                            bonfireButtons[i].visible = false;
                        }
                    }
                }
            }
            leave.visible = false;
            next.visible = true;
            prev.visible = true;
            bonfire_prev.visible = true;
            bonfire_next.visible = true;
            if (currentPage == 0)
                prev.enabled = false;
            else
                prev.enabled = true;
            if (currentPage == pages.size()-1)
                next.enabled = false;
            else
                next.enabled = true;
            if (bonfirePage == 0)
                bonfire_prev.enabled = false;
            else
                bonfire_prev.enabled = true;
            if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null)
                if (bonfirePage == bonfires.get(tabs[dimTabSelected - 5].getDimension()).size()-1)
                    bonfire_next.enabled = false;
                else
                    bonfire_next.enabled = true;
            else
                bonfire_next.enabled = false;

        } else {
            bonfire_prev.visible = false;
            bonfire_prev.enabled = false;
            bonfire_next.visible = false;
            bonfire_next.enabled = false;
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
            for (GuiButtonBonfire bonfire : bonfireButtons) {
                bonfire.visible = false;
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
        buttonIDs = new ArrayList<>();
        dimensions = new ArrayList<>();
        pages = new ArrayList<>();
        buttonList.add(travel = new GuiButton(addButton(TRAVEL), (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 20, 80, 20, I18n.format(LocalStrings.BUTTON_TRAVEL)));
        buttonList.add(leave = new GuiButton(addButton(LEAVE), (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 41, 80, 20, I18n.format(LocalStrings.BUTTON_LEAVE)));
        buttonList.add(next = new GuiButton(addButton(NEXT), 0, 0, 20, 20, ">"));
        buttonList.add(prev = new GuiButton(addButton(PREV), 20, 0, 20, 20, "<"));
        buttonList.add(bonfire_next = new GuiButtonBonfirePage(this, BONFIRE_NEXT, 0, 0, true));
        buttonList.add(bonfire_prev = new GuiButtonBonfirePage(this, BONFIRE_PREV, 8, 0, false));
        tabs = new GuiButtonDimensionTab[] {
                new GuiButtonDimensionTab(this, addButton(TAB1), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB2), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB3), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB4), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB5), 0, 0),
                new GuiButtonDimensionTab(this, addButton(TAB6), 0, 0)
        };
        bonfireButtons = new GuiButtonBonfire[] {
                new GuiButtonBonfire(this, addButton(BONFIRE1), 0, 0),
                new GuiButtonBonfire(this, addButton(BONFIRE2), 0, 0),
                new GuiButtonBonfire(this, addButton(BONFIRE3), 0, 0),
                new GuiButtonBonfire(this, addButton(BONFIRE4), 0, 0),
                new GuiButtonBonfire(this, addButton(BONFIRE5), 0, 0),
                new GuiButtonBonfire(this, addButton(BONFIRE6), 0, 0),
                new GuiButtonBonfire(this, addButton(BONFIRE7), 0, 0)
        };
        for (int i = 0; i < tabs.length; i++) {
            buttonList.add(tabs[i]);
            int sixTabs = 6 * 28;
            int gap = travel_width - sixTabs;
            tabs[i].xPosition = ((width) / 2 - (travel_width / 2) + (i * 28) + gap / 2);
            tabs[i].yPosition = (height / 2) - (travel_width / 2) + 1;
        }
        for (int i = 0; i < bonfireButtons.length; i++) {
            buttonList.add(bonfireButtons[i]);
            bonfireButtons[i].xPosition = (width / 2) - 88;
            bonfireButtons[i].yPosition = (height / 2) + (bonfireButtons[i].height) * i - 50;
        }
        prev.xPosition = ((width) / 2 - (travel_width / 2)) - 8;
        prev.yPosition = (height / 2) - (travel_width / 2) + 6;
        int sixTabs = 6 * 28;
        int gap = travel_width - sixTabs;
        next.xPosition = ((width) / 2 - (travel_width / 2) + (6 * 28) + gap / 2);
        next.yPosition = (height / 2) - (travel_width / 2) + 6;
        bonfire_prev.xPosition = (width / 2) - (travel_width / 2) + 16;
        bonfire_prev.yPosition = (height / 2) - (travel_height / 2) + 128 - 17;
        bonfire_next.xPosition = (width / 2) - (travel_width / 2) + 63;
        bonfire_next.yPosition = (height / 2) - (travel_height / 2) + 128 - 17;
        updateBonfires();
        try {
            Field dimensionsF = ReflectionHelper.findField(DimensionManager.class, "dimensions");
            dimensionsF.setAccessible(true);
            Object dimIDs = dimensionsF.get(new DimensionManager());
            Hashtable<Integer, DimensionType> dimensionIDs = (Hashtable<Integer, DimensionType>)dimIDs ;
            List<Integer> dimList = new ArrayList<>(dimensionIDs.keySet());
            dimList = Lists.reverse(dimList);
            for (int dim : dimList) {
                dimensions.add(dim);
            }
            dimensionsF.setAccessible(false);
        } catch (IllegalAccessException e) {

        }
        for (int i = 0; i < (dimensions.size() / 6)+1; i++) {
            if ((i*6)+5 > dimensions.size())
                pages.add(dimensions.subList(i*6, dimensions.size()));
            else {
                pages.add(dimensions.subList(i * 6, (i * 6) + 6));
            }
        }
        updateButtons();
        super.initGui();
    }

    public void updateBonfires() {
        bonfires = new HashMap<>();
        try {
            Field dimensionsF = ReflectionHelper.findField(DimensionManager.class, "dimensions");
            dimensionsF.setAccessible(true);
            Object dimIDs = dimensionsF.get(new DimensionManager());
            Hashtable<Integer, DimensionType> dimensionIDs = (Hashtable<Integer, DimensionType>)dimIDs ;
            for (int dim : dimensionIDs.keySet()) {
                Map<Integer, List<List<Bonfire>>> series = createSeries(dim);
                if (series != null)
                    if (series.get(dim) != null)
                        bonfires.put(dim, series.get(dim));
            }
            dimensionsF.setAccessible(false);
        } catch (IllegalAccessException e) {

        }
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
