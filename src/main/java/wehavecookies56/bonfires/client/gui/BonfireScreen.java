package wehavecookies56.bonfires.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;
import wehavecookies56.bonfires.*;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.gui.widgets.BonfireButton;
import wehavecookies56.bonfires.client.gui.widgets.BonfirePageButton;
import wehavecookies56.bonfires.client.gui.widgets.DimensionTabButton;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Toby on 10/11/2016.
 */
public class BonfireScreen extends Screen {

    private final ResourceLocation MENU = new ResourceLocation(Bonfires.modid, "textures/gui/bonfire_menu.png");
    public final ResourceLocation TRAVEL_TEX = new ResourceLocation(Bonfires.modid, "textures/gui/travel_menu.png");

    private Button travel;
    private Button leave;
    private Button reinforce;
    @SuppressWarnings("unused")
    Button back;
    private Button next;
    private Button prev;

    public Map<RegistryKey<World>, List<List<Bonfire>>> bonfires;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<Integer> buttonIDs;
    private List<List<RegistryKey<World>>> pages;

    private int currentPage = 0;
    public int bonfirePage = 0;

    private final BonfireTileEntity bonfire;
    private boolean travelOpen;

    private final int TRAVEL = 0;
    private final int LEAVE = 1;
    private final int REINFORCE = 20;
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
    public final int BONFIRE1 = 11;
    private final int BONFIRE2 = 12;
    private final int BONFIRE3 = 13;
    private final int BONFIRE4 = 14;
    private final int BONFIRE5 = 15;
    private final int BONFIRE6 = 16;
    private final int BONFIRE7 = 17;
    private final int BONFIRE_NEXT = 18;
    private final int BONFIRE_PREV = 19;

    public int dimTabSelected = TAB1;
    public int bonfireSelected = 0;

    public DimensionTabButton[] tabs;
    private BonfireButton[] bonfireButtons;
    private BonfirePageButton bonfire_next;
    private BonfirePageButton bonfire_prev;

    private final int tex_height = 166;
    private final int travel_width = 195;
    public final int travel_height = 136;

    public String ownerName = "";

    public BonfireRegistry registry;
    public List<RegistryKey<World>> dimensions;

    public BonfireScreen(BonfireTileEntity bonfire, String ownerName, List<RegistryKey<World>> dimensions, BonfireRegistry registry) {
        super(new TranslationTextComponent(""));
        this.bonfire = bonfire;
        this.ownerName = ownerName;
        this.registry = registry;
        minecraft = Minecraft.getInstance();
        this.dimensions = dimensions.stream().sorted(Comparator.comparing(RegistryKey::location)).collect(Collectors.toList());
    }

    public void drawCenteredStringNoShadow(MatrixStack stack, FontRenderer fr, String text, int x, int y, int color) {
        fr.draw(stack, text, (x - (fr.width(text) / 2F)), (y - (fr.lineHeight / 2F)), color);
    }

    private Map<RegistryKey<World>, List<List<Bonfire>>> createSeries(RegistryKey<World> dimension) {
        List<Bonfire> bonfires = registry.getPrivateBonfiresByOwnerAndPublicPerDimension(Minecraft.getInstance().player.getUUID(), dimension.location());

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
            Map<RegistryKey<World>, List<List<Bonfire>>> series = new HashMap<>();
            series.put(dimension, book);
            return series;
        } else {
            return null;
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1, 1, 1, 1);
        Minecraft.getInstance().textureManager.bind(MENU);
        if (travelOpen) {
            drawTravelMenu(stack, mouseX, mouseY, partialTicks);

            String formattedName;
            if (I18n.exists(LocalStrings.getDimensionKey(tabs[dimTabSelected - 5].getDimension()))) {
                String dimName = (tabs[dimTabSelected - 5].getDimension().location().getPath().replaceAll("_", " "));
                formattedName = WordUtils.capitalizeFully(dimName);
            } else {
                formattedName = I18n.get(LocalStrings.getDimensionKey(tabs[dimTabSelected - 5].getDimension()));
            }
            font.draw(stack, formattedName + " (" + tabs[dimTabSelected - 5].getDimension().location() + ")", (width / 2F) - 100, (height / 2F) - 62, 1184274);

            if (bonfireSelected >= BONFIRE1) {
                super.render(stack, mouseX, mouseY, partialTicks);
                drawSelectedBonfire(stack, mouseX, mouseY, partialTicks);
            } else {
                super.render(stack, mouseX, mouseY, partialTicks);
            }
            String pages = "0/0";
            if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                pages = (bonfirePage + 1) + "/" + bonfires.get(tabs[dimTabSelected - 5].getDimension()).size();
            }
            int xZero = (width / 2) - (travel_width / 2) + 16;
            int yZero = (height / 2) - (travel_height / 2) + 128 - 17;
            drawString(stack, font, pages, xZero + (55 / 2) - font.width(pages) / 2, yZero + (14 / 2) - font.lineHeight / 2, 0xFFFFFF);
        } else {
            int tex_width = 90;
            blit(stack, (width / 4) - (tex_width / 2), (height / 2) - (tex_height / 2), 0, 0, tex_width, tex_height);
            super.render(stack, mouseX, mouseY, partialTicks);
            String name = "";
            Bonfire currentBonfire = registry.getBonfire(bonfire.getID());
            if (currentBonfire != null) {
                name = currentBonfire.getName();
                if (!currentBonfire.isPublic()) {
                    drawCenteredStringNoShadow(stack, font, new TranslationTextComponent(LocalStrings.TEXT_PRIVATE).getString(), (width / 4), (height / 2) - (tex_height / 2) + 20, new Color(255, 255, 255).hashCode());
                }
            }
            drawCenteredStringNoShadow(stack, font, name, (width / 4), (height / 2) - (tex_height / 2) + 10, new Color(255, 255, 255).hashCode());
            if (!ownerName.isEmpty())
                drawCenteredStringNoShadow(stack, font, ownerName, (width / 4), (height / 2) - (tex_height / 2) + tex_height - 10, new Color(255, 255, 255).hashCode());
        }
        RenderSystem.popMatrix();
    }

    private void drawSelectedBonfire(MatrixStack stack, int mouseX, int mouseY, @SuppressWarnings("unused") float partialTicks) {
        if (bonfireSelected >= BONFIRE1) {
            if (bonfires != null) {
                if (bonfires.get(tabs[dimTabSelected-5].getDimension()) != null) {
                    Bonfire b = bonfires.get(tabs[dimTabSelected-5].getDimension()).get(bonfirePage).get(bonfireSelected-11);
                    if (b != null) {
                        int nameX = (width / 2) - 10 + 12;
                        int nameY = (height / 2) - 45;
                        int nameEndX = nameX + font.width(b.getName());
                        int nameEndY = nameY + font.lineHeight;
                        font.draw(stack, b.getName(), nameX, nameY, new Color(255, 255, 255).hashCode());
                        font.draw(stack, "X:" + b.getPos().getX() + " Y:" + b.getPos().getY() + " Z:" + b.getPos().getZ(), nameX, nameY + font.lineHeight + 3, new Color(255, 255, 255).hashCode());
                        font.draw(stack, ownerName, nameX, nameY + (font.lineHeight + 3) * 2, new Color(255, 255, 255).hashCode());
                        if (mouseX >= nameX && mouseX <= nameEndX && mouseY >= nameY && mouseY <= nameEndY) {
                            List<IReorderingProcessor> lines = new ArrayList<>();
                            lines.add(new TranslationTextComponent("ID: " + b.getId()).getVisualOrderText());
                            renderToolTip(stack, lines, mouseX, mouseY, font);
                        }
                    }
                }
            }
        }
    }

    private void drawTravelMenu(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int trueWidth = 219;
        Minecraft.getInstance().textureManager.bind(TRAVEL_TEX);
        //RenderHelper.enableGUIStandardItemLighting();
        for (DimensionTabButton tab : tabs) {
            tab.render(stack, mouseX, mouseY, partialTicks);
        }
        Minecraft.getInstance().textureManager.bind(TRAVEL_TEX);
        blit(stack, (width / 2) - (trueWidth / 2), (height / 2) - (travel_height / 2), 0, 0, trueWidth, travel_height);
    }

    public void action(int id) {
        action(id, false);
    }

    public void action(int id, boolean closesScreen) {
        switch (id) {
            case TRAVEL:
                if (!travelOpen) {
                    travelOpen = true;
                    PacketHandler.sendToServer(new RequestDimensionsFromServer());
                } else {
                    if (bonfireSelected >= BONFIRE1) {
                        if (bonfires != null) {
                            if (bonfires.get(tabs[dimTabSelected-5].getDimension()) != null) {
                                Bonfire b = bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage).get(bonfireSelected - 11);
                                Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, Minecraft.getInstance().player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
                                Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, b.getPos(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
                                PacketHandler.sendToServer(new Travel(b));
                                String formattedDimName;
                                if (I18n.exists(LocalStrings.getDimensionKey(b.getDimension()))) {
                                    String dimName = (b.getDimension().location().getPath().replaceAll("_", " "));
                                    formattedDimName = WordUtils.capitalizeFully(dimName);
                                } else {
                                    formattedDimName = I18n.get(LocalStrings.getDimensionKey(b.getDimension()));
                                }
                                Minecraft.getInstance().gui.setTitles(new TranslationTextComponent(b.getName()), null, 0, 0, 0);
                                Minecraft.getInstance().gui.setTitles(null, new TranslationTextComponent(formattedDimName), 0, 0, 0);
                                Minecraft.getInstance().gui.setTitles(null, null, 10, 20, 10);
                                minecraft.setScreen(null);
                            }
                        }
                        closesScreen = true;
                    }
                }
                break;
            case LEAVE:
                minecraft.setScreen(null);
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
            case REINFORCE:
                minecraft.setScreen(new ReinforceScreen(this));
                break;
        }
        updateButtons();
        if (!closesScreen) {
            PacketHandler.sendToServer(new RequestDimensionsFromServer());
        }
    }

    private void updateButtons() {
        for (DimensionTabButton tab : tabs) {
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
            reinforce.visible = false;
            leave.visible = false;
            next.visible = true;
            prev.visible = true;
            bonfire_prev.visible = true;
            bonfire_next.visible = true;
            prev.active = currentPage != 0;
            next.active = currentPage != pages.size() - 1;
            bonfire_prev.active = bonfirePage != 0;
            bonfire_next.active = bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null && bonfirePage != bonfires.get(tabs[dimTabSelected - 5].getDimension()).size() - 1;

        } else {
            bonfire_prev.visible = false;
            bonfire_prev.active = false;
            bonfire_next.visible = false;
            bonfire_next.active = false;
            travel.visible = true;
            travel.x = (width / 4) - (80 / 2);
            travel.y = (height / 2) - (tex_height / 2) + 20;
            if (registry.getBonfire(bonfire.getID()) != null) {
                if (!registry.getBonfire(bonfire.getID()).isPublic()) {
                    travel.y = (height / 2) - (tex_height / 2) + 30;
                    reinforce.y = (height / 2) - (tex_height / 2) + 51;
                    leave.y = (height / 2) - (tex_height / 2) + 72;
                }
            }
            reinforce.visible = true;
            leave.visible = true;
            next.visible = false;
            prev.visible = false;
            prev.active = false;
            next.active = false;
            for (DimensionTabButton tab : tabs) {
                tab.visible = false;
            }
            for (BonfireButton bonfire : bonfireButtons) {
                bonfire.visible = false;
            }
        }
        if (!BonfiresConfig.Common.enableReinforcing) {
            reinforce.visible = false;
            leave.y = reinforce.y;
        }
    }

    @Override
    protected void init() {
        buttonIDs = new ArrayList<>();
        pages = new ArrayList<>();
        bonfires = new HashMap<>();
        addButton(travel = new Button((width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 25, 80, 20, new TranslationTextComponent(LocalStrings.BUTTON_TRAVEL), button -> action(TRAVEL)));
        addButton(leave = new Button( (width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 62, 80, 20, new TranslationTextComponent(LocalStrings.BUTTON_LEAVE), button -> action(LEAVE, true)));
        addButton(reinforce = new Button((width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 41, 80, 20, new TranslationTextComponent(LocalStrings.BUTTON_REINFORCE), button -> action(REINFORCE, true)));
        addButton(next = new Button(0, 0, 20, 20, new TranslationTextComponent(">"), button -> action(NEXT)));
        addButton(prev = new Button(20, 0, 20, 20, new TranslationTextComponent("<"), button -> action(PREV)));
        addButton(bonfire_next = new BonfirePageButton(this, BONFIRE_NEXT, 0, 0, true));
        addButton(bonfire_prev = new BonfirePageButton(this, BONFIRE_PREV, 8, 0, false));
        tabs = new DimensionTabButton[] {
                new DimensionTabButton(this, TAB1, 0, 0),
                new DimensionTabButton(this, TAB2, 0, 0),
                new DimensionTabButton(this, TAB3, 0, 0),
                new DimensionTabButton(this, TAB4, 0, 0),
                new DimensionTabButton(this, TAB5, 0, 0),
                new DimensionTabButton(this, TAB6, 0, 0)
        };
        bonfireButtons = new BonfireButton[] {
                new BonfireButton(this, BONFIRE1, 0, 0),
                new BonfireButton(this, BONFIRE2, 0, 0),
                new BonfireButton(this, BONFIRE3, 0, 0),
                new BonfireButton(this, BONFIRE4, 0, 0),
                new BonfireButton(this, BONFIRE5, 0, 0),
                new BonfireButton(this, BONFIRE6, 0, 0),
                new BonfireButton(this, BONFIRE7, 0, 0)
        };
        for (int i = 0; i < tabs.length; i++) {
            addButton(tabs[i]);
            int sixTabs = 6 * 28;
            int gap = travel_width - sixTabs;
            tabs[i].x = ((width) / 2 - (travel_width / 2) + (i * 28) + gap / 2);
            tabs[i].y = (height / 2) - (travel_width / 2) + 1;
        }
        for (int i = 0; i < bonfireButtons.length; i++) {
            addButton(bonfireButtons[i]);
            bonfireButtons[i].x = (width / 2) - 88 - 12;
            bonfireButtons[i].y = (height / 2) + (bonfireButtons[i].getHeight()) * i - 50;
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
        dimensions = Lists.reverse(dimensions);
        int plus = 1;
        if (dimensions.size() % 6 == 0)
            plus = 0;
        for (int i = 0; i < (dimensions.size() / 6)+ plus; i++) {
            int start = i * 6;
            if (dimensions.size() < 6)
                start = 0;
            if ((start)+6 > dimensions.size())
                pages.add(dimensions.subList(start, dimensions.size()));
            else {
                pages.add(dimensions.subList(start, (start) + 6));
            }
        }
        updateButtons();
        super.init();
    }

    public void updateDimensionsFromServer(List<RegistryKey<World>> dimensions) {
        this.dimensions = dimensions;
        updateBonfires();
    }

    private void updateBonfires() {
        bonfires.clear();
        for (RegistryKey<World> dim : dimensions) {
            Map<RegistryKey<World>, List<List<Bonfire>>> series = createSeries(dim);
            if (series != null)
                if (series.get(dim) != null)
                    bonfires.put(dim, series.get(dim));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
