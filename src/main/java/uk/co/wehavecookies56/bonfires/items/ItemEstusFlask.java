package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import uk.co.wehavecookies56.bonfires.Bonfires;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Toby on 05/11/2016.
 */
public class ItemEstusFlask extends ItemFood {

    public ItemEstusFlask(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setAlwaysEdible();
        this.setMaxStackSize(1);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Bonfires.tabBonfires);
    }

    @Override
    public int getMetadata(ItemStack stack) {
        if (stack.getTagCompound() != null) {
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
    @Nonnull
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World worldIn, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote) {
            if (stack.getTagCompound() != null) {
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
        if (stack.getTagCompound() != null) {
            return 1 - (double)stack.getTagCompound().getInteger("estus") / (double)stack.getTagCompound().getInteger("uses");
        } else {
            return 1 - (double)0 / (double)15;
        }
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        if (stack.getTagCompound() != null) {
            return MathHelper.hsvToRGB(Math.max(0.0F, 1.0F / ((float)stack.getTagCompound().getInteger("uses") - (float)stack.getTagCompound().getInteger("estus")) / (float)stack.getTagCompound().getInteger("uses")) / 3.0F, 1.0F, 1.0F);
        } else {
            return 0x00000000;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound();
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (int i = 3; i < 16; i++) {
                ItemStack stack = new ItemStack(this);
                stack.setTagCompound(new NBTTagCompound());
                if (stack.getTagCompound() != null) {
                    stack.getTagCompound().setInteger("uses", i);
                    stack.getTagCompound().setInteger("estus", i);
                }
                items.add(stack);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() != null) {
            if (stack.getTagCompound().hasKey("uses")) {
                if (stack.getTagCompound().hasKey("estus")) {
                    tooltip.add("Uses: " + stack.getTagCompound().getInteger("estus") + "/" + stack.getTagCompound().getInteger("uses"));
                }
            }
        }
    }
}
