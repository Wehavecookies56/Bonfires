package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Toby on 05/11/2016.
 */
public class ItemEstusFlask extends ItemFood {

    int maxUses, currentUses;

    public ItemEstusFlask(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setAlwaysEdible();
        this.setMaxStackSize(1);
    }

    @Override
    public int getMetadata(ItemStack stack) {
        if (stack.hasTagCompound()) {
            if(stack.getTagCompound().getInteger("estus") == 0) {
                return 0;
            } else if (stack.getTagCompound().getInteger("estus") == stack.getTagCompound().getInteger("uses") || stack.getTagCompound().getInteger("estus") >= stack.getTagCompound().getInteger("uses") / 2) {
                return 3;
            } else if ((stack.getTagCompound().getInteger("estus") <= stack.getTagCompound().getInteger("uses") / 2) && (stack.getTagCompound().getInteger("estus") > stack.getTagCompound().getInteger("uses") / 4)) {
                return 2;
            } else if (stack.getTagCompound().getInteger("estus") <= stack.getTagCompound().getInteger("uses") / 4) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Nullable
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().getInteger("estus") > 0) {
                stack.getTagCompound().setInteger("estus", stack.getTagCompound().getInteger("estus")-1);
                entityLiving.heal(6);
            }
        }
        return stack;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return 1 - (double)stack.getTagCompound().getInteger("estus") / (double)stack.getTagCompound().getInteger("uses");
        } else {
            return 1 - (double)0 / (double)15;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        super.onFoodEaten(stack, worldIn, player);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 3; i < 16; i++) {
            ItemStack stack = new ItemStack(itemIn);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("uses", i);
            stack.getTagCompound().setInteger("estus", i);
            subItems.add(stack);
        }


    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("uses")) {
                if (stack.getTagCompound().hasKey("estus")) {
                    tooltip.add("Uses: " + stack.getTagCompound().getInteger("estus") + "/" + stack.getTagCompound().getInteger("uses"));
                }
            }
        }
        super.addInformation(stack, playerIn, tooltip, advanced);
    }
}
