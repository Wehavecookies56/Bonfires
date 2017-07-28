package uk.co.wehavecookies56.bonfires.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.LocalStrings;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.Travel;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Toby on 10/11/2016.
 */
public class GuiBonfire extends GuiScreen {

    private final ResourceLocation MENU = new ResourceLocation(Bonfires.modid, "textures/gui/bonfire_menu.png");
    final ResourceLocation TRAVEL_TEX = new ResourceLocation(Bonfires.modid, "textures/gui/travel_menu.png");

    private GuiButton travel;
    private GuiButton leave;
    @SuppressWarnings("unused")
    GuiButton back;
    private GuiButton next;
    private GuiButton prev;

    Map<Integer, List<List<Bonfire>>> bonfires;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<Integer> buttonIDs;
    private List<List<Integer>> pages;

    private int currentPage = 0;
    int bonfirePage = 0;

    private final TileEntityBonfire bonfire;
    private boolean travelOpen;

    private final int TRAVEL = 0;
    private final int LEAVE = 1;
    @SuppressWarnings("unused")
    public final int BACK = 2;
    private final int NEXT = 3;
    private final int PREV = 4;
    private final int TAB1 = 5;
    private final int TAB2 = 6;
    private final int TAB3 = 7;
    private final int TAB4 = 8;
    private final int TAB5 = 9;
    private final int TAB6 = 10;
    final int BONFIRE1 = 11;
    private final int BONFIRE2 = 12;
    private final int BONFIRE3 = 13;
    private final int BONFIRE4 = 14;
    private final int BONFIRE5 = 15;
    private final int BONFIRE6 = 16;
    private final int BONFIRE7 = 17;
    private final int BONFIRE_NEXT = 18;
    private final int BONFIRE_PREV = 19;

    int dimTabSelected = TAB1;
    int bonfireSelected = 0;

    GuiButtonDimensionTab[] tabs;
    private GuiButtonBonfire[] bonfireButtons;
    private GuiButtonBonfirePage bonfire_next;
    private GuiButtonBonfirePage bonfire_prev;

    private final int tex_height = 166;
    private final int travel_width = 195;
    final int travel_height = 136;

    public static String ownerName = "";

    GuiBonfire(TileEntityBonfire bonfire) {
        this.bonfire = bonfire;
    }

    void drawCenteredStringNoShadow(FontRenderer fr, String text, int x, int y, int color) {
        fr.drawString(text, (x - (fr.getStringWidth(text) / 2)), (y - (fr.FONT_HEIGHT / 2)), color);
    }

    private static Map<Integer, List<List<Bonfire>>> createSeries(int dimension) {
        List<Bonfire> bonfires = BonfireRegistry.INSTANCE.getPrivateBonfiresByOwnerAndPublicPerDimension(Minecraft.getMinecraft().player.getPersistentID(), dimension);

        if (!bonfires.isEmpty()) {
            List<List<Bonfire>> book = new ArrayList<>();

            int plus = 1;
            if (bonfires.size() % 7 == 0)
                plus = 0;
            for (int i = 0; i < (bonfires.size() / 7) + plus; i++) {
                List<Bonfire> page;
                int start = i * 7;
                if (bonfires.size() < 7)
                    start = 0;
                if ((start) + 7 > bonfires.size())
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

            String dimName = (DimensionManager.getProviderType(tabs[dimTabSelected - 5].getDimension()).getName().replaceAll("_", " "));
            fontRenderer.drawString(WordUtils.capitalizeFully(dimName) + " (" + tabs[dimTabSelected - 5].getDimension() + ")", (width / 2) - 100, (height / 2) - 62, 1184274);

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
            drawString(fontRenderer, pages, xZero + (55 / 2) - fontRenderer.getStringWidth(pages) / 2, yZero + (14 / 2) - fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
        } else {
            int tex_width = 90;
            drawTexturedModalRect((width / 4) - (tex_width / 2), (height / 2) - (tex_height / 2), 0, 0, tex_width, tex_height);
            super.drawScreen(mouseX, mouseY, partialTicks);
            String name = "";
            Bonfire currentBonfire = BonfireRegistry.INSTANCE.getBonfire(bonfire.getID());
            if (currentBonfire != null) {
                name = currentBonfire.getName();
                if (!currentBonfire.isPublic()) {
                    drawCenteredStringNoShadow(mc.fontRenderer, I18n.format(LocalStrings.TEXT_PRIVATE), (width / 4), (height / 2) - (tex_height / 2) + 20, new Color(255, 255, 255).hashCode());
                }
            }
            drawCenteredStringNoShadow(mc.fontRenderer, name, (width / 4), (height / 2) - (tex_height / 2) + 10, new Color(255, 255, 255).hashCode());
            if (!ownerName.isEmpty())
                drawCenteredStringNoShadow(mc.fontRenderer, ownerName, (width / 4), (height / 2) - (tex_height / 2) + tex_height - 10, new Color(255, 255, 255).hashCode());
        }
        GL11.glPopMatrix();
    }

    private void drawSelectedBonfire(int mouseX, int mouseY, @SuppressWarnings("unused") float partialTicks) {
        if (bonfireSelected >= BONFIRE1) {
            if (bonfires != null) {
                if (bonfires.get(tabs[dimTabSelected-5].getDimension()) != null) {
                    Bonfire b = bonfires.get(tabs[dimTabSelected-5].getDimension()).get(bonfirePage).get(bonfireSelected-11);
                    if (b != null) {
                        int nameX = (width / 2) - 10 + 12;
                        int nameY = (height / 2) - 45;
                        int nameEndX = nameX + fontRenderer.getStringWidth(b.getName());
                        int nameEndY = nameY + fontRenderer.FONT_HEIGHT;
                        fontRenderer.drawString(b.getName(), nameX, nameY, new Color(255, 255, 255).hashCode());
                        fontRenderer.drawString("X:" + b.getPos().getX() + " Y:" + b.getPos().getY() + " Z:" + b.getPos().getZ(), nameX, nameY + fontRenderer.FONT_HEIGHT + 3, new Color(255, 255, 255).hashCode());
                        fontRenderer.drawString(ownerName, nameX, nameY + (fontRenderer.FONT_HEIGHT + 3) * 2, new Color(255, 255, 255).hashCode());
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

    private void drawTravelMenu(int mouseX, int mouseY, float partialTicks) {
        int trueWidth = 219;
        Minecraft.getMinecraft().renderEngine.bindTexture(TRAVEL_TEX);
        RenderHelper.enableGUIStandardItemLighting();
        for (GuiButtonDimensionTab tab : tabs) {
            tab.drawButton(mc, mouseX, mouseY, partialTicks);
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(TRAVEL_TEX);
        drawTexturedModalRect((width / 2) - (trueWidth / 2), (height / 2) - (travel_height / 2), 0, 0, trueWidth, travel_height);
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

    private void updateButtons() {
        for (GuiButtonDimensionTab tab : tabs) {
            tab.visible = false;
        }
        if (travelOpen) {
            if (bonfireSelected >= BONFIRE1) {
                travel.visible = true;
                travel.x = (width / 2) - 5 + 12;
                travel.y = (height / 2) + 38;
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
                if (tabs[dimTabSelected - 5] != null) {
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
            prev.enabled = currentPage != 0;
            next.enabled = currentPage != pages.size() - 1;
            bonfire_prev.enabled = bonfirePage != 0;
            bonfire_next.enabled = bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null && bonfirePage != bonfires.get(tabs[dimTabSelected - 5].getDimension()).size() - 1;

        } else {
            bonfire_prev.visible = false;
            bonfire_prev.enabled = false;
            bonfire_next.visible = false;
            bonfire_next.enabled = false;
            travel.visible = true;
            travel.x = (width / 4) - (80 / 2);
            travel.y = (height / 2) - (tex_height / 2) + 20;
            if (BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()) != null) {
                if (!BonfireRegistry.INSTANCE.getBonfire(bonfire.getID()).isPublic()) {
                    travel.y = (height / 2) - (tex_height / 2) + 30;
                    leave.y = (height / 2) - (tex_height / 2) + 52;
                }
            }
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

    private int addButton(int id) {
        buttonIDs.add(id);
        return id;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonIDs = new ArrayList<>();
        List<Integer> dimensions = new ArrayList<>();
        pages = new ArrayList<>();
        bonfires = new HashMap<>();
        buttonList.add(travel = new GuiButton(addButton(TRAVEL), (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 25, 80, 20, I18n.format(LocalStrings.BUTTON_TRAVEL)));
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
            tabs[i].x = ((width) / 2 - (travel_width / 2) + (i * 28) + gap / 2);
            tabs[i].y = (height / 2) - (travel_width / 2) + 1;
        }
        for (int i = 0; i < bonfireButtons.length; i++) {
            buttonList.add(bonfireButtons[i]);
            bonfireButtons[i].x = (width / 2) - 88 - 12;
            bonfireButtons[i].y = (height / 2) + (bonfireButtons[i].height) * i - 50;
        }
        prev.x = ((width) / 2 - (travel_width / 2)) - 8;
        prev.y = (height / 2) - (travel_width / 2) + 6;
        int sixTabs = 6 * 28;
        int gap = travel_width - sixTabs;
        next.x = ((width) / 2 - (travel_width / 2) + (6 * 28) + gap / 2);
        next.y = (height / 2) - (travel_width / 2) + 6;
        bonfire_prev.x = (width / 2) - (travel_width / 2) + 16;
        bonfire_prev.y = (height / 2) - (travel_height / 2) + 128 - 17;
        bonfire_next.x = (width / 2) - (travel_width / 2) + 63;
        bonfire_next.y = (height / 2) - (travel_height / 2) + 128 - 17;
        updateBonfires();
        List<Integer> dimList = new ArrayList<>(Arrays.asList(DimensionManager.getStaticDimensionIDs()));
        dimList = Lists.reverse(dimList);
        dimensions.addAll(dimList);
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

    private void updateBonfires() {
        bonfires.clear();
        for (int dim : DimensionManager.getStaticDimensionIDs()) {
            Map<Integer, List<List<Bonfire>>> series = createSeries(dim);
            if (series != null)
                if (series.get(dim) != null)
                    bonfires.put(dim, series.get(dim));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
