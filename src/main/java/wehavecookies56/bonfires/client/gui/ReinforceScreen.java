package wehavecookies56.bonfires.client.gui;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.client.gui.widgets.ReinforceItemButton;
import wehavecookies56.bonfires.client.gui.widgets.ScrollBarButton;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.server.ReinforceItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReinforceScreen extends Screen {

    public List<ItemStack> reinforceableItems;
    public List<Integer> slots;
    public int itemSelected = -1;
    float scrollOffset = 0;
    final int SCROLLBAR = 0;
    final int ITEMS = 1;
    final int CONFIRM = 2;
    ReinforceItemButton items;
    BonfireScreen parent;
    public ScrollBarButton scrollBar;
    Button confirm;
    private static ResourceLocation texture = new ResourceLocation(Bonfires.modid, "textures/gui/reinforce_menu.png");
    int texWidth = 256;
    int texHeight = 219;
    Minecraft mc;

    public ReinforceScreen(BonfireScreen parent) {
        super(Component.empty());
        this.parent = parent;
        mc = Minecraft.getInstance();
        if (!parent.canReinforce) {
            mc.setScreen(null);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        scrollBar.mouseClicked(mouseX, mouseY, button);
        items.mousePressed(mc, mouseX, mouseY, scrollOffset);
        confirm.mouseClicked(mouseX, mouseY, button);
        if (confirm.isMouseOver(mouseX, mouseY)) {
            action(CONFIRM);
        }
        updateButtons();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        scrollBar.mouseReleased(mouseX, mouseY, button);
        confirm.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        scrollBar.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        scrollBar.mouseScrolled(mouseX, mouseY, scrollDelta);
        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

    public void updateButtons() {
        Window window = mc.getWindow();
        int centerX = (window.getWidth() / 2) - (texWidth / 2);
        int centerY = (window.getHeight() / 2) - (texHeight / 2);
        if (itemSelected != -1) {
            ReinforceHandler.ReinforceLevel reinforceLevel = ReinforceHandler.getReinforceLevel(reinforceableItems.get(itemSelected));
            if (reinforceLevel.level() != reinforceLevel.maxLevel()) {
                if (ReinforceHandler.hasRequiredItems(mc.player, ReinforceHandler.getRequiredResources(reinforceableItems.get(itemSelected)))) {
                    confirm.active = true;
                } else {
                    confirm.active = false;
                }
            } else {
                confirm.active = false;
            }
        }
    }

    @Override
    public void init() {
        getReinforceableItems();
        Window window = mc.getWindow();
        int centerX = (window.getGuiScaledWidth() / 2) - (texWidth / 2);
        int centerY = (window.getGuiScaledHeight() / 2) - (texHeight / 2);
        addRenderableOnly(scrollBar = new ScrollBarButton(SCROLLBAR, (window.getGuiScaledWidth() / 2) + (texWidth / 2) - 16, (window.getGuiScaledHeight() / 2) - (texHeight / 2) + 41, 8, 15, (window.getGuiScaledHeight() / 2) - (texHeight / 2) + 41, (window.getGuiScaledHeight() / 2) - (texHeight / 2) + 42 + 155));
        addRenderableOnly(items = new ReinforceItemButton(this, ITEMS, (window.getGuiScaledWidth() / 2) - (texWidth / 2) + 9, (window.getGuiScaledHeight() / 2) - (texHeight / 2) + 41, 239, 171));
        addRenderableWidget(confirm = Button.builder(Component.translatable(LocalStrings.BUTTON_REINFORCE), button -> {}).pos(centerX + 180, centerY + 14).size(60, 20).build());
        if (reinforceableItems.size() > 1) {
            itemSelected = 0;
        } else {
            confirm.active = false;
        }
        updateButtons();
        super.init();
    }

    public void action(int id) {
        switch (id) {
            case CONFIRM:
                if (itemSelected != -1) {
                    if (ReinforceHandler.hasRequiredItems(mc.player, ReinforceHandler.getRequiredResources(reinforceableItems.get(itemSelected)))) {
                        ItemStack reinforcedStack = reinforceableItems.get(itemSelected).copy();
                        ReinforceHandler.levelUp(reinforcedStack);
                        reinforcedStack.getTag().putInt("Damage", 0);
                        PacketHandler.sendToServer(new ReinforceItem(slots.get(itemSelected)));
                        mc.player.getInventory().setItem(slots.get(itemSelected), reinforcedStack);
                        getReinforceableItems();
                    }
                }
                break;
        }
        updateButtons();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        Window window = mc.getWindow();
        int centerX = (window.getGuiScaledWidth() / 2) - (texWidth / 2);
        int centerY = (window.getGuiScaledHeight() / 2) - (texHeight / 2);
        guiGraphics.blit(texture, centerX, centerY, 0, 0, texWidth, texHeight);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        int scrollBarHeight = (scrollBar.getBottom()) - (scrollBar.top);
        int listHeight = (36 * reinforceableItems.size());
        if (scrollBarHeight >= listHeight) {
            scrollBar.visible = false;
            scrollBar.active = false;
        } else {
            scrollBar.visible = true;
            scrollBar.active = true;
        }

        float buttonRelativeToBar = scrollBar.getY() - (scrollBar.top-1);
        float scrollPos = Math.min(buttonRelativeToBar != 0 ? buttonRelativeToBar / (scrollBarHeight) : 0, 1);
        scrollOffset = scrollPos*(listHeight-scrollBarHeight);
        items.drawButtons(guiGraphics, mouseX, mouseY, partialTicks, scrollOffset);
        guiGraphics.drawString(font, Component.translatable(LocalStrings.TEXT_REINFORCE), centerX + 10, centerY + 10, new Color(255, 255, 255).hashCode());
        if (itemSelected != -1) {
            ItemStack required = ReinforceHandler.getRequiredResources(reinforceableItems.get(itemSelected));
            int hasCount = 0;
            for (int i = 0; i < mc.player.getInventory().items.size(); i++) {
                if (ItemStack.isSameItem(mc.player.getInventory().getItem(i), required)) {
                    hasCount += mc.player.getInventory().getItem(i).getCount();
                }
            }
            ReinforceHandler.ReinforceLevel reinforceLevel = ReinforceHandler.getReinforceLevel(reinforceableItems.get(itemSelected));
            if (reinforceLevel.level() != reinforceLevel.maxLevel()) {
                guiGraphics.drawString(font, required.getHoverName().getString() + ": " + hasCount + " / " + required.getCount(), centerX + 10, centerY + 24, new Color(255, 255, 255).hashCode());
            } else {
                guiGraphics.drawString(font, Component.translatable(LocalStrings.TEXT_MAX_LEVEL), centerX + 10, centerY + 24, new Color(255, 255, 255).hashCode());
            }
        }
    }

    public void getReinforceableItems() {
        List<ItemStack> items = new ArrayList<>();
        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < mc.player.getInventory().items.size(); i++) {
            if (ReinforceHandler.canReinforce(mc.player.getInventory().getItem(i))) {
                items.add(mc.player.getInventory().getItem(i));
                slots.add(i);
            }
        }
        reinforceableItems = items;
        this.slots = slots;
    }

    @Override
    public void tick() {
        updateButtons();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
