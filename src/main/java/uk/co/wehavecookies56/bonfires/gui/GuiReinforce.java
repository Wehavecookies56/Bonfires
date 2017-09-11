package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.LocalStrings;
import uk.co.wehavecookies56.bonfires.ReinforceHandler;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.ReinforceItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiReinforce extends GuiScreen {

    public List<ItemStack> reinforceableItems;
    public List<Integer> slots;
    int itemSelected = 0;
    float scrollOffset = 0;
    final int SCROLLBAR = 0;
    final int ITEMS = 1;
    final int CONFIRM = 2;
    GuiButtonReinforceItem items;
    GuiBonfire parent;
    GuiButtonScrollBar scrollBar;
    GuiButton confirm;
    private static ResourceLocation texture = new ResourceLocation(Bonfires.modid, "textures/gui/reinforce_menu.png");
    int texWidth = 256;
    int texHeight = 219;

    public GuiReinforce(GuiBonfire parent) {
        super();
        this.parent = parent;
        mc = Minecraft.getMinecraft();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        scrollBar.mousePressed(mc, mouseX, mouseY);
        items.mousePressed(mc, mouseX, mouseY, scrollOffset);
        confirm.mousePressed(mc, mouseX, mouseY);
        if (confirm.isMouseOver()) {
            actionPerformed(confirm);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        scrollBar.mouseReleased(mouseX, mouseY);
        confirm.mouseReleased(mouseX, mouseY);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        scrollBar.mouseDragged(mc, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        getReinforceableItems();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        buttonList.add(scrollBar = new GuiButtonScrollBar(SCROLLBAR, (scaledResolution.getScaledWidth() / 2) + (texWidth / 2) - 16, (scaledResolution.getScaledHeight() / 2) - (texHeight / 2) + 41, 8, 15, (scaledResolution.getScaledHeight() / 2) - (texHeight / 2) + 42, (scaledResolution.getScaledHeight() / 2) - (texHeight / 2) + 42 + 155));
        buttonList.add(items = new GuiButtonReinforceItem(this, ITEMS, (scaledResolution.getScaledWidth() / 2) - (texWidth / 2) + 9, (scaledResolution.getScaledHeight() / 2) - (texHeight / 2) + 41, 239, 171));
        buttonList.add(confirm = new GuiButton(CONFIRM, 0, 0, 50, 20, LocalStrings.BUTTON_REINFORCE));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case CONFIRM:
                if (itemSelected != -1) {
                    if (hasRequiredItems(mc.player, ReinforceHandler.getRequiredResources(reinforceableItems.get(itemSelected)))) {
                        ItemStack reinforcedStack = reinforceableItems.get(itemSelected).copy();
                        ReinforceHandler.getHandler(reinforcedStack).levelup(1);
                        int level = ReinforceHandler.getHandler(reinforcedStack).level();
                        if (level != 0)
                            reinforcedStack.setStackDisplayName(I18n.format(reinforcedStack.getUnlocalizedName()+".name") + " +" + level);
                        PacketDispatcher.sendToServer(new ReinforceItem(reinforcedStack, slots.get(itemSelected), ReinforceHandler.getHandler(reinforcedStack).level()));
                        mc.player.inventory.setInventorySlotContents(slots.get(itemSelected), reinforcedStack);
                        getReinforceableItems();
                    }
                }
                break;
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int centerX = (scaledResolution.getScaledWidth() / 2) - (texWidth / 2);
        int centerY = (scaledResolution.getScaledHeight() / 2) - (texHeight / 2);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(centerX, centerY, 0, 0, texWidth, texHeight);
        super.drawScreen(mouseX, mouseY, partialTicks);
        float scrollBarHeight = scrollBar.bottom - (scrollBar.top);
        float listHeight = (36 * reinforceableItems.size());
        if (scrollBarHeight >= listHeight) {
            scrollBar.visible = false;
            scrollBar.enabled = false;
        } else {
            scrollBar.visible = true;
            scrollBar.enabled = true;
        }

        float buttonRelativeToBar = scrollBar.y - (scrollBar.top-1);
        float scrollPos = buttonRelativeToBar != 0 ? buttonRelativeToBar / scrollBarHeight : 0;
        scrollOffset = scrollPos*(listHeight-scrollBarHeight);
        items.drawButtons(mc, mouseX, mouseY, partialTicks, scrollOffset);
    }

    public void getReinforceableItems() {
        List<ItemStack> items = new ArrayList<>();
        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            if (ReinforceHandler.hasHandler(mc.player.inventory.getStackInSlot(i))) {
                items.add(mc.player.inventory.getStackInSlot(i));
                slots.add(i);
            }
        }
        reinforceableItems = items;
        this.slots = slots;
    }

    public static boolean hasRequiredItems(EntityPlayer player, ItemStack reqiuired) {
        int hasCount = 0;
        int countRequired = reqiuired.getCount();
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (ItemStack.areItemStacksEqual(reqiuired, player.inventory.getStackInSlot(i))) {
                return true;
            } else {
                if (ItemStack.areItemsEqual(player.inventory.getStackInSlot(i), reqiuired)) {
                    hasCount += player.inventory.getStackInSlot(i).getCount();
                }
            }
        }
        if (hasCount >= countRequired) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void removeRequiredItems(EntityPlayer player, ItemStack reqiuired) {
        if (hasRequiredItems(player, reqiuired)) {
            int remaining = reqiuired.getCount();
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                if (ItemStack.areItemStacksEqual(reqiuired, player.inventory.getStackInSlot(i))) {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    remaining = 0;
                    return;
                } else {
                    if (ItemStack.areItemsEqual(player.inventory.getStackInSlot(i), reqiuired)) {
                        if (player.inventory.getStackInSlot(i).getCount() >= remaining) {
                            ItemStack stackWithNewCount = player.inventory.getStackInSlot(i);
                            stackWithNewCount.shrink(remaining);
                            player.inventory.setInventorySlotContents(i, stackWithNewCount);
                            remaining = 0;
                            return;
                        } else {
                            player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                            remaining -= player.inventory.getStackInSlot(i).getCount();
                        }

                    }
                }
            }
        }
    }
}
