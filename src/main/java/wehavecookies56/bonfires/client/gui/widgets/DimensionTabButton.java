package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

import java.util.List;

/**
 * Created by Toby on 14/11/2016.
 */
public class DimensionTabButton extends Button {

    private BonfireScreen parent;
    private RegistryKey<World> dimension;
    private int id;
    private Item icon = Items.FILLED_MAP;

    public DimensionTabButton(BonfireScreen parent, int buttonId, int x, int y) {
        super(x, y, 28, 30, new TranslationTextComponent(""), b -> {
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
                        if (GameRegistry.findRegistry(Item.class).containsKey(new ResourceLocation(item))) {
                            return icon = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(item));
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
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.pushMatrix();
        if (visible) {
            int tab_width = 28;
            int tab_height = 30;
            int tab_u = 28;
            int tab_v = parent.travel_height;
            Minecraft.getInstance().textureManager.bind(parent.TRAVEL_TEX);
            RenderSystem.color4f(1, 1, 1, 1);
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
        RenderSystem.popMatrix();
    }
}
