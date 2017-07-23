package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import uk.co.wehavecookies56.bonfires.BonfiresConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Toby on 14/11/2016.
 */
public class GuiButtonDimensionTab extends GuiButton {

    GuiBonfire parent;
    int dimension;
    Item icon = Items.FILLED_MAP;

    public GuiButtonDimensionTab(GuiBonfire parent, int buttonId, int x, int y) {
        super(buttonId, x, y, 28, 30, "");
        this.parent = parent;
        this.dimension = dimension;
    }

    public Item getIcon() {
        if (icon == Items.FILLED_MAP) {
            if (BonfiresConfig.tabIcons.containsKey(getDimension() + "")) {
                if (GameRegistry.findRegistry(Item.class).containsKey(new ResourceLocation(BonfiresConfig.tabIcons.get(getDimension() + "")))) {
                    return icon = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(BonfiresConfig.tabIcons.get(getDimension() + "")));
                } else {
                    return icon;
                }
            } else {
                return icon;
            }
        } else {
            return icon;
        }
    }

    public void setIcon(Item icon) {
        this.icon = icon;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        if (visible) {
            int tab_width = 28;
            int tab_height = 30;
            int tab_u = 28;
            int tab_v = parent.travel_height;
            Minecraft.getMinecraft().renderEngine.bindTexture(parent.TRAVEL_TEX);
            GL11.glColor4f(1, 1, 1, 1);
            if (parent.dimTabSelected == id) {
                tab_v = parent.travel_height + 30;
                tab_height = 32;
                drawTexturedModalRect(x, y, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(getIcon()), x + (tab_width / 2) - 8, y + (tab_height / 2) - 8);
            } else {
                drawTexturedModalRect(x, y - 1, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(getIcon()), x + (tab_width / 2) - 8, y + (tab_height / 2) - 8 -1);
            }
        }
        GL11.glPopMatrix();
    }
}
