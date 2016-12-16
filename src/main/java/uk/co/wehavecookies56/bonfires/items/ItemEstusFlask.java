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
            switch (stack.getTagCompound().getInteger("uses")) {
                case 3:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 3:
                            return 3;
                        case 2:
                            return 2;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 4:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 4:
                            return 3;
                        case 3:
                            return 2;
                        case 2:
                            return 2;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 5:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 5:
                            return 3;
                        case 4:
                            return 3;
                        case 3:
                            return 2;
                        case 2:
                            return 2;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 6:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 6:
                            return 3;
                        case 5:
                            return 3;
                        case 4:
                            return 2;
                        case 3:
                            return 2;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 7:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 7:
                            return 3;
                        case 6:
                            return 3;
                        case 5:
                            return 3;
                        case 4:
                            return 2;
                        case 3:
                            return 2;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 8:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 8:
                            return 3;
                        case 7:
                            return 3;
                        case 6:
                            return 3;
                        case 5:
                            return 2;
                        case 4:
                            return 2;
                        case 3:
                            return 2;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 9:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 9:
                            return 3;
                        case 8:
                            return 3;
                        case 7:
                            return 3;
                        case 6:
                            return 2;
                        case 5:
                            return 2;
                        case 4:
                            return 2;
                        case 3:
                            return 1;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 10:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 10:
                            return 3;
                        case 9:
                            return 3;
                        case 8:
                            return 3;
                        case 7:
                            return 3;
                        case 6:
                            return 2;
                        case 5:
                            return 2;
                        case 4:
                            return 2;
                        case 3:
                            return 1;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 11:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 11:
                            return 3;
                        case 10:
                            return 3;
                        case 9:
                            return 3;
                        case 8:
                            return 3;
                        case 7:
                            return 2;
                        case 6:
                            return 2;
                        case 5:
                            return 2;
                        case 4:
                            return 2;
                        case 3:
                            return 1;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 12:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 12:
                            return 3;
                        case 11:
                            return 3;
                        case 10:
                            return 3;
                        case 9:
                            return 3;
                        case 8:
                            return 2;
                        case 7:
                            return 2;
                        case 6:
                            return 2;
                        case 5:
                            return 2;
                        case 4:
                            return 1;
                        case 3:
                            return 1;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 13:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 13:
                            return 3;
                        case 12:
                            return 3;
                        case 11:
                            return 3;
                        case 10:
                            return 3;
                        case 9:
                            return 3;
                        case 8:
                            return 2;
                        case 7:
                            return 2;
                        case 6:
                            return 2;
                        case 5:
                            return 2;
                        case 4:
                            return 1;
                        case 3:
                            return 1;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 14:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 14:
                            return 3;
                        case 13:
                            return 3;
                        case 12:
                            return 3;
                        case 11:
                            return 3;
                        case 10:
                            return 3;
                        case 9:
                            return 2;
                        case 8:
                            return 2;
                        case 7:
                            return 2;
                        case 6:
                            return 2;
                        case 5:
                            return 2;
                        case 4:
                            return 1;
                        case 3:
                            return 1;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
                case 15:
                    switch (stack.getTagCompound().getInteger("estus")) {
                        case 15:
                            return 3;
                        case 14:
                            return 3;
                        case 13:
                            return 3;
                        case 12:
                            return 3;
                        case 11:
                            return 3;
                        case 10:
                            return 2;
                        case 9:
                            return 2;
                        case 8:
                            return 2;
                        case 7:
                            return 2;
                        case 6:
                            return 2;
                        case 5:
                            return 1;
                        case 4:
                            return 1;
                        case 3:
                            return 1;
                        case 2:
                            return 1;
                        case 1:
                            return 1;
                        case 0:
                            return 0;
                    }
                    break;
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
        if (!worldIn.isRemote) {
            if (stack.hasTagCompound()) {
                if (stack.getTagCompound().getInteger("estus") > 0) {
                    stack.getTagCompound().setInteger("estus", stack.getTagCompound().getInteger("estus") - 1);
                    entityLiving.heal(6);
                }
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
