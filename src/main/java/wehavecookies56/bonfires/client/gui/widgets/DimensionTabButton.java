package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
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
        super(x, y, 28, 30, new TextComponent(""), b -> {
            parent.action(buttonId);
        });
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
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            int tab_width = 28;
            int tab_height = 30;
            int tab_u = 28;
            int tab_v = parent.travel_height;
            RenderSystem.setShaderTexture(0, parent.TRAVEL_TEX);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            if (parent.dimTabSelected == id) {
                tab_v = parent.travel_height + 30;
                tab_height = 32;
                blit(stack, x, y, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getInstance().getItemRenderer().renderGuiItem(new ItemStack(getIcon(), 1), x + (tab_width / 2) - 8, y + (tab_height / 2) - 8);
            } else {
                blit(stack, x, y - 1, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getInstance().getItemRenderer().renderGuiItem(new ItemStack(getIcon(), 1), x + (tab_width / 2) - 8, y + (tab_height / 2) - 8 -1);
            }
        }
    }
}
