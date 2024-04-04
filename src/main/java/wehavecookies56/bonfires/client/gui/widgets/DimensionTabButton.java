package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

import java.util.List;

/**
 * Created by Toby on 14/11/2016.
 */
public class DimensionTabButton extends ButtonWidget {

    private BonfireScreen parent;
    private RegistryKey<World> dimension;
    private int id;
    private Item icon = Items.FILLED_MAP;

    public DimensionTabButton(BonfireScreen parent, int buttonId, int x, int y) {
        super(x, y, 28, 30, Text.empty(), b -> {
            parent.action(buttonId);
        }, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.id = buttonId;
        this.parent = parent;
    }

    private Item getIcon() {
        if (icon == Items.FILLED_MAP) {
            List<String> icons = Bonfires.CONFIG.client.tabIcons();
            for (String s : icons) {
                String[] split = s.split("=");
                if (split.length == 2) {
                    String dimID = split[0];
                    String item = split[1];
                    if (dimID.equals(dimension.getValue().toString())) {
                        if (Registries.ITEM.containsId(new Identifier(item))) {
                            return icon = Registries.ITEM.get(new Identifier(item));
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
    
    public RegistryKey<World> getDimension() {
        return dimension;
    }

    public void setDimension(RegistryKey<World> dimension) {
        this.dimension = dimension;
        this.resetIcon();
    }

    @Override
    public void renderWidget(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            int tab_width = 28;
            int tab_height = 30;
            int tab_u = 28;
            int tab_v = parent.travel_height;
            guiGraphics.setShaderColor(1, 1, 1, 1);
            if (parent.dimTabSelected == id) {
                tab_v = parent.travel_height + 30;
                tab_height = 32;
                guiGraphics.drawTexture(parent.TRAVEL_TEX, getX(), getY(), tab_u, tab_v, tab_width, tab_height);
                guiGraphics.drawItem(new ItemStack(getIcon(), 1), getX() + (tab_width / 2) - 8, getY() + (tab_height / 2) - 8);
            } else {
                guiGraphics.drawTexture(parent.TRAVEL_TEX, getX(), getY() - 1, tab_u, tab_v, tab_width, tab_height);
                guiGraphics.drawItem(new ItemStack(getIcon(), 1), getX() + (tab_width / 2) - 8, getY() + (tab_height / 2) - 8 -1);
            }
        }
    }
}
