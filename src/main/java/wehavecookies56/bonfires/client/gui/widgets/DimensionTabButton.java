package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

import java.util.List;

/**
 * Created by Toby on 14/11/2016.
 */
public class DimensionTabButton extends Button {

    private BonfireScreen parent;
    private ResourceKey<Level> dimension;
    private int id;
    private Item icon = Items.FILLED_MAP;

    public DimensionTabButton(BonfireScreen parent, int buttonId, int x, int y) {
        super(new Builder(Component.empty(), b -> {
            parent.action(buttonId);
        }).pos(x, y).size(28, 30));
        this.id = buttonId;
        this.parent = parent;
    }

    private Item getIcon() {
        if (icon == Items.FILLED_MAP) {
            List<String> icons = BonfiresConfig.Client.tabIcons;
            for (String s : icons) {
                String[] split = s.split("=");
                if (split.length == 2) {
                    String dimID = split[0];
                    String item = split[1];
                    if (dimID.equals(dimension.location().toString())) {
                        if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(item))) {
                            return icon = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
                        } else {
                            return icon;
                        }
                    }
                } else {
                    Bonfires.LOGGER.error(s + " is an invalid icon setting");
                }
            }
        } else {
            return icon;
        }
        return icon;
    }

    public void setIcon(Item icon) {
        this.icon = icon;
    }
    
    void resetIcon() {
        this.icon = Items.FILLED_MAP;
    }
    
    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        this.resetIcon();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            int tab_width = 28;
            int tab_height = 30;
            int tab_u = 28;
            int tab_v = parent.travel_height;
            guiGraphics.setColor(1, 1, 1, 1);
            if (parent.dimTabSelected == id) {
                tab_v = parent.travel_height + 30;
                tab_height = 32;
                guiGraphics.blit(parent.TRAVEL_TEX, getX(), getY(), tab_u, tab_v, tab_width, tab_height);
                guiGraphics.renderFakeItem(new ItemStack(getIcon(), 1), getX() + (tab_width / 2) - 8, getY() + (tab_height / 2) - 8);
            } else {
                guiGraphics.blit(parent.TRAVEL_TEX, getX(), getY() - 1, tab_u, tab_v, tab_width, tab_height);
                guiGraphics.renderFakeItem(new ItemStack(getIcon(), 1), getX() + (tab_width / 2) - 8, getY() + (tab_height / 2) - 8 -1);
            }
        }
    }
}
